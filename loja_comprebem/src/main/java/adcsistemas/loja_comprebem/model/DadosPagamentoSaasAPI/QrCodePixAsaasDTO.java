package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

public class QrCodePixAsaasDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String encodeImage;
	
	private String payload;
	
	private String expirationDate;
	
	private String success;

	public String getEncodeImage() {
		return encodeImage;
	}

	public void setEncodeImage(String encodeImage) {
		this.encodeImage = encodeImage;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
