package adcsistemas.loja_comprebem.api_pagamentos.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import adcsistemas.loja_comprebem.ApiTokenIntegracao;
import adcsistemas.loja_comprebem.enums.StatusVendaLojaVirtual;
import adcsistemas.loja_comprebem.exception.ErroResponseApiAsaas;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoEnvioDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CartaoCreditoAsaasHolderInfoDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CobrancaApiAsaasCartaoCreditoDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CobrancaApiAsaasCartaoDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CobrancaGeradaCartaoCreditoSaasDataDTO;
import adcsistemas.loja_comprebem.model.cobranca_pagamento.CobrancaPagamento;
import adcsistemas.loja_comprebem.model.dto.VendaCompraLojaVirtualDTO;
import adcsistemas.loja_comprebem.repository.PagamentoAsaasRepository;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;
import adcsistemas.loja_comprebem.service.VendaService;
import adcsistemas.loja_comprebem.service.SSLClient.HostIgnoringClient;
import adcsistemas.loja_comprebem.service.TokenAssasService.PagamentoSaasApiService;
import adcsistemas.loja_comprebem.utils.ValidaCpf;

@Controller
public class PagamentoAssasController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private PagamentoAsaasRepository pagamentoAsaasRepository;
	
	@Autowired
	private PagamentoSaasApiService pagamentoSaasApiService;
	
	@Autowired
	private VendaService vendaService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/pagamento/{idVendaCompra}")
	public ModelAndView pagamento(@PathVariable(value = "idVendaCompra",
					required = false) String idVendaCompra) {
		
		ModelAndView modelAndView = new ModelAndView("pagamento");
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExcluido(Long.parseLong(idVendaCompra));
		
		if(compraLojaVirtual == null) {
			modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
		}else {
			modelAndView.addObject("venda", vendaService.consultaVenda(compraLojaVirtual));
		}
		
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "finalizarCompraCartao")
	public ResponseEntity<String> finalizarCompraCartaoAsaas(
			@RequestParam("cardNumber") String cardNumber,
			@RequestParam("holderName") String holderName,
			@RequestParam("securityCode") String securityCode,
			@RequestParam("expirationMonth") String expirationMonth,
			@RequestParam("expirationYear") String expirationYear,
			@RequestParam("idVendaCampo") Long idVendaCampo,
			@RequestParam("cpf") String cpf,
			@RequestParam("qtdparcela") Integer qtdparcela,
			@RequestParam("cep") String cep,
			@RequestParam("rua") String rua,
			@RequestParam("numero") String numero,
			@RequestParam("estado") String estado,
			@RequestParam("cidade") String cidade) throws Exception{
		
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.findById(idVendaCampo).orElse(null);
		
		if (vendaCompraLojaVirtual == null) {
			return new ResponseEntity<String>("Código da venda não existe!", HttpStatus.OK);
		}
		
		String cpfLimpo =  cpf.replaceAll("\\.", "").replaceAll("\\-", "");
		
		if (!ValidaCpf.isCPF(cpfLimpo)) {
			return new ResponseEntity<String>("CPF informado é inválido.", HttpStatus.OK);
		}
		
		
		if (qtdparcela > 12 || qtdparcela <= 0) {
			return new ResponseEntity<String>("Quantidade de parcelar deve ser de  1 até 12.", HttpStatus.OK);
		}
		
		if (vendaCompraLojaVirtual.getValorTotal().doubleValue() <= 0) {
			return new ResponseEntity<String>("Valor da venda não pode ser Zero(0).", HttpStatus.OK);
		}
		
		if (vendaCompraLojaVirtual.getExcluido()) {
			return new ResponseEntity<String>("Venda excluida!", HttpStatus.OK);
		}
		
		if(vendaCompraLojaVirtual.getStatusVendaLojaVirtual().ABANDONADO_CARRINHO_COMPRA != StatusVendaLojaVirtual.ABANDONADO_CARRINHO_COMPRA) {
			return new ResponseEntity<String>("Venda não concluida!", HttpStatus.OK);
		}
		
		if(vendaCompraLojaVirtual.getStatusVendaLojaVirtual().CANCELADO != StatusVendaLojaVirtual.CANCELADO) {
			return new ResponseEntity<String>("Venda Cancelada!", HttpStatus.OK);
		}

		/*Lista as cobranca de cartao e caso não efetuadas são deletadas*/
		
		List<CobrancaPagamento> cobrancaPagamentos = pagamentoAsaasRepository.cobrancaDaVendaCompra(idVendaCampo);
		
		for(CobrancaPagamento cobrancaPagamento : cobrancaPagamentos) {
			pagamentoAsaasRepository.deleteById(cobrancaPagamento.getId());
			pagamentoAsaasRepository.flush();
		}
		
		/*Inicio -- Gerando cobranca por cartão*/
		CobrancaPagamentoEnvioDTO cartao = new CobrancaPagamentoEnvioDTO();
		
		cartao.setPayerCpfCnpj(cpfLimpo);
		cartao.setPayerName(holderName);
		cartao.setPayerPhone(vendaCompraLojaVirtual.getPessoaFisica().getTelefone());
		
		CobrancaApiAsaasCartaoDTO cobrancaApiAsaasCartaoDTO = new CobrancaApiAsaasCartaoDTO();
		cobrancaApiAsaasCartaoDTO.setCustomer(pagamentoSaasApiService.buscaPessoaFisicaApiAsaas(cartao));
		cobrancaApiAsaasCartaoDTO.setBillingType(ApiTokenIntegracao.CREDIT_CARD);
		cobrancaApiAsaasCartaoDTO.setDescription("Venda realizada cliente por cartão de crédito: ID Venda -> " + idVendaCampo);
		
		
		if(qtdparcela == 1) {
			cobrancaApiAsaasCartaoDTO.setInstallmentValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
			
		}else {
			BigDecimal valolParcela = vendaCompraLojaVirtual.getValorTotal()
					.divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN)
					.setScale(2, RoundingMode.DOWN);
			cobrancaApiAsaasCartaoDTO.setInstallmentValue(valolParcela.floatValue());
		}
		
		cobrancaApiAsaasCartaoDTO.setInstallmentCount(qtdparcela);
		cobrancaApiAsaasCartaoDTO.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
		
		/*Dados do cartao credito*/
		CobrancaApiAsaasCartaoCreditoDTO creditCard = new CobrancaApiAsaasCartaoCreditoDTO();
		creditCard.setCcv(securityCode);
		creditCard.setExpiryMonth(expirationMonth);
		creditCard.setExpiryYear(expirationYear);
		creditCard.setHolderName(holderName);
		creditCard.setNumber(cardNumber);
		creditCard.setNumber(numero);
		
		cobrancaApiAsaasCartaoDTO.setCreditCard(creditCard);
		
		PessoaFisica pessoaFisica = vendaCompraLojaVirtual.getPessoaFisica();
		CartaoCreditoAsaasHolderInfoDTO creditCardHolderInfo = new CartaoCreditoAsaasHolderInfoDTO();
		creditCardHolderInfo.setName(pessoaFisica.getNome());
		creditCardHolderInfo.setEmail(pessoaFisica.getEmail());
		creditCardHolderInfo.setCpfCnpj(pessoaFisica.getCpf());
		creditCardHolderInfo.setPostalCode(cep);
		creditCardHolderInfo.setAddresNumber(numero);
		creditCardHolderInfo.setAddresComplement(null);
		creditCardHolderInfo.setPhone(pessoaFisica.getTelefone());
		creditCardHolderInfo.setMobilePhone(pessoaFisica.getTelefone());
		
		cobrancaApiAsaasCartaoDTO.setCreditCardHolderInfo(creditCardHolderInfo);
		
		String json = new ObjectMapper().writeValueAsString(cobrancaApiAsaasCartaoDTO);
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS)
				.hostIgnoringClient();
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_API_ASSAS + "v3/payments");
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.post(ClientResponse.class, json);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		int status = clientResponse.getStatus();
		clientResponse.close();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		if(status != 200) {
			
			/*Em Caso de Erro*/
			for(CobrancaPagamento cobrancaPagamento : cobrancaPagamentos) {
				
				if(pagamentoAsaasRepository.existsById(cobrancaPagamento.getId())) {
				pagamentoAsaasRepository.deleteById(cobrancaPagamento.getId());
				pagamentoAsaasRepository.flush();
				}
			}
		
			
			ErroResponseApiAsaas apiAssas = objectMapper.readValue(stringRetorno, new TypeReference<>() {});
			
			return new ResponseEntity<String>("Erro ao realizar cobrança: " + apiAssas.listaErros(), HttpStatus.OK);
		}
		
		CobrancaGeradaCartaoCreditoSaasDataDTO cartaoCredito = objectMapper.readValue(stringRetorno, new TypeReference<CobrancaGeradaCartaoCreditoSaasDataDTO>() {});
		
		int recorrencia = 1;
		List<CobrancaPagamento> cobrancaPagamentos2 = new ArrayList<CobrancaPagamento>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dataCobranca = dateFormat.parse(cobrancaApiAsaasCartaoDTO.getDueDate());
		Calendar calendar = Calendar.getInstance();
		
		for(int p = 1 ; p <= qtdparcela; p++) {
			
			CobrancaPagamento cobrancaPagamento = new CobrancaPagamento();
			
			cobrancaPagamento.setChargeICartao(cartaoCredito.getId());
			cobrancaPagamento.setCheckoutUrl(cartaoCredito.getInvoiceUrl());
			cobrancaPagamento.setCode(cartaoCredito.getId());
			cobrancaPagamento.setDataVencimento(dateFormat.format(dataCobranca));
			
			calendar.setTime(dataCobranca);
			calendar.add(Calendar.MONTH, 1);
			dataCobranca = calendar.getTime();
			
			cobrancaPagamento.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			cobrancaPagamento.setIdChrBoleto(cartaoCredito.getId());
			cobrancaPagamento.setIdPix(cartaoCredito.getId());
			cobrancaPagamento.setInstallmentLink(cartaoCredito.getInvoiceUrl());
			cobrancaPagamento.setQuitado(false);
			cobrancaPagamento.setRecorrente(recorrencia);
			cobrancaPagamento.setValor(BigDecimal.valueOf(cobrancaApiAsaasCartaoDTO.getInstallmentValue()));
			cobrancaPagamento.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
			
			recorrencia ++;
			cobrancaPagamentos2.add(cobrancaPagamento);
		}
		
		pagamentoAsaasRepository.saveAllAndFlush(cobrancaPagamentos2);
		
		if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
			
			for(CobrancaPagamento cobrancaPagamento : cobrancaPagamentos2) {
				pagamentoAsaasRepository.quitarBoletoById(cobrancaPagamento.getId());
			}
			
			vendaCompraLojaVirtualRepository.updateFinalizaVenda(vendaCompraLojaVirtual.getId());
			
			return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Pagamento não finalizado!: Status" + cartaoCredito.getStatus(), HttpStatus.OK);
		}
	}

	
	
}
