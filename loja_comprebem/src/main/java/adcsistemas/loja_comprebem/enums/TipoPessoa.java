package adcsistemas.loja_comprebem.enums;

public enum TipoPessoa {

	Empresa("Empresa"),
	JURIDICA_FORNECEDOR("Juridica_Fornecedor"),
	FISICA("Fisica");

	private String descricao;
	
	private TipoPessoa(String descricao) {
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
