package adcsistemas.loja_comprebem.model.EmailMarketing.dto;

import java.io.Serializable;

public class EmailCampanha implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String campaignId;

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	
	
}
