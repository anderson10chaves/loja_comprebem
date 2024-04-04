package adcsistemas.loja_comprebem;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;

import adcsistemas.loja_comprebem.model.EmailMarketing.dto.EmailCampanha;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.EmailMarketing;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.NovoUsuarioEmail;
import adcsistemas.loja_comprebem.service.EmailsMarketing.EmailsService;
import adcsistemas.loja_comprebem.service.SSLClient.HostIgnoringClient;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class EmailTeste {
	
	@Autowired
	private EmailsService emailsService;

	@Test
	public void testeEmailMarketingCampanha() throws Exception {

		List<EmailMarketing> list = emailsService.listaCampanhaEmails();

		for (EmailMarketing emailMarketing : list) {
			System.out.println(emailMarketing);
			System.out.println("-------------------------");
		}
	}

	@Test
	public void CriaEmailContato() throws Exception {

		NovoUsuarioEmail novoUsuarioEmail = new NovoUsuarioEmail();
		novoUsuarioEmail.setName("Hudson Chaves");
		novoUsuarioEmail.setEmail("suporteadcsistemas@gmail.com");

		EmailCampanha campanha = new EmailCampanha();
		campanha.setCampaignId("O0B0X");
		campanha.setCampaignId(campanha.getCampaignId());

		String retorno = emailsService.criarEmailContato(novoUsuarioEmail);
		
		System.out.println(retorno);
	}

	@Test
	public void testEnviaEmailGetResponse() throws Exception {

		String retorno = emailsService.enviarEmail("O0B0X", "Novo Teste de Email", "Novo email de testes");
		
		System.out.println(retorno);
	}
	
	@Test
	public void listaCampanhaEmails() throws Exception {

		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL).hostIgnoringClient();

		com.sun.jersey.api.client.WebResource webResource = client
				.resource(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL + "from-fields");
		
		String clientResponse = webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE_EMAIL)
				.get(String.class);

		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		
		
		System.out.println(clientResponse);
		
		
		
	}
}
