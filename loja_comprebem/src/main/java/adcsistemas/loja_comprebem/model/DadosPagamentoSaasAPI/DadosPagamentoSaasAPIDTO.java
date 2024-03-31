package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosPagamentoSaasAPIDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private EmbeddedSaasAPIDTO _embedded = new EmbeddedSaasAPIDTO();
	
	private List<LinksSaasAPIDTO> _links = new ArrayList<LinksSaasAPIDTO>();

	public List<LinksSaasAPIDTO> get_links() {
		return _links;
	}

	public void set_links(List<LinksSaasAPIDTO> _links) {
		this._links = _links;
	}

	public EmbeddedSaasAPIDTO get_embedded() {
		return _embedded;
	}

	public void set_embedded(EmbeddedSaasAPIDTO _embedded) {
		this._embedded = _embedded;
	}

}
