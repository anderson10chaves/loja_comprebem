package adcsistemas.loja_comprebem.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import adcsistemas.loja_comprebem.ApiTokenIntegracao;
import adcsistemas.loja_comprebem.enums.StatusContaReceber;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.ContaReceber;
import adcsistemas.loja_comprebem.model.Empresa;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.ItemVendaLoja;
import adcsistemas.loja_comprebem.model.Pessoa;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.StatusRastreio;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.dto.ItemVendaDTO;
import adcsistemas.loja_comprebem.model.dto.VendaCompraLojaVirtualDTO;
import adcsistemas.loja_comprebem.model.dto.etiiqueta.ApiToEnvioEtiquetaDTO;
import adcsistemas.loja_comprebem.model.dto.etiiqueta.EnvioEtiquetaDTO;
import adcsistemas.loja_comprebem.model.dto.etiiqueta.ProductsEnvioEtiquetaDTO;
import adcsistemas.loja_comprebem.model.dto.etiiqueta.TagsEnvioEtiquetaDTO;
import adcsistemas.loja_comprebem.model.dto.etiiqueta.VolumesEnvioEtiquetaDTO;
import adcsistemas.loja_comprebem.repository.ContaReceberRepository;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.NotaFiscalVendaRepository;
import adcsistemas.loja_comprebem.repository.StatusRastreioRepository;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;
import adcsistemas.loja_comprebem.service.SendEmailService;
import adcsistemas.loja_comprebem.service.VendaService;
import adcsistemas.loja_comprebem.transportadora.dto.ConsultaApiFreteDTO;
import adcsistemas.loja_comprebem.transportadora.dto.TransportadoraDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class VendaCompraLojaVirtualController {

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaController pessoaController;

	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ContaReceberRepository contaReceberRepository;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@ResponseBody
	@PostMapping(value = "/salvarVendas")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendas(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaComprebem, UnsupportedEncodingException, MessagingException {

		vendaCompraLojaVirtual.getPessoaFisica().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoaFisica()).getBody();
		vendaCompraLojaVirtual.setPessoaFisica(pessoaFisica);

		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}
		/* Salva primeiro a venda e todos dados do cliente */
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

		

		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i)
					.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}

		/* Associa a venda realizada com a nota de venda */
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		/* Persiste novamente a nota de venda e fica amarrada a venda */
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		/* Classe VendaCompralojaDTO caso necessite para retorno de infromações */
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoaFisica());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getEmpresa());
		
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

		vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());

		vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

		vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtual.getDiaEntrega());
		vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtual.getDataVenda());
		vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtual.getDataEntrega());

		for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setId(item.getId());
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());

			vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		ContaReceber contaReceber = new ContaReceber();
		contaReceber.setDescricao("Venda da Loja nº: " + vendaCompraLojaVirtual.getId());
		contaReceber.setDtPagamento(Calendar.getInstance().getTime());
		contaReceber.setDtVencimento(Calendar.getInstance().getTime());
		contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		contaReceber.setPessoaFisica(vendaCompraLojaVirtual.getPessoaFisica());
		contaReceber.setStatus(StatusContaReceber.QUITADO);
		contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		
		contaReceberRepository.saveAndFlush(contaReceber);
		
		/*Email para comprador*/
		StringBuilder msgVendaEmail = new StringBuilder();
		msgVendaEmail.append("Caro(a) Cliente, ").append(pessoaFisica.getNome()).append("</br>");
		msgVendaEmail.append("Você realizou a compra nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
		msgVendaEmail.append("Na Loja ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
		/*Assunto, msg, destino*/
		sendEmailService.enviarEmailHtml("Compra realizada com sucesso", msgVendaEmail.toString(), pessoaFisica.getEmail());
		
		/*Email para vendedor*/
		msgVendaEmail = new StringBuilder();
		msgVendaEmail.append("Sua Loja realizou uma venda, nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
		sendEmailService.enviarEmailHtml("Venda finalizada!", msgVendaEmail.toString(), vendaCompraLojaVirtual.getEmpresa().getEmail());
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> pesquisaVendaId(@PathVariable("id") Long idVenda) {

		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExcluido(idVenda);

		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new VendaCompraLojaVirtual();
		}

		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoaFisica());

		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getEmpresa());
		
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

		vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());

		vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtual.getFormaPagamento());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

		vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtual.getDiaEntrega());
		vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtual.getDataVenda());
		vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtual.getDataEntrega());

		for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setId(item.getId());
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());

			vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);

	}
	
	@ResponseBody
	@GetMapping(value = "/vendaPorCliente/{idCliente}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> vendaPorCliente(@PathVariable("idCliente") Long idCliente) {

		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.vendaPorCliente(idCliente);

		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo : vendaCompraLojaVirtual) {

			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtualSalvo.getId());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getPessoaFisica());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getEmpresa());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtualSalvo.getValorTotal());

			vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtualSalvo.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtualSalvo.getEnderecoCobranca());

			vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtualSalvo.getFormaPagamento());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtualSalvo.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtualSalvo.getValorFrete());

			vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtualSalvo.getDiaEntrega());
			vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtualSalvo.getDataVenda());
			vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtualSalvo.getDataEntrega());

			for (ItemVendaLoja item : vendaCompraLojaVirtualSalvo.getItemVendaLojas()) {
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOList, HttpStatus.OK);

	}


	@ResponseBody
	@GetMapping(value = "/pesquisaVendaProdutoAtivoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> pesquisaVendaProdutoAtivoId(
			@PathVariable("id") Long idProduto) {

		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.vendaPorProdutoInativo(idProduto);

		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo : vendaCompraLojaVirtual) {

			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtualSalvo.getId());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getPessoaFisica());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getEmpresa());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtualSalvo.getValorTotal());

			vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtualSalvo.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtualSalvo.getEnderecoCobranca());

			vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtualSalvo.getFormaPagamento());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtualSalvo.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtualSalvo.getValorFrete());

			vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtualSalvo.getDiaEntrega());
			vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtualSalvo.getDataVenda());
			vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtualSalvo.getDataEntrega());

			for (ItemVendaLoja item : vendaCompraLojaVirtualSalvo.getItemVendaLojas()) {
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOList, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "/pesquisaVendaProdutoInativoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> pesquisaVendaProdutoInativoId(
			@PathVariable("id") Long idProduto) {

		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.vendaPorProdutoAtivo(idProduto);

		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo : vendaCompraLojaVirtual) {

			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtualSalvo.getId());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getPessoaFisica());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getEmpresa());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtualSalvo.getValorTotal());

			vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtualSalvo.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtualSalvo.getEnderecoCobranca());

			vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtualSalvo.getFormaPagamento());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtualSalvo.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtualSalvo.getValorFrete());

			vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtualSalvo.getDiaEntrega());
			vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtualSalvo.getDataVenda());
			vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtualSalvo.getDataEntrega());

			for (ItemVendaLoja item : vendaCompraLojaVirtualSalvo.getItemVendaLojas()) {
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOList, HttpStatus.OK);

	}
	
	
	@ResponseBody
	@GetMapping(value = "/pesquisaVendaDinamicaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> pesquisaVendaDinamicaData(
			@PathVariable("data1") String data1,
			@PathVariable("data2") String data2) throws ParseException{

		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;
		
		vendaCompraLojaVirtual = vendaService.pesquisaVendaDinamicaData(data1, data2);

		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo : vendaCompraLojaVirtual) {

			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtualSalvo.getId());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getPessoaFisica());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getEmpresa());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtualSalvo.getValorTotal());

			vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtualSalvo.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtualSalvo.getEnderecoCobranca());

			vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtualSalvo.getFormaPagamento());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtualSalvo.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtualSalvo.getValorFrete());

			vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtualSalvo.getDiaEntrega());
			vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtualSalvo.getDataVenda());
			vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtualSalvo.getDataEntrega());

			for (ItemVendaLoja item : vendaCompraLojaVirtualSalvo.getItemVendaLojas()) {
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOList, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "/pesquisaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> pesquisaVendaDinamica(@PathVariable("valor") String valor,
			@PathVariable("tipoconsulta") String tipoconsulta) {

		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = null;

		if (tipoconsulta.equalsIgnoreCase("ID_PROD")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor));
		} else if (tipoconsulta.equalsIgnoreCase("NOME_PROD")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
					.pesquisaVendaNomeProduto(valor.toUpperCase().trim());
		} else if (tipoconsulta.equalsIgnoreCase("NOME_CLIENTE")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
					.pesquisaVendaNomeCliente(valor.toUpperCase().trim());
		} else if (tipoconsulta.equalsIgnoreCase("END_COBRANCA")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
					.pesquisaVendaEndCobranca(valor.toUpperCase().trim());
		} else if (tipoconsulta.equalsIgnoreCase("END_ENTREGA")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
					.pesquisaVendaEndEntrega(valor.toUpperCase().trim());
		} else if (tipoconsulta.equalsIgnoreCase("CEP")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.pesquisaVendaCep(valor.toUpperCase().trim());
		}  else if (tipoconsulta.equalsIgnoreCase("CPF")) {
			vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.pesquisaVendaCpf(valor.toUpperCase().trim());
		}
		if (vendaCompraLojaVirtual == null) {
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo : vendaCompraLojaVirtual) {

			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtualSalvo.getId());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getPessoaFisica());

			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtualSalvo.getEmpresa());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtualSalvo.getValorTotal());

			vendaCompraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtualSalvo.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtualSalvo.getEnderecoCobranca());

			vendaCompraLojaVirtualDTO.setFormaPagamento(vendaCompraLojaVirtualSalvo.getFormaPagamento());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtualSalvo.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtualSalvo.getValorFrete());

			vendaCompraLojaVirtualDTO.setDiaEntrega(vendaCompraLojaVirtualSalvo.getDiaEntrega());
			vendaCompraLojaVirtualDTO.setDataVenda(vendaCompraLojaVirtualSalvo.getDataVenda());
			vendaCompraLojaVirtualDTO.setDataEntrega(vendaCompraLojaVirtualSalvo.getDataEntrega());

			for (ItemVendaLoja item : vendaCompraLojaVirtualSalvo.getItemVendaLojas()) {
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setId(item.getId());
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				vendaCompraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			vendaCompraLojaVirtualDTOList.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOList, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vendaService.deleteVendaTotalBanco(idVenda);

		return new ResponseEntity<String>("Atenção esta exclusão não poderá ser recuperada, se deseja excluir!",
				HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteVendaLogicaBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaLogicaBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vendaService.deleteVendaLogicaBanco(idVenda);

		return new ResponseEntity<String>("Venda Inativada com sucesso!", HttpStatus.OK);
	}

	@ResponseBody
	@PutMapping(value = "/ativaVendaRegistroBanco/{idVenda}")
	public ResponseEntity<String> ativaVendaRegistroBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vendaService.ativaVendaRegistroBanco(idVenda);

		return new ResponseEntity<String>("Venda Ativada com sucesso!", HttpStatus.OK);
	}
	
	
	
	/**
	 * Lista todas Etiquetas Pendentes de envio
	 * @param pendente
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaPendente/{pendente}")
	public ResponseEntity<String> pendenteEtiqueta(@PathVariable String pendente) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+pendente+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=pending")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	
	/**
	 * Etiqueta Realizada
	 * @param realizada
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaRealizada/{realizada}")
	public ResponseEntity<String> realizadaEtiqueta(@PathVariable String realizada) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+realizada+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=Released")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	
	/**
	 * Etiquetas Postada ou enviado
	 * @param enviado
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaEnviada/{enviado}")
	public ResponseEntity<String> enviadoEtiqueta(@PathVariable String enviado) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+enviado+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=Posted")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	
	/**
	 * Etiqueta em transporte a caminho
	 * @param transporte
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaTransporte/{transporte}")
	public ResponseEntity<String> transporteEtiqueta(@PathVariable String transporte) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status=transporte}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=Delivered")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	/**
	 * Etiqueta que não estão em transporte
	 * @param naotransporte
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaNaoTransporte/{naotransporte}")
	public ResponseEntity<String> transporteNaoEtiqueta(@PathVariable String naotransporte) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+naotransporte+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=Not%20Delivered")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	
	/**
	 * Lista todas as Etiquetas Canceladas
	 * @param cancelado
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "statusEtiquetaCancelado/{cancelado}")
	public ResponseEntity<String> canceladoEtiqueta(@PathVariable String cancelado) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+cancelado+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/orders?status=cancelled")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	} 
	
	/**
	 * Pesquisa Agencia ou Transportadoras por estado
	 * @param estado
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "pesquisaAgenciaPorEstados/{estado}")
	public ResponseEntity<String> pesquisaEtiquetaPorEstado(@PathVariable String estado) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+estado+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/agencies?")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	} 
	
	/**
	 * Pesquisa Transportadora Por Cidades
	 * @param cidade
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@GetMapping(value = "pesquisaAgenciaPorCidade/{cidade}")
	public ResponseEntity<String> pesquisaEtiquetaPorCidade(@PathVariable String cidade) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{/orders?status="+cidade+"}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/agencies?")
		  .get()
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	} 
	
	/**
	 * Cancelamento de Etiquetas pelo Vendedor
	 * @param idEtiqueta
	 * @param descricao
	 * @param reason_id
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@PostMapping(value = "cancelaEtiqueta/{idEtiqueta}/{descricao}")
	public ResponseEntity<String> cancelaEtiqueta(@PathVariable String idEtiqueta, @PathVariable String descricao, @PathVariable String reason_id) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\"order\":{\"reason_id\":\"2\",\"id\":\""+idEtiqueta+"\",\"description\":\""+descricao+"\"}}");
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/cancel")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "  + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/imprimeCompraEtiquetaFrete/{idVenda}")
	public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws Exception, IOException {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);
		
		if(compraLojaVirtual == null) {
			return new ResponseEntity<String>("Venda não encontrada", HttpStatus.OK);
		}
		
		/*Carrega os endereco empresas*/
		List<Endereco> enderecos = enderecoRepository.enderecoEmp(compraLojaVirtual.getEmpresa().getId());
		compraLojaVirtual.getEmpresa().setEnderecos(enderecos);
		
		/**
		 * Envio de pedido de etiqueta frete, pedido pelo cliente e passado dados remetente
		 * 
		 */
		EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();
		
		envioEtiquetaDTO.setService(compraLojaVirtual.getServicoTransportadora());
		envioEtiquetaDTO.setAgency("30");
		envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
		envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
		envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
		envioEtiquetaDTO.getFrom().setCompany_document(compraLojaVirtual.getEmpresa().getCnpj());
		envioEtiquetaDTO.getFrom().setState_register(compraLojaVirtual.getEmpresa().getInscEstadual());
		envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getLogradouro());
		envioEtiquetaDTO.getFrom().setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
		envioEtiquetaDTO.getFrom().setNumber(compraLojaVirtual.getEmpresa().getEmpresa().getEnderecos().get(0).getNumero());
		envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
		envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
		envioEtiquetaDTO.getFrom().setCountry_id(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getPais());
		envioEtiquetaDTO.getFrom().setPostal_code(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
		envioEtiquetaDTO.getFrom().setNote("Não Há anotações");
		
		
		/**
		 * Envio de pedido de etiqueta frete, pedido pelo cliente e passado dados destinatário
		 * 
		 */
		envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoaFisica().getNome());
		envioEtiquetaDTO.getTo().setPhone(compraLojaVirtual.getPessoaFisica().getTelefone());
		envioEtiquetaDTO.getTo().setEmail(compraLojaVirtual.getPessoaFisica().getEmail());
		envioEtiquetaDTO.getTo().setDocument(compraLojaVirtual.getPessoaFisica().getCpf());
		envioEtiquetaDTO.getTo().setAddress(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getLogradouro());
		envioEtiquetaDTO.getTo().setComplement(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getComplemento());
		envioEtiquetaDTO.getTo().setNumber(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getNumero());
		envioEtiquetaDTO.getTo().setDistrict(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setCity(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getCidade());
		envioEtiquetaDTO.getTo().setState_abbr(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setCountry_id(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getPais());
		envioEtiquetaDTO.getTo().setPostal_code(compraLojaVirtual.getPessoaFisica().enderecoEntrega().getCep());
		envioEtiquetaDTO.getTo().setNote("Não Há anotações");
		
		/**
		 * Envio de pedido de etiqueta frete, pedido pelo cliente e passado os produtos
		 * 
		 */
		List<ProductsEnvioEtiquetaDTO> produtcs = new ArrayList<ProductsEnvioEtiquetaDTO>();
		
		for(ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {
			
			ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();
			
			dto.setName(itemVendaLoja.getProduto().getNome());
			dto.setQuantity(itemVendaLoja.getQuantidade().toString());
			dto.setUnitary_value("" + itemVendaLoja.getProduto().getValorVenda().doubleValue());
			
			produtcs.add(dto);
			
		}
		
		envioEtiquetaDTO.setProducts(produtcs);
		
		List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();
		
		for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {
			
			VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();
			
			dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
			dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
			dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
			dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());
			
			volumes.add(dto);
		}
		
		
		envioEtiquetaDTO.setVolumes(volumes);
		
		
		envioEtiquetaDTO.setVolumes(volumes);
		
		envioEtiquetaDTO.getOptions().setInsurance_value("" + compraLojaVirtual.getValorTotal().doubleValue());
		envioEtiquetaDTO.getOptions().setReceipt(false);
		envioEtiquetaDTO.getOptions().setOwn_hand(false);
		envioEtiquetaDTO.getOptions().setOwn_hand(false);
		envioEtiquetaDTO.getOptions().setNon_commercial(false);
		envioEtiquetaDTO.getOptions().getInvoice().setKey(compraLojaVirtual.getNotaFiscalVenda().getNumero());
		envioEtiquetaDTO.getOptions().setPlatform(compraLojaVirtual.getEmpresa().getNomeFantasia());
		
		TagsEnvioEtiquetaDTO tagEnvio = new TagsEnvioEtiquetaDTO();
		tagEnvio.setTag("Identificação do pedido na plataforma, exemplo" + compraLojaVirtual.getId());
		tagEnvio.setUrl(null);
		envioEtiquetaDTO.getOptions().getTags().add(tagEnvio);

		String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);
		
		/**
		 * Insere as etiquetas de frete
		 */
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/cart")
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "+ ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();
		
		String respostaJson = response.body().string();
		
		if (respostaJson.contains("error")) {
			throw new ExceptionLojaComprebem(respostaJson);
		}
		
		JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);
		
		Iterator<JsonNode> iterator = jsonNode.iterator();
		
		String idEtiqueta = "";
		while(iterator.hasNext()) {
			JsonNode node = iterator.next();
			if (node.get("id") != null) {
				idEtiqueta = node.get("id").asText();
			}else {
				idEtiqueta = node.asText();
			}
			break;
		}
		
		/*Salvando o codigo da etiqueta*/
		
		jdbcTemplate.execute("begin; update vd_cp_loja_virt set codigo_etiqueta = '"+idEtiqueta+"' where id = " + compraLojaVirtual.getId() + " ;commit;");
		//vendaCompraLojaVirtualRepository.updateEtiqueta(idEtiqueta, compraLojaVirtual.getId());
		
		
		/**
		 * Faz a compra do frete para etiqueta
		 */
		OkHttpClient clientCompra = new OkHttpClient().newBuilder().build();
		 okhttp3.MediaType mediaTypeC =  okhttp3.MediaType.parse("application/json");
		 okhttp3.RequestBody bodyC =  okhttp3.RequestBody.create(mediaTypeC, "{\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
		 okhttp3.Request requestC = new  okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX  +"api/v2/me/shipment/checkout")
		  .method("POST", bodyC)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();
		
		 okhttp3.Response responseC = clientCompra.newCall(requestC).execute();
		 
		 if (!responseC.isSuccessful()) {
			 return new ResponseEntity<String>("Não foi possível realizar a compra da etiqueta", HttpStatus.OK); 
		 }
		
		/**
		 * Gera as etiquetas
		 */
		
		OkHttpClient clientGera = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaTypeG = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyG = okhttp3.RequestBody.create(mediaTypeG, "{\"mode\":\"private\"orders\":[\""+idEtiqueta+"\"]}");
		okhttp3.Request requestG = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/generate")
		  .post(bodyG)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response responseG = clientGera.newCall(requestG).execute();
		
		if(!responseG.isSuccessful()) {
			return new ResponseEntity<String>("Não foi possível completar a impressão da etiqueta", HttpStatus.OK);
		}
		
		
		/**
		 * Imprime as etiquetas
		 */
		
		OkHttpClient clientIm = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaTypeIm = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyIm = okhttp3.RequestBody.create(mediaTypeIm, "{\"mode\": \"private\",    \"orders\": [\""+idEtiqueta+"\"]}");
		okhttp3.Request requestIm = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/print")
		  .method("POST", bodyIm)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response responseIm = clientIm.newCall(requestIm).execute();
		
		if (!responseIm.isSuccessful()) {
			return new ResponseEntity<String>("Não foi possível completar a emissão da etiqueta", HttpStatus.OK);
		}
		
		String urlEtiqueta = responseIm.body().string();
		
		/*Salvando a url da etiqueta*/
		
		jdbcTemplate.execute("begin; update vd_cp_loja_virt set url_imp_etiqueta = '"+urlEtiqueta+"' where id = " + compraLojaVirtual.getId() + ";commit;");
		//vendaCompraLojaVirtualRepository.updateUrlEtiqueta(urlEtiqueta, compraLojaVirtual.getId());
		
		
		/**
		 * Realiza a pesquisa de rastreio 
		 */
		
		OkHttpClient clientR = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaTypeR = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyR = okhttp3.RequestBody.create(mediaTypeR, "{\n    \"orders\": [\n        \"\""+idEtiqueta+"\"\"\n    ]\n}\"");
		okhttp3.Request requestR = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/tracking")
		  .method("POST", bodyR)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "+ ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response responseR = clientR.newCall(requestR).execute();
		
		JsonNode jsonNodeR = new ObjectMapper().readTree(responseR.body().string());
		
		Iterator<JsonNode> iteratorR = jsonNodeR.iterator();
		
		String idEtiquetaR = "";
		
		while(iteratorR.hasNext()) {
			JsonNode node = iteratorR.next();
			if (node.get("tracking") != null) {
				idEtiquetaR = node.get("id").asText();
			}else {
				idEtiquetaR = node.asText();
			}
			break;
		
		}
		
		StatusRastreio status = new StatusRastreio();
		
		List<StatusRastreio> rastreios = new ArrayList<StatusRastreio>();
		
		
		
		if (rastreios != null && idEtiqueta != idEtiquetaR) {
			
			StatusRastreio rastreio = new StatusRastreio();
			

			rastreio.setEmpresa(compraLojaVirtual.getEmpresa());
			rastreio.setPessoafisica(compraLojaVirtual.getPessoaFisica());
			rastreio.getPessoafisica().enderecoEntrega().getPessoa();
			rastreio.getEmpresa().enderecoEntrega().getPessoa();
			
			rastreio.setId(idVenda);
			rastreio.setVendaCompraLojaVirtual(compraLojaVirtual);
			rastreio.setUrlRastreio("https://app.melhorrastreio.com.br/app/HTTPSWWWMELHORRASTREIOCOMBRRASTREIO" + idEtiqueta);
			
			statusRastreioRepository.saveAndFlush(rastreio);
			
		}else {
			statusRastreioRepository.salvaUrlRastreio("https://app.melhorrastreio.com.br/app/HTTPSWWWMELHORRASTREIOCOMBRRASTREIO" + idEtiqueta , idVenda);
		}
			
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
		
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	
	
	/**
	 * Realiza a consulta api de fretes e retorna valor no momento da venda.
	 * @param consultaApiFreteDTO
	 * @return ConsultaApiFreteDTO
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/calculaFreteLojaVirtual")
	public ResponseEntity<List<TransportadoraDTO>> 
		consultaApiFrete(@RequestBody @Valid ConsultaApiFreteDTO consultaApiFreteDTO) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(consultaApiFreteDTO);
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"/api/v2/me/shipment/calculate")
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		
		Iterator<JsonNode> iterator = jsonNode.iterator();
		
		List<TransportadoraDTO> transportadoraDTOs = new ArrayList<TransportadoraDTO>();
		
		
		while(iterator.hasNext()) {
			JsonNode node = iterator.next();
		
			TransportadoraDTO transportadoraDTO = new TransportadoraDTO();
			
			
			if(node.get("id") != null) {
				transportadoraDTO.setId(node.get("id").asText());
			}
			
			if(node.get("name") != null) {
				transportadoraDTO.setNome(node.get("name").asText());
			}
			
			if(node.get("price") != null) {
				transportadoraDTO.setNome(node.get("price").asText());
			}
			
			if(node.get("company") != null) {
				transportadoraDTO.setCompany(node.get("company").get("name").asText());
				transportadoraDTO.setPicture(node.get("company").get("picture").asText());
			}
			
			if(transportadoraDTO.dadosOk()) {
				transportadoraDTOs.add(transportadoraDTO);
			}
		}
		
		return new ResponseEntity<List<TransportadoraDTO>>(transportadoraDTOs, HttpStatus.OK);
	}


}
