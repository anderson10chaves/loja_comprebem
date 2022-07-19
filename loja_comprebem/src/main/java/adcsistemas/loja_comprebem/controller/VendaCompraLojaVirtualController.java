package adcsistemas.loja_comprebem.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.ItemVendaLoja;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.dto.ItemVendaDTO;
import adcsistemas.loja_comprebem.model.dto.VendaCompraLojaVirtualDTO;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.NotaFiscalVendaRepository;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;

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

	@ResponseBody
	@PostMapping(value = "/salvarVendas")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendas(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaComprebem {

		vendaCompraLojaVirtual.getPessoaFisica().setPessoaJuridica(vendaCompraLojaVirtual.getPessoaJuridica());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoaFisica()).getBody();
		vendaCompraLojaVirtual.setPessoaFisica(pessoaFisica);

		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

		vendaCompraLojaVirtual.getNotaFiscalVenda().setPessoaJuridica(vendaCompraLojaVirtual.getPessoaJuridica());
		/* Salva primeiro a venda e todos dados do cliente */
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i)
					.setPessoaJuridica(vendaCompraLojaVirtual.getPessoaJuridica());
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
	@GetMapping(value = "/pesquisaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> pesquisaVendaId(@PathVariable("id") Long idVenda) {

		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda)
				.orElse(new VendaCompraLojaVirtual());

		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoaFisica());

		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoaJuridica());
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

}
