package adcsistemas.loja_comprebem.controller;

import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.enums.TipoPessoa;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Empresa;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.Usuario;
import adcsistemas.loja_comprebem.model.dto.CepDTO;
import adcsistemas.loja_comprebem.model.dto.ConsultaCnpjDTO;
import adcsistemas.loja_comprebem.model.dto.ObjetoMsgEmailRecSenha;
import adcsistemas.loja_comprebem.repository.EmpresaRepository;
import adcsistemas.loja_comprebem.repository.EnderecoRepository;
import adcsistemas.loja_comprebem.repository.PessoaFisicaRepository;
import adcsistemas.loja_comprebem.repository.UsuarioRepository;
import adcsistemas.loja_comprebem.service.PessoaUserService;
import adcsistemas.loja_comprebem.service.SendEmailService;
import adcsistemas.loja_comprebem.service.ServiceContagemAcessoApi;
import adcsistemas.loja_comprebem.utils.ValidaCnpj;
import adcsistemas.loja_comprebem.utils.ValidaCpf;

@RestController
public class PessoaController {

	@Autowired
	private EmpresaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaUserService pessoaUserService;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private SendEmailService sendEmailService;
	
	@ResponseBody
	@GetMapping(value = "/pesquisaPorNomePJ/{nome}")
	public ResponseEntity<List<Empresa>> pesquisaPorNomePJ(@PathVariable("nome") String nome) {

		List<Empresa> empresas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPoint();
		
		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasNomeFastasia/{nomeFantasia}")
	public ResponseEntity<Empresa> existeNomeFantasia(@PathVariable("nomeFantasia") String nomeFantasia) {

		Empresa empresa = pessoaRepository.existeNomeFantasia(nomeFantasia.trim().toUpperCase());

		return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaNomeFastasia/{nomeFastasia}")
	public ResponseEntity<List<Empresa>> existeNomeFantasialList(@PathVariable("nomeFastasia") String nomeFastasia) {

		List<Empresa> empresas = pessoaRepository.existeNomeFantasiaList(nomeFastasia.trim().toUpperCase());

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasCategoria/{categoria}")
	public ResponseEntity<Empresa> existeCategoria(@PathVariable("categoria") String categoria) {

		Empresa empresas = pessoaRepository.existeCategoria(categoria.trim().toUpperCase());

		return new ResponseEntity<Empresa>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCategoria/{categoria}")
	public ResponseEntity<List<Empresa>> existeCategoriaList(@PathVariable("categoria") String categoria) {

		List<Empresa> empresas = pessoaRepository.existeCategoriaList(categoria.trim().toUpperCase());

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasRazaoSocial/{razaoSocial}")
	public ResponseEntity<Empresa> existeRazaoSocial(@PathVariable("razaoSocial") String razaoSocial) {

		Empresa empresas = pessoaRepository.existeinscMunicipal(razaoSocial.trim().toUpperCase());

		return new ResponseEntity<Empresa>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisainscMunicipal/{razaoSocial}")
	public ResponseEntity<List<Empresa>> existeRazaoSocialList(@PathVariable("razaoSocial") String razaoSocial) {

		List<Empresa> empresas = pessoaRepository.existeRazaoSocialList(razaoSocial.trim().toUpperCase());

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasinscMunicipal/{inscMunicipal}")
	public ResponseEntity<Empresa> existeinscMunicipal(@PathVariable("inscMunicipal") String inscMunicipal) {

		Empresa empresas = pessoaRepository.existeinscMunicipal(inscMunicipal.trim().toUpperCase());

		return new ResponseEntity<Empresa>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisainscMunicipal/{inscMunicipal}")
	public ResponseEntity<List<Empresa>> existeinscMunicipallList(@PathVariable("inscMunicipal") String inscMunicipal) {

		List<Empresa> empresas = pessoaRepository.existeinscMunicipallList(inscMunicipal);

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasCnpj/{cnpj}")
	public ResponseEntity<Empresa> existeCnpj(@PathVariable("cnpj") String cnpj) {

		Empresa empresas = pessoaRepository.existeCnpj(cnpj);

		return new ResponseEntity<Empresa>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaCnpj/{cnpj}")
	public ResponseEntity<List<Empresa>> existeCnpjList(@PathVariable("cnpj") String cnpj) {

		List<Empresa> empresas = pessoaRepository.existeCnpjList(cnpj);

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultasInscEstadual/{inscEstadual}")
	public ResponseEntity<Empresa> existeinscEstadual(@PathVariable("inscEstadual") String inscEstadual) {

		Empresa empresas = pessoaRepository.existeinscEstadual(inscEstadual);

		return new ResponseEntity<Empresa>(empresas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/pesquisaInscEstadual/{inscEstadual}")
	public ResponseEntity<List<Empresa>> existeinscEstadualList(@PathVariable("inscEstadual") String inscEstadual) {

		List<Empresa> empresas = pessoaRepository.existeinscEstadualList(inscEstadual);

		return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.OK);
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
	@PostMapping(value = "/salvarEmpresa")
	public ResponseEntity<Empresa> salvarEmpresa(@RequestBody @Valid Empresa empresa)
			throws ExceptionLojaComprebem {

		if (empresa == null) {
			throw new ExceptionLojaComprebem("A empresa não pode ser nulo!");
		}
		
		if (empresa.getTipoPessoa() == null) {
			throw new ExceptionLojaComprebem("Informe o tipo Empresa ou Fornecedor");
		}

		if (empresa.getId() == null && pessoaRepository.existeCnpj(empresa.getCnpj()) != null) {
			throw new ExceptionLojaComprebem("Já existe CNPJ com esse codigo: " + empresa.getCnpj());
		}

		if (empresa.getId() == null
				&& pessoaRepository.existeinscEstadual(empresa.getInscEstadual()) != null) {
			throw new ExceptionLojaComprebem(
					"Já existe Inscrição Estadual com esse codigo: " + empresa.getInscEstadual());
		}

		if (!ValidaCnpj.isCNPJ(empresa.getCnpj())) {
			throw new ExceptionLojaComprebem("Cnpj : " + empresa.getCnpj() + "é inválido!.");
		}

		if (empresa.getId() == null || empresa.getId() <= 0) {

			for (int p = 0; p < empresa.getEnderecos().size(); p++) {

				CepDTO cepDTO = pessoaUserService.consultaCep(empresa.getEnderecos().get(p).getCep());

				empresa.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				empresa.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				empresa.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				empresa.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				empresa.getEnderecos().get(p).setUf(cepDTO.getUf());

			}
		} else {
			for (int p = 0; p < empresa.getEnderecos().size(); p++) {
				Endereco enderecoTemp = enderecoRepository.findById(empresa.getEnderecos().get(p).getId()).get();

				if (!enderecoTemp.getCep().equals(empresa.getEnderecos().get(p).getCep())) {

					CepDTO cepDTO = pessoaUserService.consultaCep(empresa.getEnderecos().get(p).getCep());

					empresa.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					empresa.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					empresa.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					empresa.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					empresa.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}

		empresa = pessoaUserService.salvarEmpresa(empresa);

		return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/recuperarAcesso")
	public ResponseEntity<ObjetoMsgEmailRecSenha> recuperarSenha(@RequestBody String login) throws Exception, MessagingException {
		
		Usuario usuario = usuarioRepository.findUserByLogin(login);
		
		if(usuario == null) {
			return (ResponseEntity<ObjetoMsgEmailRecSenha>) new ResponseEntity<ObjetoMsgEmailRecSenha>(new ObjetoMsgEmailRecSenha("Usuário não encontrado"), HttpStatus.OK);
		}
		
		String senha = UUID.randomUUID().toString();
		
		senha = senha.substring(0, 6);
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
		
		usuarioRepository.updateSenhaUser(senhaCriptografada, login);
		
		StringBuilder msgEmail = new StringBuilder();
		msgEmail.append("<h2>Loja CompreBem<h2>");
		msgEmail.append("<b>Senha nova : </b>").append(senha);
		sendEmailService.enviarEmailHtml("Sua nova senha foi gerada!", msgEmail.toString(), usuario.getPessoa().getEmail());
		
		
		return (ResponseEntity<ObjetoMsgEmailRecSenha>) new ResponseEntity<ObjetoMsgEmailRecSenha>( new ObjetoMsgEmailRecSenha("Senha enviada para seu e-mail de cadastro!"), HttpStatus.OK);
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
