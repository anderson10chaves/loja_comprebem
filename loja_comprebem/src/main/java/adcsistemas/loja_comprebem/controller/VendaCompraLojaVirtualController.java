package adcsistemas.loja_comprebem.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.enums.StatusContaReceber;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.ContaReceber;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.ItemVendaLoja;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.StatusRastreio;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.dto.ItemVendaDTO;
import adcsistemas.loja_comprebem.model.dto.VendaCompraLojaVirtualDTO;
import adcsistemas.loja_comprebem.repository.ContaReceberRepository;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.NotaFiscalVendaRepository;
import adcsistemas.loja_comprebem.repository.StatusRastreioRepository;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;
import adcsistemas.loja_comprebem.service.SendEmailService;
import adcsistemas.loja_comprebem.service.VendaService;

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
		/* Salva primeiro a venda e todos dados do cliente */
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistruicao("Loja Local");
		statusRastreio.setCidade("Local");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("Local");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		statusRastreioRepository.save(statusRastreio);

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

}
