package adcsistemas.loja_comprebem.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.NotaFiscalCompra;
import adcsistemas.loja_comprebem.repository.NotaFiscalCompraRepository;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionLojaComprebem {

		if(notaFiscalCompra.getNumeroNota() != null) {
			List<NotaFiscalCompra> notasFiscalCompras = notaFiscalCompraRepository.pesquisaNotaFiscalNumeroNota(notaFiscalCompra.getNumeroNota().trim().toUpperCase());
			
			if(!notasFiscalCompras.isEmpty()) {
				throw new ExceptionLojaComprebem("Nota Fiscal Compra já existe com essa numeração: " + notaFiscalCompra.getNumeroNota());
			}
		}
		
		if(notaFiscalCompra.getDescricaoObservacao() != null) {
			List<NotaFiscalCompra> notasFiscalCompras = notaFiscalCompraRepository.pesquisaNotaFiscalDesc(notaFiscalCompra.getDescricaoObservacao().trim().toUpperCase());
			
			if(!notasFiscalCompras.isEmpty()) {
				throw new ExceptionLojaComprebem("Nota Fiscal Compra já existe com essa descrição: " + notaFiscalCompra.getDescricaoObservacao());
			}
		}
		
		if(notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Pessoa Jurídica referente a Nota Fiscal de Compra deve ser Informada");
		}
		
		if(notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Empresa referente a Nota Fiscal de Compra deve ser Informada");
		}
		
		if(notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Conta Pagar da Nota Fiscal de Compra deve ser Informada");
		}

		NotaFiscalCompra notaFiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteNotaFiscalCompra")
	public ResponseEntity<?> deleteNotaFiscalCompra(@RequestBody NotaFiscalCompra notaFiscalCompra) {
		
		if(notaFiscalCompraRepository.findById(notaFiscalCompra.getId()).isPresent() == false) {
			return new ResponseEntity<>("Nota Fiscal Compra já removido", HttpStatus.OK);
		}
		
		notaFiscalCompraRepository.deleteById(notaFiscalCompra.getId());

		return new ResponseEntity<>("Nota Fiscal Compra excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteNotaFiscalCompraId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraId(@PathVariable("id") Long id) {
		
		if(notaFiscalCompraRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Nota Fiscal Compra já removido", HttpStatus.OK);
		}

		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id); // primeiro delete filhos
		notaFiscalCompraRepository.deleteById(id); // depois delete pai

		return new ResponseEntity<>("Nota Fiscal Compra excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaNotaFiscalCompraId/{id}")
	public ResponseEntity<?> pesquisaNotaFiscalCompraId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaComprebem("Nota Fiscal de Compra não encontrado com código: " + id);
		}

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaNotaFiscalDesc/{descricaoObservacao}")
	public ResponseEntity<List<NotaFiscalCompra>> pesquisaNotaFiscalDesc(@PathVariable("descricaoObservacao") String descricaoObservacao) {

		List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.pesquisaNotaFiscalDesc(descricaoObservacao.trim().toUpperCase());

		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);
	}
}
