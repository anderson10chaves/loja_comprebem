package adcsistemas.loja_comprebem.transportadora.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultaApiFreteDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private ApiFromDTO from;
	
	private ApiToDoDTO to;
	
	private List<ProductsDTO> products = new ArrayList<ProductsDTO>();

	public ApiFromDTO getFrom() {
		return from;
	}

	public void setFrom(ApiFromDTO from) {
		this.from = from;
	}

	public ApiToDoDTO getTo() {
		return to;
	}

	public void setTo(ApiToDoDTO to) {
		this.to = to;
	}

	public List<ProductsDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsDTO> products) {
		this.products = products;
	} 
	
	
	
	

}
