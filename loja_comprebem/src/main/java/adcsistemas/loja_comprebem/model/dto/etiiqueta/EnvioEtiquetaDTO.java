package adcsistemas.loja_comprebem.model.dto.etiiqueta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnvioEtiquetaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String service;
	
	private String agency;
	
	private ApiFromEnvioEtiquetaDTO from = new ApiFromEnvioEtiquetaDTO();
	
	private ApiToEnvioEtiquetaDTO to = new ApiToEnvioEtiquetaDTO();
	
	
	List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();
	
	List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

	private OptionsEnvioEtiquetaDTO options = new OptionsEnvioEtiquetaDTO();
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public ApiFromEnvioEtiquetaDTO getFrom() {
		return from;
	}

	public void setFrom(ApiFromEnvioEtiquetaDTO from) {
		this.from = from;
	}

	public ApiToEnvioEtiquetaDTO getTo() {
		return to;
	}

	public void setTo(ApiToEnvioEtiquetaDTO to) {
		this.to = to;
	}

	public List<ProductsEnvioEtiquetaDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsEnvioEtiquetaDTO> products) {
		this.products = products;
	}

	public List<VolumesEnvioEtiquetaDTO> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<VolumesEnvioEtiquetaDTO> volumes) {
		this.volumes = volumes;
	}

	public OptionsEnvioEtiquetaDTO getOptions() {
		return options;
	}

	public void setOptions(OptionsEnvioEtiquetaDTO options) {
		this.options = options;
	}
	
	
	

}
