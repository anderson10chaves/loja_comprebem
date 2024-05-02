package adcsistemas.loja_comprebem.controller;

import java.util.ArrayList;
import java.util.List;

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

import adcsistemas.loja_comprebem.model.ImagemProduto;
import adcsistemas.loja_comprebem.model.dto.ImagemProdutoDTO;
import adcsistemas.loja_comprebem.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {
	
	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto) {
		
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
		imagemProdutoDTO.setImagemMiniatura(imagemProdutoDTO.getImagemMiniatura());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		
		
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemProdutoTodos/{idProduto}")
	public ResponseEntity<?> deleteImagemProdutoTodos(@PathVariable("idProduto") Long idProduto) {
		
		if(imagemProdutoRepository.findById(idProduto).isPresent() == false) {
			return new ResponseEntity<>("Imagem do Produto já removida", HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(idProduto);
		
		return new ResponseEntity<>("Imagem do Produto excluída com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemProduto")
	public ResponseEntity<?> deleteImagemProduto(@RequestBody ImagemProduto imagemProduto) {
		
		if(imagemProdutoRepository.findById(imagemProduto.getId()).isPresent() == false) {
			return new ResponseEntity<>("Imagem do Produto já removida", HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(imagemProduto.getId());
		
		return new ResponseEntity<>("Imagem do Produto excluída com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemProdutoId/{id}")
	public ResponseEntity<?> deleteImagemProdutoId(@PathVariable("id") Long id) {
		
		if(imagemProdutoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Imagem do Produto já removida", HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(id);
		
		return new ResponseEntity<>("Imagem do Produto excluída com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaImagemProdutoId/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> pesquisaImagemProdutoId(@PathVariable("idProduto") Long idProduto) {
		
		List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();
		
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.pesquisaImagemProdutoId(idProduto);
		
		for(ImagemProduto imagemProduto : imagemProdutos) {
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
			imagemProdutoDTO.setImagemMiniatura(imagemProdutoDTO.getImagemMiniatura());
			imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
			
			dtos.add(imagemProdutoDTO);
		}
		
		return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);
		
	}

}
