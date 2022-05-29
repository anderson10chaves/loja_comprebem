package adcsistemas.loja_comprebem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.service.AcessoService;

@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@PostMapping("/salvarAcesso/**")
	public Acesso salvarAcesso(Acesso acesso) {
		
		return acessoService.save(acesso);
	}

}
