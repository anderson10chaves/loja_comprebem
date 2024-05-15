package adcsistemas.loja_comprebem.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

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
	public ResponseEntity<String> deleteCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLojaComprebem {
		
		if(categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			throw new ExceptionLojaComprebem("Categoria Produto removida");
		}

		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		return new ResponseEntity<String>(new Gson().toJson("Categoria Produto excluído com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteCategoriaProdutoId/{id}")
	public ResponseEntity<?> deleteCategoriaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {
		
		if(categoriaProdutoRepository.findById(id).isPresent() == false) {
			throw new ExceptionLojaComprebem("Categoria Produto foi removida");
		}

		categoriaProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Categoria Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaCategoriaProdutosId/{id}")
	public ResponseEntity<CategoriaProduto> pesquisaCatedoriaProdutosId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).get();

		if (categoriaProduto == null) {
			throw new ExceptionLojaComprebem("Categoria Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<CategoriaProduto>(categoriaProduto, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCategoriaProdutoDescEmpresa/{nomeDesc}/{empresa}")
	public ResponseEntity<List<CategoriaProduto>> pesquisaCategoriaProdutoNomeDescEmpresa(@PathVariable("nomeDesc") String nomeDesc,
			@PathVariable("empresa") Long empresa) {
		
		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.pesquisaCategoriaProdutoNomeDescEmpresa(nomeDesc.toUpperCase(), empresa);

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaCategoriaProdutoDesc/{nomeDesc}")
	public ResponseEntity<List<CategoriaProduto>> pesquisaCategoriaProdutoDesc(@PathVariable("nomeDesc") String nomeDesc) {

		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.pesquisaCategoriaProdutoNomeDesc(nomeDesc.toUpperCase());

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listarCategoriaProdutoCodEmp/{codEmpresa}")
	public ResponseEntity<List<CategoriaProduto>> listarCategoriaProdutoCodEmp(@PathVariable("codEmpresa") Long codEmpresa) {

		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll(codEmpresa);

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/qtdPaginaCategoriaProduto/{idEmpresa}")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa){
		Integer qtdPagina = categoriaProdutoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaPaginaCatProduto/{idEmpresa}/{pagina}")
	public ResponseEntity<List<CategoriaProduto>> page(@PathVariable("idEmpresa") Long idEmpresa,
			@PathVariable("pagina") Integer pagina){
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("nomeDesc"));
		
		List<CategoriaProduto> lista = categoriaProdutoRepository.findPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<CategoriaProduto>>(lista, HttpStatus.OK);
	}
}
