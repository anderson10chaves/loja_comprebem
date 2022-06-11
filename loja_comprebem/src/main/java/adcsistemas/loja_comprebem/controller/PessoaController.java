package adcsistemas.loja_comprebem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.PessoaJuridica;
import adcsistemas.loja_comprebem.repository.PessoaRepository;
import adcsistemas.loja_comprebem.service.PessoaUserService;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;

	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaComprebem {

		if (pessoaJuridica == null) {
			throw new ExceptionLojaComprebem("Pessoa Juridica não pode ser nulo!");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpj(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaComprebem("Já existe CNPJ com esse codigo: " + pessoaJuridica.getCnpj());
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeinscEstadual(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLojaComprebem("Já existe Inscrição Estadual com esse codigo: " + pessoaJuridica.getInscEstadual());
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoJuridica(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
}
