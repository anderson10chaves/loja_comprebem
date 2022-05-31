package adcsistemas.loja_comprebem.controller;

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

import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.repository.AcessoRepository;
import adcsistemas.loja_comprebem.service.AcessoService;

@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository; 
	
	@ResponseBody
	@PostMapping(value =  "/salvarAcesso")
	public ResponseEntity<Acesso>  salvarAcesso(@RequestBody Acesso acesso) {
		
		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value =  "/deleteAcesso")
	public ResponseEntity<?>  deleteAcesso(@RequestBody Acesso acesso) {
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity<>("Acesso excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value =  "/deleteAcessoId/{id}")
	public ResponseEntity<?>  deleteAcessoId(@PathVariable("id") Long id) {
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity<>("Acesso excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value =  "/consultaAcessoId/{id}")
	public ResponseEntity<?>  consultaAcessoId(@PathVariable("id") Long id) {
		
		Acesso acesso = acessoRepository.findById(id).get();
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value =  "/consultaAcessoDesc/{desc}")
	public ResponseEntity<List<Acesso>>  consultaAcessoDesc(@PathVariable("desc") String desc) {
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc);
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}

}
