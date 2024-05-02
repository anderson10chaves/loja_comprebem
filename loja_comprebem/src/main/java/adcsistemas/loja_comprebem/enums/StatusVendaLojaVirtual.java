package adcsistemas.loja_comprebem.enums;

public enum StatusVendaLojaVirtual {
	
	FINALIZADA("Finalizada"),
	CANCELADO("Cancelado"),
	ABANDONADO_CARRINHO_COMPRA("Abandonou Carrinho Compra");
	
	private String descricao =  "";
	
	private StatusVendaLojaVirtual(String valor) {
		this.descricao = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

}
