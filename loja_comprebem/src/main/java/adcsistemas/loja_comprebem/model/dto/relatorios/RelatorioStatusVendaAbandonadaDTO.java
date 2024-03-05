package adcsistemas.loja_comprebem.model.dto.relatorios;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RelatorioStatusVendaAbandonadaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Informe uma data inicial")
	@NotEmpty(message = "Informe a data inicial")
	private String dataInicial = "";

	@NotNull(message = "Informe uma data final")
	@NotEmpty(message = "Informe a data final")
	private String dataFinal = "";
	
	
	private String codigoProduto = ""; 
	
	private String nomeProduto = "";
	
	private String nomeCliente = "";

	private String emailCliente = "";

	private String foneCliente = "";

	private String codidoCliente = "";

	private String qtdEstoque = "";

	private String codigoVenda = "";

	private String statusVenda = "";
	
	private String valorVendaProduto = "";

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getFoneCliente() {
		return foneCliente;
	}

	public void setFoneCliente(String foneCliente) {
		this.foneCliente = foneCliente;
	}

	public String getCodidoCliente() {
		return codidoCliente;
	}

	public void setCodidoCliente(String codidoCliente) {
		this.codidoCliente = codidoCliente;
	}

	public String getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(String qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}

	public String getCodigoVenda() {
		return codigoVenda;
	}

	public void setCodigoVenda(String codigoVenda) {
		this.codigoVenda = codigoVenda;
	}

	public String getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(String statusVenda) {
		this.statusVenda = statusVenda;
	}

	public String getValorVendaProduto() {
		return valorVendaProduto;
	}

	public void setValorVendaProduto(String valorVendaProduto) {
		this.valorVendaProduto = valorVendaProduto;
	}

}
