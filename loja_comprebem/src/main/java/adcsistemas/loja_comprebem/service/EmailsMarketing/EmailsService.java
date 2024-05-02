package adcsistemas.loja_comprebem.service.EmailsMarketing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import adcsistemas.loja_comprebem.ApiTokenIntegracao;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.EmailFromField;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.EmailMarketing;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.EmailNewsLetter;
import adcsistemas.loja_comprebem.model.EmailMarketing.dto.NovoUsuarioEmail;
import adcsistemas.loja_comprebem.service.SSLClient.HostIgnoringClient;

@Service
public class EmailsService {
	
	public List<EmailMarketing> listaCampanhaEmails() throws Exception {
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL).hostIgnoringClient();

		String json = client.resource(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL + "campaigns")
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE_EMAIL)
				.get(String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		List<EmailMarketing> list = objectMapper.readValue(json, new TypeReference<List<EmailMarketing>>() {
		});
		
		return list;
	}
	
	public String criarEmailContato(NovoUsuarioEmail novoUsuarioEmail) throws Exception {

		String json = new ObjectMapper().writeValueAsString(novoUsuarioEmail);

		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL).hostIgnoringClient();

		com.sun.jersey.api.client.WebResource webResource = client
				.resource(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL + "contacts");

		ClientResponse clientResponse = webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE_EMAIL)
				.post(ClientResponse.class, json);

		String retorno = clientResponse.getEntity(String.class);
		
				
		if(clientResponse.getStatus() == 202) {
			retorno = "Cadastro de email com sucesso";
		} if (clientResponse.getStatus() == 400) {
			retorno = "Email já existe no cadastro";
		}		
		clientResponse.close();
		return retorno;
		
	}
	
	public String enviarEmail(String idCampanha, String nomeEmail, String msg) throws Exception {
	
		EmailNewsLetter emailNewsLetter = new EmailNewsLetter();

		emailNewsLetter.getSendSettings().getSelectedCampaigns().add(idCampanha);/* O0B0X Campanha de destino e lista de emails*/
		emailNewsLetter.setSubject(nomeEmail);
		emailNewsLetter.setName(emailNewsLetter.getSubject());
		emailNewsLetter.getFromField().setFromFieldId("rxOp7");/* ID email para resposta */
		emailNewsLetter.getReplyTo().setFromFieldId("rxOp7");/* ID do email do remetente */
		emailNewsLetter.getCampaign().setCampaignId("O0B0X");/* Campanha de origem */

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate hoje = LocalDate.now();
		LocalDate amanha = hoje.plusDays(1);
		String dataEnvio = amanha.format(dateTimeFormatter);

		emailNewsLetter.setSendOn(dataEnvio + "T08:00:00-03:00");
		emailNewsLetter.getContent().setHtml(msg);

		String json = new ObjectMapper().writeValueAsString(emailNewsLetter);

		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL).hostIgnoringClient();

		com.sun.jersey.api.client.WebResource webResource = client
				.resource(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL + "newsletters");

		ClientResponse clientResponse = webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE_EMAIL)
				.post(ClientResponse.class, json);

		if(clientResponse.getStatus() == 201) {
		String retorno = clientResponse.getEntity(String.class);
		
			retorno = "Enviado com sucesso!";
		} else {
			return clientResponse.toString();
		}
		
		clientResponse.close();
		return json;
		
		
	}
	
	public List<EmailMarketing> listaEmail() throws Exception{
		
		
		Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL).hostIgnoringClient();

		com.sun.jersey.api.client.WebResource webResource = client
				.resource(ApiTokenIntegracao.URL_GET_RESPONSE_EMAIL + "from-fields");
		
		String clientResponse = webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE_EMAIL)
				.get(String.class);

		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		
		String emailMarketing = clientResponse;
		
		EmailMarketing emailMarketings = objectMapper.readValue(clientResponse, new TypeReference<EmailMarketing>() {
		});
		
		
		return listaEmail();
	}

}
