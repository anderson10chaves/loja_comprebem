package adcsistemas.loja_comprebem.controller;

import java.util.ArrayList;
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
import adcsistemas.loja_comprebem.model.AvaliacaoProduto;
import adcsistemas.loja_comprebem.model.dto.AvaliacaoProdutoDTO;
import adcsistemas.loja_comprebem.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {
	
	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

	@ResponseBody
	@PostMapping(value = "/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionLojaComprebem {
		
		if(avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() < 0)) {
			throw new ExceptionLojaComprebem("Informe a Empresa");
		}
		
		if(avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() < 0)) {
			throw new ExceptionLojaComprebem("Informe o Produto");
		}
		
		if(avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() < 0)) {
			throw new ExceptionLojaComprebem("Informe a Pessoa");
		}
		
		AvaliacaoProduto avaliacaoProdutoSalvo = avaliacaoProdutoRepository.save(avaliacaoProduto);
		
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProdutoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deletaAvaliacaoProdutoPessoa/{id}")
	public ResponseEntity<?> deleteAvaliacaoPessoa(@PathVariable("id") Long id) {
		
		
		  if(avaliacaoProdutoRepository.findById(id).isPresent() == false) {
		  return new ResponseEntity<>("Avaliação Produto já removido", HttpStatus.OK);
		  }
		 
		
		avaliacaoProdutoRepository.deleteAvaliacaoPessoa(id);
		
		return new ResponseEntity<>("Avaliação removido com sucesso", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaAvaliacaoProdutoId/{idProduto}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> pesquisaAvaliacaoProdutoId(@PathVariable("idProduto") Long idProduto) {
		
		List<AvaliacaoProdutoDTO> dtos = new ArrayList<AvaliacaoProdutoDTO>();

		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.pesquisaAvaliacaoProdutoId(idProduto);
		
		
		  for(AvaliacaoProduto avaliacaoProduto : avaliacaoProdutos) {
		  AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();
		  avaliacaoProdutoDTO.setId(avaliacaoProduto.getId());
		  avaliacaoProdutoDTO.setDescricao(avaliacaoProduto.getDescricao());
		  avaliacaoProdutoDTO.setNota(avaliacaoProduto.getNota());
		  avaliacaoProdutoDTO.setPessoa(avaliacaoProduto.getPessoa().getId());
		  avaliacaoProdutoDTO.setEmpresa(avaliacaoProduto.getEmpresa().getId());
		  avaliacaoProdutoDTO.setProduto(avaliacaoProduto.getProduto().getId());
		 
		  dtos.add(avaliacaoProdutoDTO);
		  
		  }
		
		return new ResponseEntity<List<AvaliacaoProdutoDTO>>(dtos, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaAvaliacaoPessoaId/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> pesquisaAvaliacaoPessoaId(@PathVariable("idPessoa") Long idPessoa) {
		
		List<AvaliacaoProdutoDTO> dtos = new ArrayList<AvaliacaoProdutoDTO>();

		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.pesquisaAvaliacaoPessoa(idPessoa);
		
		for(AvaliacaoProduto avaliacaoProduto : avaliacaoProdutos) {
			  AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();
			  avaliacaoProdutoDTO.setId(avaliacaoProduto.getId());
			  avaliacaoProdutoDTO.setDescricao(avaliacaoProduto.getDescricao());
			  avaliacaoProdutoDTO.setNota(avaliacaoProduto.getNota());
			  avaliacaoProdutoDTO.setPessoa(avaliacaoProduto.getPessoa().getId());
			  avaliacaoProdutoDTO.setEmpresa(avaliacaoProduto.getEmpresa().getId());
			  avaliacaoProdutoDTO.setProduto(avaliacaoProduto.getProduto().getId());
			  
			  dtos.add(avaliacaoProdutoDTO);
		}
		
		return new ResponseEntity<List<AvaliacaoProdutoDTO>>(dtos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaAvaliacaoProdutoPessoaId/{idProduto}/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> pesquisaAvaliacaoProdutoPessoaId(@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) {
		
		List<AvaliacaoProdutoDTO> dtos = new ArrayList<AvaliacaoProdutoDTO>();
		
			
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.pesquisaAvaliacaoProdutoPessoa(idProduto, idPessoa);

		for(AvaliacaoProduto avaliacaoProduto : avaliacaoProdutos) {
			  AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();
			  avaliacaoProdutoDTO.setId(avaliacaoProduto.getId());
			  avaliacaoProdutoDTO.setDescricao(avaliacaoProduto.getDescricao());
			  avaliacaoProdutoDTO.setNota(avaliacaoProduto.getNota());
			  avaliacaoProdutoDTO.setPessoa(avaliacaoProduto.getPessoa().getId());
			  avaliacaoProdutoDTO.setEmpresa(avaliacaoProduto.getEmpresa().getId());
			  avaliacaoProdutoDTO.setProduto(avaliacaoProduto.getProduto().getId());
			  
			  dtos.add(avaliacaoProdutoDTO);
		}
		
		return new ResponseEntity<List<AvaliacaoProdutoDTO>>(dtos, HttpStatus.OK);
	}
}
