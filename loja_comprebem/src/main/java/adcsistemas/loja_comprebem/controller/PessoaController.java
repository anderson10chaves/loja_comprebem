package adcsistemas.loja_comprebem.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.PessoaJuridica;
import adcsistemas.loja_comprebem.model.dto.CepDTO;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.PessoaFisicaRepository;
import adcsistemas.loja_comprebem.repository.PessoaRepository;
import adcsistemas.loja_comprebem.service.PessoaUserService;
import adcsistemas.loja_comprebem.utils.ValidaCnpj;
import adcsistemas.loja_comprebem.utils.ValidaCpf;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaUserService pessoaUserService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@ResponseBody
	@GetMapping(value = "/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep) {

		CepDTO cepDTO = pessoaUserService.consultaCep(cep);

		return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws ExceptionLojaComprebem {

		if (pessoaJuridica == null) {
			throw new ExceptionLojaComprebem("Pessoa Juridica não pode ser nulo!");
		}

		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpj(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaComprebem("Já existe CNPJ com esse codigo: " + pessoaJuridica.getCnpj());
		}

		if (pessoaJuridica.getId() == null
				&& pessoaRepository.existeinscEstadual(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLojaComprebem(
					"Já existe Inscrição Estadual com esse codigo: " + pessoaJuridica.getInscEstadual());
		}

		if (!ValidaCnpj.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLojaComprebem("Cnpj : " + pessoaJuridica.getCnpj() + "é inválido!.");
		}

		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {

			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

			}
		} else {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {

					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}

		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody @Valid PessoaFisica pessoaFisica)
			throws ExceptionLojaComprebem {

		if (pessoaFisica == null) {
			throw new ExceptionLojaComprebem("Pessoa Fisica não pode ser nulo!");
		}

		if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpf(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaComprebem("Já existe CPF com esse codigo: " + pessoaFisica.getCpf());
		}

		if (!ValidaCpf.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaComprebem("CPF : " + pessoaFisica.getCpf() + "está incorreto ou inválido!.");
		}

		if (pessoaFisica.getId() == null || pessoaFisica.getId() <= 0) {

			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {

				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());

				pessoaFisica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaFisica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaFisica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaFisica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaFisica.getEnderecos().get(p).setUf(cepDTO.getUf());
			}
		} else {
			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {

				Endereco enderecoTemp = enderecoRepository.findById(pessoaFisica.getEnderecos().get(p).getId()).get();

				if (!enderecoTemp.getCep().equals(pessoaFisica.getEnderecos().get(p).getCep())) {

					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());

					pessoaFisica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaFisica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaFisica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaFisica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaFisica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}

		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
}
