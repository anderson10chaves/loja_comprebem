package adcsistemas.loja_comprebem.controller;

import java.util.List;

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

import adcsistemas.loja_comprebem.enums.TipoPessoa;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.PessoaJuridica;
import adcsistemas.loja_comprebem.model.dto.CepDTO;
import adcsistemas.loja_comprebem.model.dto.ConsultaCnpjDTO;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.PessoaFisicaRepository;
import adcsistemas.loja_comprebem.repository.PessoaRepository;
import adcsistemas.loja_comprebem.service.PessoaUserService;
import adcsistemas.loja_comprebem.service.ServiceContagemAcessoApi;
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
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;

	@ResponseBody
	@GetMapping(value = "/pesquisaPorNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> pesquisaPorNomePJ(@PathVariable("nome") String nome) {

		List<PessoaJuridica> juridicas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPoint();
		
		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasNomeFastasia/{nomeFantasia}")
	public ResponseEntity<PessoaJuridica> existeNomeFantasia(@PathVariable("nomeFantasia") String nomeFantasia) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeNomeFantasia(nomeFantasia.trim().toUpperCase());

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaNomeFastasia/{nomeFastasia}")
	public ResponseEntity<List<PessoaJuridica>> existeNomeFantasialList(@PathVariable("nomeFastasia") String nomeFastasia) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeNomeFantasiaList(nomeFastasia.trim().toUpperCase());

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasCategoria/{categoria}")
	public ResponseEntity<PessoaJuridica> existeCategoria(@PathVariable("categoria") String categoria) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeCategoria(categoria.trim().toUpperCase());

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCategoria/{categoria}")
	public ResponseEntity<List<PessoaJuridica>> existeCategoriaList(@PathVariable("categoria") String categoria) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeCategoriaList(categoria.trim().toUpperCase());

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasRazaoSocial/{razaoSocial}")
	public ResponseEntity<PessoaJuridica> existeRazaoSocial(@PathVariable("razaoSocial") String razaoSocial) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeinscMunicipal(razaoSocial.trim().toUpperCase());

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisainscMunicipal/{razaoSocial}")
	public ResponseEntity<List<PessoaJuridica>> existeRazaoSocialList(@PathVariable("razaoSocial") String razaoSocial) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeRazaoSocialList(razaoSocial.trim().toUpperCase());

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasinscMunicipal/{inscMunicipal}")
	public ResponseEntity<PessoaJuridica> existeinscMunicipal(@PathVariable("inscMunicipal") String inscMunicipal) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeinscMunicipal(inscMunicipal.trim().toUpperCase());

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisainscMunicipal/{inscMunicipal}")
	public ResponseEntity<List<PessoaJuridica>> existeinscMunicipallList(@PathVariable("inscMunicipal") String inscMunicipal) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeinscMunicipallList(inscMunicipal);

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasCnpj/{cnpj}")
	public ResponseEntity<PessoaJuridica> existeCnpj(@PathVariable("cnpj") String cnpj) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpj(cnpj);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCnpj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> existeCnpjList(@PathVariable("cnpj") String cnpj) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeCnpjList(cnpj);

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasInscEstadual/{inscEstadual}")
	public ResponseEntity<PessoaJuridica> existeinscEstadual(@PathVariable("inscEstadual") String inscEstadual) {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeinscEstadual(inscEstadual);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaInscEstadual/{inscEstadual}")
	public ResponseEntity<List<PessoaJuridica>> existeinscEstadualList(@PathVariable("inscEstadual") String inscEstadual) {

		List<PessoaJuridica> juridicas = pessoaRepository.existeinscEstadualList(inscEstadual);

		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaPorNomePF/{nome}")
	public ResponseEntity<List<PessoaFisica>> pesquisaPorNomePF(@PathVariable("nome") String nome) {

		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPoint();
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaPorCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> existeCpfList(@PathVariable("cpf") String cpf) {

		List<PessoaFisica> fisicas = pessoaFisicaRepository.existeCpfList(cpf);
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasCpf/{cpf}")
	public ResponseEntity<PessoaFisica> existeCpf(@PathVariable("cpf") String cpf) {

		PessoaFisica pessoaFisica = pessoaFisicaRepository.existeCpf(cpf);

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep) {

		CepDTO cepDTO = pessoaUserService.consultaCep(cep);

		return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDTO> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj) {

		ConsultaCnpjDTO consultaCnpjDTO = pessoaUserService.consultaCnpjReceitaWs(cnpj);

		return new ResponseEntity<ConsultaCnpjDTO>(consultaCnpjDTO, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws ExceptionLojaComprebem {

		if (pessoaJuridica == null) {
			throw new ExceptionLojaComprebem("Pessoa Juridica não pode ser nulo!");
		}
		
		if (pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionLojaComprebem("Informe o tipo Jurídico ou Fornecedor");
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
		
		if (pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
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
