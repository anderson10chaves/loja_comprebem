package adcsistemas.loja_comprebem.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.Empresa;
import adcsistemas.loja_comprebem.model.Usuario;
import adcsistemas.loja_comprebem.model.dto.CepDTO;
import adcsistemas.loja_comprebem.model.dto.ConsultaCnpjDTO;
import adcsistemas.loja_comprebem.repository.PessoaFisicaRepository;
import adcsistemas.loja_comprebem.repository.EmpresaRepository;
import adcsistemas.loja_comprebem.repository.UsuarioRepository;


@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmpresaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private SendEmailService sendEmailService;

	public Empresa salvarEmpresa(Empresa empresa) {

		// pessoaJuridica = pessoaRepository.save(pessoaJuridica);

		for (int i = 0; i < empresa.getEnderecos().size(); i++) {
			empresa.getEnderecos().get(i).setEmpresa(empresa);
			empresa.getEnderecos().get(i).setEmpresa(empresa);;
		}

		empresa = pessoaRepository.save(empresa);

		Usuario usuarioPj = usuarioRepository.findUserByPessoa(empresa.getId(), empresa.getEmail());

		if (usuarioPj == null) {

			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
			}

			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(empresa);
			usuarioPj.setPessoa(empresa);
			usuarioPj.setLogin(empresa.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			usuarioPj.setSenha(senhaCript);

			usuarioPj = usuarioRepository.save(usuarioPj);

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");

			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo os dados de acesso para Loja-CompreBem</b></br>");
			mensagemHtml.append("<b>Login: <b/>" + empresa.getEmail() + "</b></br>");
			mensagemHtml.append("<b>Senha: <b/>" + senha + "</br></br>");
			mensagemHtml.append("Atenciosamente Loja-CompreBem");

			try {
				sendEmailService.enviarEmailHtml("Acesso Liberado para Loja-CompreBem", mensagemHtml.toString(),
						empresa.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return empresa;

	}

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
		// pessoaJuridica = pessoaRepository.save(pessoaJuridica);

				for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
					pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
					//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
				}

				pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

				Usuario usuarioPf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

				if (usuarioPf == null) {

					String constraint = usuarioRepository.consultaConstraintAcesso();
					if (constraint != null) {
						jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
					}

					usuarioPf = new Usuario();
					usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
					usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
					usuarioPf.setPessoa(pessoaFisica);
					usuarioPf.setLogin(pessoaFisica.getEmail());

					String senha = "" + Calendar.getInstance().getTimeInMillis();
					String senhaCript = new BCryptPasswordEncoder().encode(senha);

					usuarioPf.setSenha(senhaCript);

					usuarioPf = usuarioRepository.save(usuarioPf);

					usuarioRepository.insereAcessoUser(usuarioPf.getId());
					

					StringBuilder mensagemHtml = new StringBuilder();
					
					mensagemHtml.append("<b>Segue abaixo os dados de acesso para Loja-CompreBem</b></br>");
					mensagemHtml.append("<b>Login: <b/>" + pessoaFisica.getEmail() + "</b></br>");
					mensagemHtml.append("<b>Senha: <b/>" + senha + "</br></br>");
					mensagemHtml.append("Atenciosamente Loja-CompreBem");

					try {
						sendEmailService.enviarEmailHtml("Acesso Liberado para Loja-CompreBem", mensagemHtml.toString(),
								pessoaFisica.getEmail());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return pessoaFisica;
	}
	
	public CepDTO consultaCep(String cep) {
		return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json/", CepDTO.class).getBody();
	}
	
	public ConsultaCnpjDTO consultaCnpjReceitaWs(String cnpj) {
		return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, ConsultaCnpjDTO.class).getBody();
	}

}
