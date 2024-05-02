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
import adcsistemas.loja_comprebem.model.CategoriaProduto;
import adcsistemas.loja_comprebem.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/categoriaProdutoSalvar")
	public ResponseEntity<CategoriaProduto> salvarCategoriaProduto(@RequestBody @Valid  CategoriaProduto categoriaProduto) throws ExceptionLojaComprebem {
		
		if (categoriaProduto.getEmpresa() == null || categoriaProduto.getEmpresa().getId() == null) {
			throw new ExceptionLojaComprebem("A empresa é obrigatório");
		}
		
		if(categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoriaProduto(categoriaProduto.getNomeDesc().trim().toString())) {
			throw new ExceptionLojaComprebem("Categoria já cadastrado com esse nome" + categoriaProduto.getNomeDesc());
		}
		
		CategoriaProduto categoriaProdutoSalvo = categoriaProdutoRepository.save(categoriaProduto);
		
		return new ResponseEntity<CategoriaProduto>(categoriaProdutoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/deleteCategoriaProduto")
	public ResponseEntity<?> deleteCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto) {
		
		if(categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			return new ResponseEntity<>("Categoria Produto já removida", HttpStatus.OK);
		}

		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		return new ResponseEntity<>("Categoria Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteCategoriaProdutoId/{id}")
	public ResponseEntity<?> deleteCategoriaProdutoId(@PathVariable("id") Long id) {
		
		if(categoriaProdutoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Categoria Produto já foi removida", HttpStatus.OK);
		};

		categoriaProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Categoria Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaCategoriaProdutosId/{id}")
	public ResponseEntity<?> pesquisaCatedoriaProdutosId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).orElse(null);

		if (categoriaProduto == null) {
			throw new ExceptionLojaComprebem("Categoria Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<CategoriaProduto>(categoriaProduto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaCategoriaProdutoDesc/{nomeDesc}")
	public ResponseEntity<List<CategoriaProduto>> pesquisaCategoriaProdutoNomeDesc(@PathVariable("nomeDesc") String nomeDesc) {

		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.pesquisaCategoriaProdutoNomeDesc(nomeDesc.trim().toUpperCase());

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);
	}
}
