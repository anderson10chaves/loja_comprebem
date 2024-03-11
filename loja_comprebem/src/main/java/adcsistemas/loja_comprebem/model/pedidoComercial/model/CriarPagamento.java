package adcsistemas.loja_comprebem.model.pedidoComercial.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import adcsistemas.loja_comprebem.model.Empresa;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;

@Entity
@Table(name = "criar_pagamento")
@SequenceGenerator(name = "seq_criar_pagamento", sequenceName = "seq_criar_pagamento", allocationSize = 1, initialValue = 1)
public class CriarPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_criar_pagamento")
	private Long id;
	
	private String status = "";
	
	private String status_detail = "";
	
	private String net_received_amount = "";
	
	private BigDecimal total_paid_amount = BigDecimal.ZERO;
	
	private String overpaid_amount = "";
	
	private String external_resource_url = "";
	
	private String installment_amount = "";
	
	private String financial_institution = "";
	
	private String payment_method_reference_id = "";
	
	private boolean quitado = false;
	
	private String date_of_expiration = "";
	
	
	
	@ManyToOne
	@JoinColumn(name = "venda_compra_loja_virt_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "venda_compra_loja_virt_fk"))
	private VendaCompraLojaVirtual vendaCompraLojaVirtual;
	
	@ManyToOne(targetEntity = Empresa.class)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
	private Empresa empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_detail() {
		return status_detail;
	}

	public void setStatus_detail(String status_detail) {
		this.status_detail = status_detail;
	}

	public String getNet_received_amount() {
		return net_received_amount;
	}

	public void setNet_received_amount(String net_received_amount) {
		this.net_received_amount = net_received_amount;
	}

	public BigDecimal getTotal_paid_amount() {
		return total_paid_amount;
	}

	public void setTotal_paid_amount(BigDecimal total_paid_amount) {
		this.total_paid_amount = total_paid_amount;
	}

	public String getOverpaid_amount() {
		return overpaid_amount;
	}

	public void setOverpaid_amount(String overpaid_amount) {
		this.overpaid_amount = overpaid_amount;
	}

	public String getExternal_resource_url() {
		return external_resource_url;
	}

	public void setExternal_resource_url(String external_resource_url) {
		this.external_resource_url = external_resource_url;
	}

	public String getInstallment_amount() {
		return installment_amount;
	}

	public void setInstallment_amount(String installment_amount) {
		this.installment_amount = installment_amount;
	}

	public String getFinancial_institution() {
		return financial_institution;
	}

	public void setFinancial_institution(String financial_institution) {
		this.financial_institution = financial_institution;
	}

	public String getPayment_method_reference_id() {
		return payment_method_reference_id;
	}

	public void setPayment_method_reference_id(String payment_method_reference_id) {
		this.payment_method_reference_id = payment_method_reference_id;
	}

	public boolean isQuitado() {
		return quitado;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public String getDate_of_expiration() {
		return date_of_expiration;
	}

	public void setDate_of_expiration(String date_of_expiration) {
		this.date_of_expiration = date_of_expiration;
	}

	public VendaCompraLojaVirtual getVendaCompraLojaVirtual() {
		return vendaCompraLojaVirtual;
	}

	public void setVendaCompraLojaVirtual(VendaCompraLojaVirtual vendaCompraLojaVirtual) {
		this.vendaCompraLojaVirtual = vendaCompraLojaVirtual;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date_of_expiration, empresa, external_resource_url, financial_institution, id,
				installment_amount, net_received_amount, overpaid_amount, payment_method_reference_id, quitado, status,
				status_detail, total_paid_amount, vendaCompraLojaVirtual);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CriarPagamento other = (CriarPagamento) obj;
		return Objects.equals(date_of_expiration, other.date_of_expiration) && Objects.equals(empresa, other.empresa)
				&& Objects.equals(external_resource_url, other.external_resource_url)
				&& Objects.equals(financial_institution, other.financial_institution) && Objects.equals(id, other.id)
				&& Objects.equals(installment_amount, other.installment_amount)
				&& Objects.equals(net_received_amount, other.net_received_amount)
				&& Objects.equals(overpaid_amount, other.overpaid_amount)
				&& Objects.equals(payment_method_reference_id, other.payment_method_reference_id)
				&& quitado == other.quitado && Objects.equals(status, other.status)
				&& Objects.equals(status_detail, other.status_detail)
				&& Objects.equals(total_paid_amount, other.total_paid_amount)
				&& Objects.equals(vendaCompraLojaVirtual, other.vendaCompraLojaVirtual);
	}
	
	

}
