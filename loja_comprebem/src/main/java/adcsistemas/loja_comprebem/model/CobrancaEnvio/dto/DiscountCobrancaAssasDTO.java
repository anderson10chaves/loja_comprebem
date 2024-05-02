package adcsistemas.loja_comprebem.model.CobrancaEnvio.dto;

import java.io.Serializable;

public class DiscountCobrancaAssasDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private float value;
	
	private float dueDateLimitDays;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getDueDateLimitDays() {
		return dueDateLimitDays;
	}

	public void setDueDateLimitDays(float dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}

}
