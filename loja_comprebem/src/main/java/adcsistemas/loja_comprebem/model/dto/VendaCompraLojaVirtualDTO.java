package adcsistemas.loja_comprebem.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import adcsistemas.loja_comprebem.model.PessoaFisica;

public class VendaCompraLojaVirtualDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PessoaFisica pessoaFisica;
	
	private BigDecimal valorTotal;
	
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	

}
