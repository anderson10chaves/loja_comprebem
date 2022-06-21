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
import adcsistemas.loja_comprebem.model.MarcaProduto;
import adcsistemas.loja_comprebem.repository.MarcaProdutoRepository;

@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;

	@ResponseBody
	@PostMapping(value = "/salvarMarcaProduto")
	public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionLojaComprebem {

		if (marcaProduto.getId() == null) {
			List<MarcaProduto> marcaProdutos = marcaProdutoRepository.pesquisaMarcaProdutoNome(marcaProduto.getNomeDesc().toUpperCase());

			if (!marcaProdutos.isEmpty()) {
				throw new ExceptionLojaComprebem("Marca Produto já existe com essa descrição: " + marcaProduto.getNomeDesc());
			}
		}

		MarcaProduto marcaProdutoSalvo = marcaProdutoRepository.save(marcaProduto);

		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteMarcaProduto")
	public ResponseEntity<?> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto) {
		
		if(marcaProdutoRepository.findById(marcaProduto.getId()).isPresent() == false) {
			return new ResponseEntity<>("Marca Produto já removido", HttpStatus.OK);
		}

		marcaProdutoRepository.deleteById(marcaProduto.getId());

		return new ResponseEntity<>("Marca Produto excluído com sucesso", HttpStatus.OK);
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
