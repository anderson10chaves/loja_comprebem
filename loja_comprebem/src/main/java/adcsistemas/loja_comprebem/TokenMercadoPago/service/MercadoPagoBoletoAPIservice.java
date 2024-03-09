package adcsistemas.loja_comprebem.TokenMercadoPago.service;

import java.io.Serializable;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import adcsistemas.loja_comprebem.model.AccessTokenMercadoPagoClient;
import adcsistemas.loja_comprebem.repository.AccessTokenMercadoPagoRepository;
import adcsistemas.loja_comprebem.service.MercadoPago.service.HostIgnoringClient;

@Service
public class MercadoPagoBoletoAPIservice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AccessTokenMercadoPagoService accessTokenMercadoPagoService;
	
	@Autowired
	private AccessTokenMercadoPagoRepository accessTokenMercadoPagoRepository;
	
	public AccessTokenMercadoPagoClient obterToken() throws Exception {
		
		AccessTokenMercadoPagoClient accessTokenMercadoPagoClient = accessTokenMercadoPagoService.obterTokenMercadoPago();
		
		if(accessTokenMercadoPagoClient == null || accessTokenMercadoPagoClient != null && accessTokenMercadoPagoClient.expirado()) {
			
			String client_id = "3443042677739303";
			String client_secret = "snvvGqHqGP2FOwQowdE4E4dBq6nOG5bo";
			String access_token = "TEST-6a95ac3c-e449-4fff-911e-241d3ea0c089";
			//String redirect_uri = "https://www.mercadopago.com.br/developers/panel/app/3443042677739303";
			Boolean test_token = true;
			String granttype = "client_credentials";
			
			Client client = new HostIgnoringClient("https://api.mercadopago.com/oauth/token/")
					.hostIgnoringClient();
			
			WebResource webResource = client.resource("https://api.mercadopago.com/oauth/token?client_secret=snvvGqHqGP2FOwQowdE4E4dBq6nOG5bo&access_token=TEST-3443042677739303-030804-81113c9c04b7d96cbb8f84dfd14b9e3e-142862696&redirect_uri=https://www.mercadopago.com.br/developers/panel/app/3443042677739303&test_token=true&grant_type=client_credentials&client_id=3443042677739303");
			
			String basicChave = client_id + ":" + client_secret;
			String token_autenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());
			
			ClientResponse clientResponse = webResource
					.accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", "Basic " + token_autenticacao)
					.post(ClientResponse.class);
			
			if(clientResponse.getStatus() == 200) {
				accessTokenMercadoPagoRepository.deleteAll();
				accessTokenMercadoPagoRepository.flush();
				
				AccessTokenMercadoPagoClient accessTokenMercadoPagoClient2 = clientResponse.getEntity(AccessTokenMercadoPagoClient.class);
				accessTokenMercadoPagoClient2.setAccess_token(token_autenticacao);
				
				accessTokenMercadoPagoClient2 = accessTokenMercadoPagoRepository.saveAndFlush(accessTokenMercadoPagoClient2);
				
				return accessTokenMercadoPagoClient2;
			}else {
				return null;
			} 
			
		}else {
			return accessTokenMercadoPagoClient;
		}
	}
	
	

}
