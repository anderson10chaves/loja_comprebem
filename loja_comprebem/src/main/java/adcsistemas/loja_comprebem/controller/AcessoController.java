package adcsistemas.loja_comprebem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

}
