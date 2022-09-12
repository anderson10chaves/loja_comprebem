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
import adcsistemas.loja_comprebem.model.CupomDesconto;
import adcsistemas.loja_comprebem.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {
	
	
	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarCupomDesconto")
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto) throws ExceptionLojaComprebem {

		CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);

		return new ResponseEntity<CupomDesconto>(cupomDescontoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCupomDescontoId/{id}")
	public ResponseEntity<CupomDesconto> pesquisaCupomDescontoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		CupomDesconto cupomDesconto = cupomDescontoRepository.findById(id).orElse(null);

		if (cupomDesconto == null) {
			throw new ExceptionLojaComprebem("Cupom Desconto não encontrado com código: " + id);
		}

		return new ResponseEntity<CupomDesconto>(cupomDesconto, HttpStatus.OK);
	}

	
	@ResponseBody
	@DeleteMapping(value = "/deleteCupomDescontoId/{id}")
	public ResponseEntity<?> deleteCupomDescontoId(@PathVariable("id") Long id) {
		
		cupomDescontoRepository.deleteById(id);

		return new ResponseEntity<>("Cupom Desconto excluído com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaCupomDescontoEmpresa/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> listaCupomDescontoEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.listaCupomDescontoEmpresa(idEmpresa), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaCupomDesconto")
	public ResponseEntity<List<CupomDesconto>> listaCupomDesconto(){
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(), HttpStatus.OK);
	}

}
