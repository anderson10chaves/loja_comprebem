package adcsistemas.loja_comprebem.model.dto;

import adcsistemas.loja_comprebem.model.Produto;

public class ItemVendaDTO {
	
	private Long id;
	
	private Double quantidade;
	
	private Produto produto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
