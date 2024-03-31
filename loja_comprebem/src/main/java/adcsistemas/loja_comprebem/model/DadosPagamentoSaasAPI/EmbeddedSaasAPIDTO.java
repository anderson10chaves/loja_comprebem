package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmbeddedSaasAPIDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<ConteudoPagamentoSaasAPIDTO> charges = new ArrayList<ConteudoPagamentoSaasAPIDTO>();

	public List<ConteudoPagamentoSaasAPIDTO> getCharges() {
		return charges;
	}

	public void setCharges(List<ConteudoPagamentoSaasAPIDTO> charges) {
		this.charges = charges;
	}
	
	
}
