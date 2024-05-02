package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificacaoPagamentoApiAsaasDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String event = "";
	
	private PaymentsAsaasDTO payment = new PaymentsAsaasDTO();

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	public PaymentsAsaasDTO getPayment() {
		return payment;
	}

	public void setPayment(PaymentsAsaasDTO payment) {
		this.payment = payment;
	}

	public String statusPagamento() {
		return getPayment().getStatus();
	}
	
	public String idFatura() {
		return payment.getId();
	}
	
	public Boolean boletoPixFaturaPaga() {
		return statusPagamento().equalsIgnoreCase("CONFIRMED") || statusPagamento().equalsIgnoreCase("RECEIVED");
	}	
	

}
