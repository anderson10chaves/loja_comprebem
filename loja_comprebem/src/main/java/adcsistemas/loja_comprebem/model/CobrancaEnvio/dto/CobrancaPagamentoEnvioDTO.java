package adcsistemas.loja_comprebem.model.CobrancaEnvio.dto;

import java.io.Serializable;

public class CobrancaPagamentoEnvioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String description;
	
	private String payerName;
	
	private String payerPhone;
	
	/*Valor total ou total de parcelas*/
	private String totalAmount;
	
	/*Quantidade de parcelas*/
	private String installments;
	
	/*Referencia para produto ou loja ou codigo do produto*/
	private String reference;
	
	private String payerCpfCnpj;
	
	private String email;
	
	private Long idVenda;

	public Long getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerPhone() {
		return payerPhone;
	}

	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPayerCpfCnpj() {
		return payerCpfCnpj;
	}

	public void setPayerCpfCnpj(String payerCpfCnpj) {
		this.payerCpfCnpj = payerCpfCnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
