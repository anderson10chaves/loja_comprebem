package adcsistemas.loja_comprebem.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.PessoaJuridica;
import adcsistemas.loja_comprebem.model.Usuario;
import adcsistemas.loja_comprebem.model.dto.CepDTO;
import adcsistemas.loja_comprebem.model.dto.ConsultaCnpjDTO;
import adcsistemas.loja_comprebem.repository.PessoaFisicaRepository;
import adcsistemas.loja_comprebem.repository.PessoaRepository;
import adcsistemas.loja_comprebem.repository.UsuarioRepository;


@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private SendEmailService sendEmailService;

	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {

		// pessoaJuridica = pessoaRepository.save(pessoaJuridica);

		for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
			pessoaJuridica.getEnderecos().get(i).setPessoaJuridica(pessoaJuridica);
			pessoaJuridica.getEnderecos().get(i).setPessoaJuridica(pessoaJuridica);;
		}

		pessoaJuridica = pessoaRepository.save(pessoaJuridica);

		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

		if (usuarioPj == null) {

			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
			}

			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setPessoa(pessoaJuridica);
			usuarioPj.setPessoa(pessoaJuridica);
			usuarioPj.setLogin(pessoaJuridica.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			usuarioPj.setSenha(senhaCript);

			usuarioPj = usuarioRepository.save(usuarioPj);

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");

			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo os dados de acesso para Loja-CompreBem</b></br>");
			mensagemHtml.append("<b>Login: <b/>" + pessoaJuridica.getEmail() + "</b></br>");
			mensagemHtml.append("<b>Senha: <b/>" + senha + "</br></br>");
			mensagemHtml.append("Atenciosamente Loja-CompreBem");

			try {
				sendEmailService.enviarEmailHtml("Acesso Liberado para Loja-CompreBem", mensagemHtml.toString(),
						pessoaJuridica.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pessoaJuridica;

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
					usuarioPf.setPessoa(pessoaFisica.getPessoaJuridica());
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
