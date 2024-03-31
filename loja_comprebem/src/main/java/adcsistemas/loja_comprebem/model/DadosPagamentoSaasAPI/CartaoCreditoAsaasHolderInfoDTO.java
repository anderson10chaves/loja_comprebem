package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;

public class CartaoCreditoAsaasHolderInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String email;
	
	private String cpfCnpj;
	
	private String postalCode;
	
	private String addresNumber;
	
	private String addresComplement;
	
	private String phone;
	
	private String mobilePhone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddresNumber() {
		return addresNumber;
	}

	public void setAddresNumber(String addresNumber) {
		this.addresNumber = addresNumber;
	}

	public String getAddresComplement() {
		return addresComplement;
	}

	public void setAddresComplement(String addresComplement) {
		this.addresComplement = addresComplement;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
}
