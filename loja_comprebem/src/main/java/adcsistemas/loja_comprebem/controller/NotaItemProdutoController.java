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
import adcsistemas.loja_comprebem.model.NotaItemProduto;
import adcsistemas.loja_comprebem.repository.NotaItemProdutoRepository;

@RestController
public class NotaItemProdutoController {
	
	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarNotaItemProduto")
	public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionLojaComprebem {

		if(notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
			throw new ExceptionLojaComprebem("O Produto é Obrigatório");
		};
		
		if(notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
			throw new ExceptionLojaComprebem("A nota Fiscal é Obrigatório");
		};
		
		if(notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Empresa referente a nota é Obrigatório");
		};
		
		if(notaItemProduto.getId() == null) {
			List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.pesquisaNotaItemProdutoNota(
					notaItemProduto.getProduto().getId(),
					notaItemProduto.getNotaFiscalCompra().getId());
			
			if(!notaItemProdutos.isEmpty()) {
				throw new ExceptionLojaComprebem("Produto já existe cadastrado para esta nota");
			}
		}
		
		if(notaItemProduto.getQuantidade() <= 0) {
			throw new ExceptionLojaComprebem("A quantidade de Produtos da nota é 1 item");
		}

		NotaItemProduto notaItemProdutoSalvo = notaItemProdutoRepository.save(notaItemProduto);

		return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteNotaItemProduto")
	public ResponseEntity<?> deleteNotaItemProduto(@RequestBody NotaItemProduto notaItemProduto) {
		
		if(notaItemProdutoRepository.findById(notaItemProduto.getId()).isPresent() == false) {
			return new ResponseEntity<>("Nota Item Produto já removido", HttpStatus.OK);
		}
		
		notaItemProdutoRepository.deleteNotaItemProduto(notaItemProduto.getId());

		return new ResponseEntity<>("Nota Item Produto excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteNotaItemProdutoId/{id}")
	public ResponseEntity<?> deleteNotaItemProdutoId(@PathVariable("id") Long id) {
		
		if(notaItemProdutoRepository.findById(id).isEmpty() == false) {
			return new ResponseEntity<>("Nota Item Produto já removido", HttpStatus.OK);
		}

		notaItemProdutoRepository.deleteNotaItemProduto(id);

		return new ResponseEntity<>("Nota Item Produto excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaNotaItemProdutoId/{id}")
	public ResponseEntity<?> pesquisaNotaItemProdutoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		NotaItemProduto notaItemProduto = notaItemProdutoRepository.findById(id).orElse(null);

		if (notaItemProduto == null) {
			throw new ExceptionLojaComprebem("Nota Item Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<NotaItemProduto>(notaItemProduto, HttpStatus.OK);
	}
	
}

