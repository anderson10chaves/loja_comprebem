package adcsistemas.loja_comprebem.model.EmailMarketing.dto;

import java.io.Serializable;

public class EmailContent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	  private String html;
	  
	  private String plain;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getPlain() {
		return plain;
	}

	public void setPlain(String plain) {
		this.plain = plain;
	}

}
