package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

public class LinksSaasAPIDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SelfSaasAPIDTO self = new SelfSaasAPIDTO();

	public SelfSaasAPIDTO getSelf() {
		return self;
	}

	public void setSelf(SelfSaasAPIDTO self) {
		this.self = self;
	}

}
