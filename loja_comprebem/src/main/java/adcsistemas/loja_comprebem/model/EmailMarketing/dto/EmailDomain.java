package adcsistemas.loja_comprebem.model.EmailMarketing.dto;

import java.io.Serializable;

public class EmailDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private String DKIMWarning;
	
	private String hasDMARCWarning;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDKIMWarning() {
		return DKIMWarning;
	}

	public void setDKIMWarning(String dKIMWarning) {
		DKIMWarning = dKIMWarning;
	}

	public String getHasDMARCWarning() {
		return hasDMARCWarning;
	}

	public void setHasDMARCWarning(String hasDMARCWarning) {
		this.hasDMARCWarning = hasDMARCWarning;
	}
	
	
}
