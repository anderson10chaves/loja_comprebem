package adcsistemas.loja_comprebem.service.TokenAssasService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import adcsistemas.loja_comprebem.ApiTokenIntegracao;
import adcsistemas.loja_comprebem.model.AccessTokenAssasClient;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaApiAsaasDTO;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoEnvioDTO;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoSaasAPIDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CobrancaGeradaSaasApiDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.CobrancaGeradaSaasDataDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.ConteudoPagamentoSaasAPIDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.DadosPagamentoSaasAPIDTO;
import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.QrCodePixAsaasDTO;
import adcsistemas.loja_comprebem.model.cobranca_pagamento.CobrancaPagamento;
import adcsistemas.loja_comprebem.model.dto.AsaasCliente.ClienteAsaasApiPagamentoDTO;
import adcsistemas.loja_comprebem.repository.AccessTokenAssasRepository;
import adcsistemas.loja_comprebem.repository.PagamentoAsaasRepository;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;
import adcsistemas.loja_comprebem.service.SSLClient.HostIgnoringClient;
import adcsistemas.loja_comprebem.utils.ValidaCpf;

@Service
public class PagamentoSaasApiService  {

	
	@Autowired
	private AccessTokenAsaasService accessTokenAsaasService;
	
	@Autowired
	private AccessTokenAssasRepository accessTokenAssasRepository;
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private PagamentoAsaasRepository pagamentoAssasRepository;
	
	
	/**
	 * Cria usuário Assas e retorna ID do cliente
	 * @return
	 * @throws Exception 
	 */
	public String buscaPessoaFisicaApiAsaas(CobrancaPagamentoEnvioDTO dados) throws Exception {
		
		/*Id do cliente para ligar com a cobranca*/
		String customer_id = "";
		
		/*-------------INICIO  - criando ou consultando o cliente*/
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS)
				.hostIgnoringClient();
		
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_API_ASSAS
				+ "v3/customers?email"+dados.getEmail());
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.get(ClientResponse.class);
		
		LinkedHashMap<String, Object> parser = new JSONParser(clientResponse.getEntity(String.class)).parseObject();
		clientResponse.close();
		Integer total = Integer.parseInt(parser.get("totalCount").toString());
		
		if(total <= 0 ) { /*Criar o cliente*/
			
			ClienteAsaasApiPagamentoDTO clienteAssasApiPagamentoDTO = new ClienteAsaasApiPagamentoDTO();
			
			if(!ValidaCpf.isCPF(dados.getPayerCpfCnpj())) {
				clienteAssasApiPagamentoDTO.setCpfCnpj("60051803046");
			}else {
				clienteAssasApiPagamentoDTO.setCpfCnpj(dados.getPayerCpfCnpj());
			}
			
			clienteAssasApiPagamentoDTO.setEmail(dados.getEmail());
			clienteAssasApiPagamentoDTO.setName(dados.getPayerName());
			clienteAssasApiPagamentoDTO.setPhone(dados.getPayerPhone());
			
			Client client2 = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS)
					.hostIgnoringClient();
			
			WebResource webResource2 = client2.resource(ApiTokenIntegracao.URL_API_ASSAS
					+ "v3/customers");
			
			ClientResponse clientResponse2 = webResource2
					.accept("application/json;charset=UTF-8")
					.header("content-type", "application/json")
					.header("access_token", ApiTokenIntegracao.API_KEY)
					.post(ClientResponse.class, new ObjectMapper().writeValueAsString(clienteAssasApiPagamentoDTO));
			
			LinkedHashMap<String, Object> parser2 = new JSONParser(clientResponse2.getEntity(String.class)).parseObject();
			clientResponse2.close();
			customer_id = parser2.get("id").toString(); 
			
		}else { /*Cliente já existe no cadastro*/
			List<Object> data = (List<Object>)parser.get("data");
			customer_id = new Gson().toJsonTree(data.get(0)).getAsJsonObject().get("id").toString().replaceAll("\"", "");
		}
		
		return customer_id;
		
		
	}
	
	/**
	 * Cria Chave da API ASSAS
	 * @return
	 * @throws Exception
	 */
	public String criarChavePixAssas() throws Exception {
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS)
				.hostIgnoringClient();
		
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_API_ASSAS + "v3/pix/addressKeys");
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.post(ClientResponse.class,"{\"type\":\"EVP\"}");
		
		String stringRetorno = clientResponse.getEntity(String.class);
		clientResponse.close();
		return stringRetorno;
	}
	
	
	// Gerando pagamento
	
	public String gerarPagamento(CobrancaPagamento criarPagamento_Online) throws Exception {
		
		Object vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.getClass();
		
		CobrancaPagamento criarPagamento_Onlines = new CobrancaPagamento();
		
		
		
		
		AccessTokenAssasClient accessTokenAssasClient = this.obterToken();
		if (accessTokenAssasClient != null) {
			
			Client client = new HostIgnoringClient("https://api.mercadopago.com/oauth/token/")
					.hostIgnoringClient();
			
			WebResource webResource = client.resource("https://api.mercadopago.com/checkout/preferences?access_token=TEST-3443042677739303-030804-81113c9c04b7d96cbb8f84dfd14b9e3e-142862696");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(accessTokenAssasClient);
			
			ClientResponse clientResponse = webResource
					.accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", "Basic " + accessTokenAssasClient.getAccess_token())
					.post(ClientResponse.class);
			
			String stringRetorno = clientResponse.getEntity(String.class);
			
			if (clientResponse.getStatus() == 200) {
				
				clientResponse.close();
				objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); /*Converte os arrays de muitos para um*/
				
				CobrancaPagamento criarPagamento_Online1 = objectMapper.readValue(stringRetorno,
						new TypeReference<CobrancaPagamento>() {});
				
				if(criarPagamento_Online1  != null) {
					
					
					
				}
				
				pagamentoAssasRepository.saveAndFlush(criarPagamento_Online);
				
				return criarPagamento_Online.toString();
				
			}else {
				return stringRetorno;
			}
			
			
		}else {
			return "Não foi possivel realizar seu pedido de pagamento ";
		}
		
		
	}
	/**
	 * Gerador de Pagamento para API Asaas
	 * @param cobrancaPagamentoEnvioDTO
	 * @return
	 * @throws Exception
	 */
	public String gerarBoletosApiAsaas(CobrancaPagamentoEnvioDTO cobrancaPagamentoEnvioDTO) throws Exception {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(cobrancaPagamentoEnvioDTO.getIdVenda()).get();
		
		CobrancaApiAsaasDTO cobrancaApiAsaasDTO = new CobrancaApiAsaasDTO();
		cobrancaApiAsaasDTO.setCustomer(this.buscaPessoaFisicaApiAsaas(cobrancaPagamentoEnvioDTO));
		
		/*Gera cobranca Boleto ou PIX ou UNDEFINED*/
		cobrancaApiAsaasDTO.setBillingType("UNDEFINED");
		cobrancaApiAsaasDTO.setDescription("Pix ou Boleto, gerado para cobranca, cod: " + compraLojaVirtual.getId());
		cobrancaApiAsaasDTO.setInstallmentValue(compraLojaVirtual.getValorTotal().floatValue());
		cobrancaApiAsaasDTO.setInstallmentCount(1);
		
		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		cobrancaApiAsaasDTO.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(dataVencimento.getTime()));
		
		cobrancaApiAsaasDTO.getInterest().setValue(1F);
		cobrancaApiAsaasDTO.getFine().setValue(1F);
		
		String json = new ObjectMapper().writeValueAsString(cobrancaApiAsaasDTO);
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS).hostIgnoringClient();
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_API_ASSAS + "v3/payments");
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.post(ClientResponse.class, json);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		clientResponse.close();
		
		/*Buscando parcelas geradas*/
		
		LinkedHashMap<String, Object> parser = new JSONParser(stringRetorno).parseObject();
		String installment = parser.get("installment").toString();
		Client client2 = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS).hostIgnoringClient();
		WebResource webResource2 = client.resource(ApiTokenIntegracao.URL_API_ASSAS + "v3/payments?installment=" + installment);
		
		ClientResponse clientResponse2 = webResource2
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.get(ClientResponse.class);
		
		String retornoCobrancas = clientResponse2.getEntity(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		CobrancaGeradaSaasApiDTO listaCobranca = new ObjectMapper()
				.readValue(retornoCobrancas, new TypeReference<CobrancaGeradaSaasApiDTO>() {});
		
		List<CobrancaPagamento> cobrancaPagamentos = new ArrayList<CobrancaPagamento>();
		int recorrencia = 1;
		for(CobrancaGeradaSaasDataDTO data : listaCobranca.getData()) {
			
			CobrancaPagamento cobrancaPagamento = new CobrancaPagamento();
			
			cobrancaPagamento.setEmpresa(compraLojaVirtual.getEmpresa());
			cobrancaPagamento.setVendaCompraLojaVirtual(compraLojaVirtual);
			cobrancaPagamento.setCode(data.getId());
			cobrancaPagamento.setLink(data.getInvoiceUrl());
			cobrancaPagamento.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDueDate())));
			cobrancaPagamento.setCheckoutUrl(data.getInvoiceUrl());
			cobrancaPagamento.setValor(new BigDecimal(data.getValue()));
			cobrancaPagamento.setIdChrBoleto(data.getId());
			cobrancaPagamento.setInstallmentLink(data.getInvoiceUrl());
			cobrancaPagamento.setRecorrente(recorrencia);
			
			//cobrancaPagamento.setIdPix(c.getPix().getId());
			
			QrCodePixAsaasDTO codePixAsaasDTO = this.leiaQrCodeCodigoPix(data.getId());
			
			cobrancaPagamento.setPayloadInBase64(codePixAsaasDTO.getPayload());
			cobrancaPagamento.setImageInBase64(codePixAsaasDTO.getEncodeImage());
			
			cobrancaPagamentos.add(cobrancaPagamento);
			recorrencia ++;
		}
		
		pagamentoAssasRepository.saveAllAndFlush(cobrancaPagamentos);
		
		return cobrancaPagamentos.get(0).getCheckoutUrl();
	}
	
	/*
	 * Gera o QrCode pix API ASAAS
	 */
	public QrCodePixAsaasDTO leiaQrCodeCodigoPix(String idCobranca) throws Exception {
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_API_ASSAS).hostIgnoringClient();
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_API_ASSAS + "v3/payments/" +idCobranca +"/pixQrCode");
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("access_token", ApiTokenIntegracao.API_KEY)
				.get(ClientResponse.class);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		clientResponse.close();
		
		QrCodePixAsaasDTO codePixAssas = new QrCodePixAsaasDTO();
		
		LinkedHashMap<String, Object> parser = new JSONParser(stringRetorno).parseObject();
		codePixAssas.setEncodeImage(parser.get("encodedImage").toString());
		codePixAssas.setPayload(parser.get("payload").toString());
		
		return codePixAssas;
	}
	
	
	/**
	 * Gera a cobranca Para para API JUNO ou IUGI
	 * @throws Exception 
	 */
	public String gerarBoletosApi(CobrancaPagamentoEnvioDTO cobrancaPagamentoEnvioDTO) throws Exception {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(cobrancaPagamentoEnvioDTO.getIdVenda()).get();
		
		CobrancaPagamentoSaasAPIDTO cobrancaPagamentoSaasAPIDTO = new CobrancaPagamentoSaasAPIDTO();
		
		cobrancaPagamentoSaasAPIDTO.getCharge().setPixKey(ApiTokenIntegracao.BOLETO_PIX);
		cobrancaPagamentoSaasAPIDTO.getCharge().setDescription(cobrancaPagamentoEnvioDTO.getDescription());
		cobrancaPagamentoSaasAPIDTO.getCharge().setAmount(Float.valueOf(cobrancaPagamentoEnvioDTO.getTotalAmount()));
		cobrancaPagamentoSaasAPIDTO.getCharge().setInstallments(Integer.parseInt(cobrancaPagamentoEnvioDTO.getInstallments()));
		
		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		cobrancaPagamentoSaasAPIDTO.getCharge().setDueDate(dataFormat.format(dataVencimento.getTime()));
		
		cobrancaPagamentoSaasAPIDTO.getCharge().setFine(BigDecimal.valueOf(5.00));
		cobrancaPagamentoSaasAPIDTO.getCharge().setInterest(BigDecimal.valueOf(10.00));
		cobrancaPagamentoSaasAPIDTO.getCharge().setMaxOverdueDays(10);
		cobrancaPagamentoSaasAPIDTO.getCharge().getPaymentTypes().add("BOLETO_PIX");
		
		cobrancaPagamentoSaasAPIDTO.getBilling().setName(cobrancaPagamentoEnvioDTO.getPayerName());
		cobrancaPagamentoSaasAPIDTO.getBilling().setDocument(cobrancaPagamentoEnvioDTO.getPayerCpfCnpj());
		cobrancaPagamentoSaasAPIDTO.getBilling().setEmail(cobrancaPagamentoEnvioDTO.getEmail());
		cobrancaPagamentoSaasAPIDTO.getBilling().setPhone(cobrancaPagamentoEnvioDTO.getPayerPhone());
		
		AccessTokenAssasClient  accessTokenAsaaAssasClient = this.obterToken();
		if(accessTokenAsaaAssasClient != null) {
			
			Client client = new HostIgnoringClient("https://api.juno.com.br").hostIgnoringClient();
			WebResource webResource = client.resource("https://api.juno.com.br/charges");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(cobrancaPagamentoEnvioDTO);
			
			ClientResponse clientResponse = webResource
					.accept("application/json;charset=UTF-8")
					.header("content-type", "application/json;charset=UTF-8")
					.header("X-API-Version", 2)
					.header("X-Resource-Token", ApiTokenIntegracao.API_KEY_SANDBOX)
					.header("Authorization", "Bearer" + accessTokenAsaaAssasClient.getAccess_token())
					.header("X-Idempotency-Key", "Loja-comprebem")
					.post(ClientResponse.class, "{\"type\": \"RANDON_KEY\"}");
			
			String stringRetorno = clientResponse.getEntity(String.class);
			
			if (clientResponse.getStatus() == 200) {
				
				clientResponse.close();
				objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); /*Converte relacionamento de Josn para um só*/
				
				DadosPagamentoSaasAPIDTO jsonRetornoObj = objectMapper.readValue(stringRetorno,
						new TypeReference<DadosPagamentoSaasAPIDTO>() {});
				
				int recorrencia = 1;
				
				List<CobrancaPagamento> cobrancaPagamentos = new ArrayList<CobrancaPagamento>();
				
				for(ConteudoPagamentoSaasAPIDTO c : jsonRetornoObj.get_embedded().getCharges()) {
					
					CobrancaPagamento cobrancaPagamento = new CobrancaPagamento();
					
					cobrancaPagamento.setEmpresa(compraLojaVirtual.getEmpresa());
					cobrancaPagamento.setVendaCompraLojaVirtual(compraLojaVirtual);
					cobrancaPagamento.setCode(c.getCode());
					cobrancaPagamento.setLink(c.getLink());
					cobrancaPagamento.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
					cobrancaPagamento.setCheckoutUrl(c.getCheckoutUrl());
					cobrancaPagamento.setValor(new BigDecimal(c.getAmount()));
					cobrancaPagamento.setIdChrBoleto(c.getId());
					cobrancaPagamento.setInstallmentLink(c.getInstallmentLink());
					cobrancaPagamento.setIdPix(c.getPix().getId());
					cobrancaPagamento.setPayloadInBase64(c.getPix().getPayloadInBase64());
					cobrancaPagamento.setImageInBase64(c.getPix().getImageInBase64());
					cobrancaPagamento.setRecorrente(recorrencia);
					recorrencia ++;
					
				}
				
				pagamentoAssasRepository.saveAllAndFlush(cobrancaPagamentos);
				
				return cobrancaPagamentos.get(0).getLink();
				
			} else {
				return stringRetorno;
			}
			
		}else  {
			return "Não Existe chave de acesso para API";
		}
		
	}
	
	/**
	 * Cancelamento de Boleto e Pix
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String cancelaBoletoPix(String code) throws Exception {
		
		AccessTokenAssasClient accessTokenAsaaAssasClient = new AccessTokenAssasClient();
		
		Client client = new HostIgnoringClient("https://api.juno.com.br").hostIgnoringClient();
		WebResource webResource = client.resource("https://api.juno.com.br/charges/"+code+"/cancelation");
		
		ClientResponse clientReponse = webResource.accept(MediaType.APPLICATION_JSON)
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json;charset=UTF-8")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.URL_API_ASSAS)
				.header("Authorization", "Bearer" + accessTokenAsaaAssasClient.getAccess_token())
				.header("X-Idempotency-Key", "Loja-comprebem")
				.post(ClientResponse.class, "{\"type\": \"RANDON_KEY\"}");
		
		if(clientReponse.getStatus() == 204) {
			return "Cancelado com sucesso!";
		}
		
		return clientReponse.getEntity(String.class);
	}
	
	/**
	 * Gera Chave pix para API JUNO ou IUGI
	 * @return
	 * @throws Exception
	 */
	public String geraChaveBoletoPix() throws Exception {
		
		AccessTokenAssasClient accessTokenAssasClient = this.obterToken();
		
		Client client = new HostIgnoringClient("https://api.juno.com.br").hostIgnoringClient();
		WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("content-type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.API_KEY_SANDBOX)
				.header("Authorization", "Bearer" + accessTokenAssasClient.getAccess_token())
				.header("X-Idempotency-Key", "Loja-comprebem")
				.post(ClientResponse.class, "{\"type\": \"RANDON_KEY\"}");
		
		return clientResponse.getEntity(String.class);
		
	}
	
	public AccessTokenAssasClient obterToken() throws Exception {
		
		AccessTokenAssasClient accessTokenAssasClient = accessTokenAsaasService.obterTokenAssas();
		
		if(accessTokenAssasClient == null || accessTokenAssasClient != null && accessTokenAssasClient.expirado()) {
			
			String client_id = "3443042677739303";
			String client_secret = "snvvGqHqGP2FOwQowdE4E4dBq6nOG5bo";
			String access_token = "TEST-6a95ac3c-e449-4fff-911e-241d3ea0c089";
			String redirect_uri = "https://webhook.site/0538cdba-d2be-4fc2-9f70-57febb971dfd";
			Boolean test_token = true;
			String granttype = "client_credentials";
			
			Client client = new HostIgnoringClient("https://api.mercadopago.com/oauth/token/")
					.hostIgnoringClient();
			
			WebResource webResource = client.resource("https://api.mercadopago.com/oauth/token?client_secret=snvvGqHqGP2FOwQowdE4E4dBq6nOG5bo&access_token=TEST-3443042677739303-030804-81113c9c04b7d96cbb8f84dfd14b9e3e-142862696&redirect_uri=https://www.mercadopago.com.br/developers/panel/app/3443042677739303&test_token=true&grant_type=client_credentials&client_id=3443042677739303");
			
			String basicChave = client_id + ":" + client_secret;
			String token_autenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());
			
			ClientResponse clientResponse = webResource
					.accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", "Basic " + token_autenticacao)
					.post(ClientResponse.class);
			
			if(clientResponse.getStatus() == 200) {
				accessTokenAssasRepository.deleteAll();
				accessTokenAssasRepository.flush();
				
				AccessTokenAssasClient accessTokenAssasClient2 = clientResponse.getEntity(AccessTokenAssasClient.class);
				accessTokenAssasClient2.setAccess_token(token_autenticacao);
				
				accessTokenAssasClient2 = accessTokenAssasRepository.saveAndFlush(accessTokenAssasClient2);
				
				return accessTokenAssasClient2;
			}else {
				return null;
			} 
			
		}else {
			return null;
		}
	}	

}
