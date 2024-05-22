package adcsistemas.loja_comprebem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.model.CupomDesconto;
import adcsistemas.loja_comprebem.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesconto/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> listaCupomDesc(@PathVariable("idEmpresa") Long idEmpresa){
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.cupomDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesconto")
	public ResponseEntity<List<CupomDesconto>> listaCupomDesc(){
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll() , HttpStatus.OK);
	}
}
