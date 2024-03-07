package adcsistemas.loja_comprebem;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteApiMelhorEnvio {

	public static void main(String[] args) throws Exception {
		
		/**
		 * Listar transportadoras
		 */
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/agencies?company=2&country=BR&state=SP&city=ROSANA")
		  .get()
		  .addHeader("accept", "application/json")
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();
		
		
		System.out.println(response.body().string());
		
		
		/**
		 * Insere as etiquetas de frete
		 */
		/*OkHttpClient client = new OkHttpClient().newBuilder().build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"name\":\"Anderson Chaves\",\"document\":\"34538502898\",\"address\":\"Travessa Alamandas\",\"complement\":\"Quadra 92\",\"number\":\"106\",\"city\":\"Rosana\",\"country_id\":\"BR\",\"postal_code\":\"19274000\",\"state_abbr\":\"SP\",\"note\":\"skdlsdjsadkj\"},\"to\":{\"name\":\"Denise Chaves\",\"document\":\"35865777882\",\"postal_code\":\"06728-080\",\"address\":\"Rua Barra Mansa\",\"complement\":\"Esmeralda Park (Caucaia do Alto)\",\"number\":\"557\",\"city\":\"Cotia\",\"country_id\":\"BR\",\"state_abbr\":\"SP\",\"note\":\"dsajdjs\"},\"options\":{\"receipt\":false,\"own_hand\":false,\"reverse\":false,\"non_commercial\":false},\"service\":30,\"agency\":48,\"products\":[{\"name\":\"Iphone\",\"quantity\":\"1\",\"unitary_value\":\"5\"}],\"volumes\":[{\"height\":2,\"width\":2,\"length\":2,\"weight\":0.03}]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"/api/v2/me/cart")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "+ ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();;
		
		System.out.println(response.body().string());*/
		
		
		/**
		 * Faz a compra do frete para etiqueta
		 */
		/*OkHttpClient client = new OkHttpClient().newBuilder().build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9b7da83d-75e9-4316-ac7f-b31eb8c029c8\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "/api/v2/me/shipment/checkout")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());*/
		
		
		/**
		 * Gera as etiquetas
		 */
		
		/*OkHttpClient client = new OkHttpClient().newBuilder().build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9b7da83d-75e9-4316-ac7f-b31eb8c029c8\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "/api/v2/me/shipment/generate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());*/
		
		
		/**
		 * Imprime as etiquetas
		 */
		
		/*OkHttpClient client = new OkHttpClient().newBuilder().build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9b7da83d-75e9-4316-ac7f-b31eb8c029c8\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "/api/v2/me/shipment/print")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", ApiTokenIntegracao.EMAIL_RESP)
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());*/
	}
}
