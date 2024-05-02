package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

public class CobrancaGeradaSaasDiscountDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double value;
	
	private String limitDate;
	
	private Integer dueDateLimitDays;
	
	private String type;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public Integer getDueDateLimitDays() {
		return dueDateLimitDays;
	}

	public void setDueDateLimitDays(Integer dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
