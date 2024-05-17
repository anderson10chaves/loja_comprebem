package adcsistemas.loja_comprebem.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import adcsistemas.loja_comprebem.model.MarcaProduto;
import adcsistemas.loja_comprebem.repository.MarcaProdutoRepository;

@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;
	
	@ResponseBody
	@GetMapping(value = "/listarMarcaProdutoCodEmp/{codEmpresa}")
	public ResponseEntity<List<MarcaProduto>> listarMarcaProdutoCodEmp(@PathVariable("codEmpresa") Long codEmpresa) {

		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.findAll(codEmpresa);

		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaPaginaMarcaProduto/{idEmpresa}/{pagina}")
	public ResponseEntity<List<MarcaProduto>> page(@PathVariable("idEmpresa") Long idEmpresa,
			@PathVariable("pagina") Integer pagina){
		
		Pageable pageable = PageRequest.of(pagina, 6, Sort.by("nomeDesc"));
		
		List<MarcaProduto> lista = marcaProdutoRepository.findPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<MarcaProduto>>(lista, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaMarcaProdutoDescEmpresa/{nomeDesc}/{empresa}")
	public ResponseEntity<List<MarcaProduto>> pesquisaMarcaProdutoDescEmpresa(@PathVariable("nomeDesc") String nomeDesc,
			@PathVariable("empresa") Long empresa) {
		
		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.pesquisaMarcaProdutoNomeDescEmpresa(nomeDesc.toUpperCase(), empresa);

		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/qtdPaginaMarcaProduto/{idEmpresa}")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa){
		
		Integer qtdPagina = marcaProdutoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	

	@ResponseBody
	@PostMapping(value = "/salvarMarcaProduto")
	public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid  MarcaProduto marcaProduto) throws ExceptionLojaComprebem {
		
		if (marcaProduto.getEmpresa() == null || marcaProduto.getEmpresa().getId() == null) {
			throw new ExceptionLojaComprebem("A empresa é obrigatório");
		}
		
		if(marcaProduto.getId() == null && marcaProdutoRepository.existeMarcaProduto(marcaProduto.getNomeDesc().trim().toString())) {
			throw new ExceptionLojaComprebem("Marca já cadastrado com esse nome" + marcaProduto.getNomeDesc());
		}
		
		MarcaProduto marcaProdutoSalvo = marcaProdutoRepository.save(marcaProduto);
		
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}
	@ResponseBody
	@PostMapping(value = "/deleteMarcaProduto")
	public ResponseEntity<String> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto) throws ExceptionLojaComprebem {
		
		if(marcaProdutoRepository.findById(marcaProduto.getId()).isPresent() == false) {
			throw new ExceptionLojaComprebem("Marca Produto já removido");
		}

		marcaProdutoRepository.deleteById(marcaProduto.getId());

		return new ResponseEntity<String>(new Gson().toJson("Marca Produto excluído com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteMarcaProdutoId/{id}")
	public ResponseEntity<?> deleteMarcaProdutoId(@PathVariable("id") Long id) {
		
		if(marcaProdutoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Marca Produto já removido", HttpStatus.OK);
		}

		marcaProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Marca Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaMarcaProdutoId/{id}")
	public ResponseEntity<?> pesquisaMarcaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		if (marcaProduto == null) {
			throw new ExceptionLojaComprebem("Marca Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaMarcaProdutoDesc/{nomeDesc}")
	public ResponseEntity<List<MarcaProduto>> pesquisaMarcaProdutoDesc(@PathVariable("nomeDesc") String nomeDesc) {

		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.pesquisaMarcaProdutoNome(nomeDesc.trim().toUpperCase());

		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}

}
