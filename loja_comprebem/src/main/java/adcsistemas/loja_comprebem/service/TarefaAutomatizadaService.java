package adcsistemas.loja_comprebem.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.Usuario;
import adcsistemas.loja_comprebem.repository.UsuarioRepository;

@Service
public class TarefaAutomatizadaService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SendEmailService sendEmailService;

	//@Scheduled(initialDelay = 2000, fixedDelay = 86400000) /*Roda a cada 24 horas*/
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /* Roda as 11 todas os dias horario de Sao Paulo*/
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaExpirada();
		
		for (Usuario usuario : usuarios) {
			
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/><br/>");
			msg.append("O acesso expirou!, sua senha precisar ser trocada.").append("<br/><br>");
			msg.append("<b>Realize a troca de sua senha para continuar a utilizar nossos serviços.</b><br/><br/>");
			msg.append("<b>Atenciosamente Loja CompreBem!<br/>");
			
			sendEmailService.enviarEmailHtml("Re-Validação de Acesso", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
		}
		
	}
}
