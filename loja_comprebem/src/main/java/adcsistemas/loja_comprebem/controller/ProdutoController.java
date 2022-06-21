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
import adcsistemas.loja_comprebem.model.Produto;
import adcsistemas.loja_comprebem.repository.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaComprebem {

		if(produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Empresa deve ser informada");
		}
		
		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.pesquisaProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionLojaComprebem("Produto já existe com essa descrição: " + produto.getNome());
			}
			
			if(produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
				throw new ExceptionLojaComprebem("A Categoria Produto dever ser informada");
			}
			
			if(produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
				throw new ExceptionLojaComprebem("A Marca Produto dever ser informada");
			}
		}

		Produto produtoSalvo = produtoRepository.save(produto);

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {
		
		if(produtoRepository.findById(produto.getId()).isEmpty() == false) {
			return new ResponseEntity<>("Produto já removido", HttpStatus.OK);
		}

		produtoRepository.deleteById(produto.getId());

		return new ResponseEntity<>("Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteProdutoId/{id}")
	public ResponseEntity<?> deleteAcessoId(@PathVariable("id") Long id) {
		
		if(produtoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Produto já removido", HttpStatus.OK);
		}

		produtoRepository.deleteById(id);

		return new ResponseEntity<>("Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaProdutoId/{id}")
	public ResponseEntity<?> pesquisaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new ExceptionLojaComprebem("Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaProdutoDesc/{desc}")
	public ResponseEntity<List<Produto>> pesquisaProdutoDesc(@PathVariable("nome") String nome) {

		List<Produto> produto = produtoRepository.pesquisaProdutoNome(nome.toUpperCase());

		return new ResponseEntity<List<Produto>>(produto, HttpStatus.OK);
	}

}
