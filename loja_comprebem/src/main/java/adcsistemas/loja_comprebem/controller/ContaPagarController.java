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
import adcsistemas.loja_comprebem.model.ContaPagar;
import adcsistemas.loja_comprebem.repository.ContaPagarRepository;

@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;

	@ResponseBody
	@PostMapping(value = "/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar)
			throws ExceptionLojaComprebem {

		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Empresa deve ser informada");
		}

		if (contaPagar.getPessoaFisica() == null || contaPagar.getPessoaFisica().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Pessoa Juridica deve ser informada");
		}

		if (contaPagar.getPessoa_fornecedor() == null || contaPagar.getPessoa_fornecedor().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Pessoa Fornecedor deve ser informada");
		}

		if (contaPagar.getId() == null) {
			List<ContaPagar> contaPagars = contaPagarRepository
					.pesquisaContaPagarDesc(contaPagar.getDescricao().trim().toUpperCase());

			if (!contaPagars.isEmpty()) {
				throw new ExceptionLojaComprebem(
						"A Conta a Pagar já cadastrado com esse nome " + contaPagar.getDescricao());
			}
		}

		ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);

		return new ResponseEntity<ContaPagar>(contaPagarSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteContaPagar")
	public ResponseEntity<?> deleteContaPagar(@RequestBody ContaPagar contaPagar) {

		if (contaPagarRepository.findById(contaPagar.getId()).isPresent() == false) {
			return new ResponseEntity<>("Conta a Pagar  com ID: " + contaPagar.getId() + " já removido", HttpStatus.OK);
		}

		contaPagarRepository.deleteById(contaPagar.getId());

		return new ResponseEntity<>("Conta a Pagar excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteContaPagarId/{id}")
	public ResponseEntity<?> deleteContaPagarId(@PathVariable("id") Long id) {

		if (contaPagarRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Conta a Pagar já removido", HttpStatus.OK);
		}

		contaPagarRepository.deleteById(id);

		return new ResponseEntity<>("Conta a Pagar excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaContaPagarId/{id}")
	public ResponseEntity<?> pesquisaContaPagarId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

		if (contaPagar == null) {
			throw new ExceptionLojaComprebem("Conta a Pagar não encontrado com código: " + id);
		}

		return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaContaPagarDesc/{descricao}")
	public ResponseEntity<List<ContaPagar>> pesquisaContaPagarDesc(@PathVariable("descricao") String descricao) {

		List<ContaPagar> contaPagars = contaPagarRepository.pesquisaContaPagarDesc(descricao.trim().toUpperCase());

		return new ResponseEntity<List<ContaPagar>>(contaPagars, HttpStatus.OK);
	}

}
