package adcsistemas.loja_comprebem.model.EmailMarketing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NovoUsuarioEmail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String email;
	
	private String datOfCycle = "0";
	
	private String scoring;
	
	private String ipAddress;
	
	private EmailCampanha campaign = new EmailCampanha();
	
	private List<String> tags = new ArrayList<String>();
	
	private List<EmailCustomFieldValues> customFieldValues = new ArrayList<EmailCustomFieldValues>();

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

	public String getDatOfCycle() {
		return datOfCycle;
	}

	public void setDatOfCycle(String datOfCycle) {
		this.datOfCycle = datOfCycle;
	}

	public String getScoring() {
		return scoring;
	}

	public void setScoring(String scoring) {
		this.scoring = scoring;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public EmailCampanha getCampaign() {
		return campaign;
	}

	public void setCampaign(EmailCampanha campaign) {
		this.campaign = campaign;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<EmailCustomFieldValues> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(List<EmailCustomFieldValues> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

}
