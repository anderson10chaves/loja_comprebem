package adcsistemas.loja_comprebem.enums;

public enum TipoEndereco {
	
	COBRANCA("COBRANÇA"),
	ENTREGA("ENTREGA");
	
	private String descricao;
	
	private TipoEndereco(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

}
