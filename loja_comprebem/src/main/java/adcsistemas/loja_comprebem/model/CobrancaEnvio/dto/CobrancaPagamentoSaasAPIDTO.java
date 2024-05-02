package adcsistemas.loja_comprebem.model.CobrancaEnvio.dto;

import java.io.Serializable;

public class CobrancaPagamentoSaasAPIDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private CobrancaPagamentoChargeDTO charge = new CobrancaPagamentoChargeDTO();
	
	private CobrancaPagamentoBillingDTO billing = new CobrancaPagamentoBillingDTO();

	public CobrancaPagamentoChargeDTO getCharge() {
		return charge;
	}

	public void setCharge(CobrancaPagamentoChargeDTO charge) {
		this.charge = charge;
	}

	public CobrancaPagamentoBillingDTO getBilling() {
		return billing;
	}

	public void setBilling(CobrancaPagamentoBillingDTO billing) {
		this.billing = billing;
	}
	
	
}
