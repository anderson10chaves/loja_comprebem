package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.DiscountCobrancaAssasDTO;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.FineCobrancaAsaasDTO;
import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.InterestCobrancaAsaasDTO;

public class CobrancaApiAsaasCartaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customer;
	
	private String billingType;
	
	private String dueDate;
	
	private String value;
	
	private String description;
	
	private String externalReference;
	
	private float installmentValue;
	
	private Integer installmentCount;
	
	private DiscountCobrancaAssasDTO  discount = new DiscountCobrancaAssasDTO();
	
	private FineCobrancaAsaasDTO fine = new FineCobrancaAsaasDTO();
	
	private InterestCobrancaAsaasDTO interest = new InterestCobrancaAsaasDTO();
	
	private CobrancaApiAsaasCartaoCreditoDTO creditCard = new CobrancaApiAsaasCartaoCreditoDTO();
	
	private CartaoCreditoAsaasHolderInfoDTO creditCardHolderInfo = new CartaoCreditoAsaasHolderInfoDTO();
	
	private boolean postalService = false;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public float getInstallmentValue() {
		return installmentValue;
	}

	public void setInstallmentValue(float installmentValue) {
		this.installmentValue = installmentValue;
	}

	public Integer getInstallmentCount() {
		return installmentCount;
	}

	public void setInstallmentCount(Integer installmentCount) {
		this.installmentCount = installmentCount;
	}

	public DiscountCobrancaAssasDTO getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountCobrancaAssasDTO discount) {
		this.discount = discount;
	}

	public FineCobrancaAsaasDTO getFine() {
		return fine;
	}

	public void setFine(FineCobrancaAsaasDTO fine) {
		this.fine = fine;
	}

	public InterestCobrancaAsaasDTO getInterest() {
		return interest;
	}

	public void setInterest(InterestCobrancaAsaasDTO interest) {
		this.interest = interest;
	}

	public CobrancaApiAsaasCartaoCreditoDTO getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CobrancaApiAsaasCartaoCreditoDTO creditCard) {
		this.creditCard = creditCard;
	}

	public CartaoCreditoAsaasHolderInfoDTO getCreditCardHolderInfo() {
		return creditCardHolderInfo;
	}

	public void setCreditCardHolderInfo(CartaoCreditoAsaasHolderInfoDTO creditCardHolderInfo) {
		this.creditCardHolderInfo = creditCardHolderInfo;
	}

	public boolean isPostalService() {
		return postalService;
	}

	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}
	
	

}
