package adcsistemas.loja_comprebem.model.dto;

import java.io.Serializable;

public class AvaliacaoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String descricao;
	
	private Integer nota;
	
	private Long pessoa;
	
	private Long empresa;
	
	private Long produto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public Long getPessoa() {
		return pessoa;
	}

	public void setPessoa(Long pessoa) {
		this.pessoa = pessoa;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public Long getProduto() {
		return produto;
	}

	public void setProduto(Long produto) {
		this.produto = produto;
	}
	
	
}
