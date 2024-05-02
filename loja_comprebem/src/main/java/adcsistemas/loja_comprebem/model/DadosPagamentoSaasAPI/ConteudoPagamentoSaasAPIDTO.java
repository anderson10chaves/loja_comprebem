package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConteudoPagamentoSaasAPIDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String id;
	
	private String code;
	
	private String reference;
	
	private String dueDate;
	
	private String link;
	
	private String checkoutUrl;
	
	private String installmentLink;
	
	private String payNumber;
	
	private String amount;
	
	private String status;
	
	private BilletDetailsDTO billetDetails = new BilletDetailsDTO();
	
	private List<PaymentsAsaasDTO> payments = new ArrayList<PaymentsAsaasDTO>();
	
	private PixDTO pix = new PixDTO();
	
	private List<LinksSaasAPIDTO> _links = new ArrayList<LinksSaasAPIDTO>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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

	public String getInstallmentLink() {
		return installmentLink;
	}

	public void setInstallmentLink(String installmentLink) {
		this.installmentLink = installmentLink;
	}

	public String getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BilletDetailsDTO getBilletDetails() {
		return billetDetails;
	}

	public void setBilletDetails(BilletDetailsDTO billetDetails) {
		this.billetDetails = billetDetails;
	}

	public List<PaymentsAsaasDTO> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentsAsaasDTO> payments) {
		this.payments = payments;
	}

	public PixDTO getPix() {
		return pix;
	}

	public void setPix(PixDTO pix) {
		this.pix = pix;
	}

	public List<LinksSaasAPIDTO> get_links() {
		return _links;
	}

	public void set_links(List<LinksSaasAPIDTO> _links) {
		this._links = _links;
	}
	
	
}
