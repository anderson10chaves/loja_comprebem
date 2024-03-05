package adcsistemas.loja_comprebem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.SpringApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import adcsistemas.loja_comprebem.transportadora.dto.TransportadoraDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteApiMelhorEnvio {

	public static void main(String[] args) throws Exception {
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\n"
				+ "   \"from\":{\n"
				+ "      \"postal_code\":\"01002001\"\n"
				+ "   },\n"
				+ "   \"to\":{\n"
				+ "      \"postal_code\":\"90570020\"\n"
				+ "   },\n"
				+ "   \"package\":{\n"
				+ "      \"height\":4,\n"
				+ "      \"width\":12,\n"
				+ "      \"length\":17,\n"
				+ "      \"weight\":0.3\n"
				+ "   },\n"
				+ "   \"options\":{\n"
				+ "      \"insurance_value\":1180.87,\n"
				+ "      \"receipt\":false,\n"
				+ "      \"own_hand\":false\n"
				+ "   },\n"
				+ "   \"services\":\"1,2,3,4,7,11\"\n"
				+ "}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"/api/v2/me/shipment/calculate")
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "andchaves10@icloud.com")
		  .build();

		Response response = client.newCall(request).execute();
		//System.out.println(response.body().string());
		
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		
		Iterator<JsonNode> iterator = jsonNode.iterator();
		
		List<TransportadoraDTO> transportadoraDTOs = new ArrayList<TransportadoraDTO>();
		
		
		while(iterator.hasNext()) {
			JsonNode node = iterator.next();
		
			TransportadoraDTO transportadoraDTO = new TransportadoraDTO();
			
			
			if(node.get("id") != null) {
				transportadoraDTO.setId(node.get("id").asText());
			}
			
			if(node.get("name") != null) {
				transportadoraDTO.setNome(node.get("name").asText());
			}
			
			if(node.get("price") != null) {
				transportadoraDTO.setNome(node.get("price").asText());
			}
			
			if(node.get("company") != null) {
				transportadoraDTO.setCompany(node.get("company").get("name").asText());
				transportadoraDTO.setPicture(node.get("company").get("picture").asText());
			}
			
			if(transportadoraDTO.dadosOk()) {
				transportadoraDTOs.add(transportadoraDTO);
			}
		}
		
		System.out.println(transportadoraDTOs);
	}

}
