package adcsistemas.loja_comprebem.model.cobranca_pagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
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
@Table(name = "cobranca_pagamento")
@SequenceGenerator(name = "seq_cobranca_pagamento", sequenceName = "seq_cobranca_pagamento", allocationSize = 1, initialValue = 1)
public class CobrancaPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cobranca_pagamento")
	private Long id;
	
	private String code = "";
	
	private String link = "";
	
	private String checkoutUrl = "";
	
	private boolean quitado = false;
	
	private String dataVencimento = "";
	
	private BigDecimal valor = BigDecimal.ZERO;
	
	private Integer recorrente = 0;
	
	/*Controle para cancelar boletos por ID*/
	private String idChrBoleto = "";
	
	/*Link da parcela boleto*/
	private String installmentLink = "";
	
	private String IdPix = "";
	
	@Column(columnDefinition = "text")
	private String payloadInBase64 = "";
	
	@Column(columnDefinition = "text")
	private String imageInBase64 = "";
	
	private String chargeICartao = "";
	
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCheckoutUrl() {
		return checkoutUrl;
	}

	public void setCheckoutUrl(String checkoutUrl) {
		this.checkoutUrl = checkoutUrl;
	}

	public boolean isQuitado() {
		return quitado;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getRecorrente() {
		return recorrente;
	}

	public void setRecorrente(Integer recorrente) {
		this.recorrente = recorrente;
	}

	public String getIdChrBoleto() {
		return idChrBoleto;
	}

	public void setIdChrBoleto(String idChrBoleto) {
		this.idChrBoleto = idChrBoleto;
	}

	public String getInstallmentLink() {
		return installmentLink;
	}

	public void setInstallmentLink(String installmentLink) {
		this.installmentLink = installmentLink;
	}

	public String getIdPix() {
		return IdPix;
	}

	public void setIdPix(String idPix) {
		IdPix = idPix;
	}

	public String getPayloadInBase64() {
		return payloadInBase64;
	}

	public void setPayloadInBase64(String payloadInBase64) {
		this.payloadInBase64 = payloadInBase64;
	}

	public String getImageInBase64() {
		return imageInBase64;
	}

	public void setImageInBase64(String imageInBase64) {
		this.imageInBase64 = imageInBase64;
	}

	public String getChargeICartao() {
		return chargeICartao;
	}

	public void setChargeICartao(String chargeICartao) {
		this.chargeICartao = chargeICartao;
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
		return Objects.hash(IdPix, chargeICartao, checkoutUrl, code, dataVencimento, empresa, id, idChrBoleto,
				imageInBase64, installmentLink, link, payloadInBase64, quitado, recorrente, valor,
				vendaCompraLojaVirtual);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CobrancaPagamento other = (CobrancaPagamento) obj;
		return Objects.equals(IdPix, other.IdPix) && Objects.equals(chargeICartao, other.chargeICartao)
				&& Objects.equals(checkoutUrl, other.checkoutUrl) && Objects.equals(code, other.code)
				&& Objects.equals(dataVencimento, other.dataVencimento) && Objects.equals(empresa, other.empresa)
				&& Objects.equals(id, other.id) && Objects.equals(idChrBoleto, other.idChrBoleto)
				&& Objects.equals(imageInBase64, other.imageInBase64)
				&& Objects.equals(installmentLink, other.installmentLink) && Objects.equals(link, other.link)
				&& Objects.equals(payloadInBase64, other.payloadInBase64) && quitado == other.quitado
				&& Objects.equals(recorrente, other.recorrente) && Objects.equals(valor, other.valor)
				&& Objects.equals(vendaCompraLojaVirtual, other.vendaCompraLojaVirtual);
	}

	
}
