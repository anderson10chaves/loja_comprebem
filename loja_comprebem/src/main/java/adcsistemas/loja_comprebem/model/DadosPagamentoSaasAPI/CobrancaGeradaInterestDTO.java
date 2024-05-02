package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

public class CobrancaGeradaInterestDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 private Double value;
     
	 private String type;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
