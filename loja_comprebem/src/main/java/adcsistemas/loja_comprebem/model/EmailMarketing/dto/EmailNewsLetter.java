package adcsistemas.loja_comprebem.model.EmailMarketing.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class EmailNewsLetter implements Serializable {

	private static final long serialVersionUID = 1L;

	private EmailContent content = new EmailContent();
	
	private ArrayList<String> flags = new ArrayList<String>();
	
	private String name;
	
	private String type = "broadcast";
	
	private String editor = "custom";
	
	private String subject;
	
	private EmailFromField fromField = new EmailFromField();
	
	private EmailReplyTo replyTo = new EmailReplyTo();
	
	private EmailCampanha campaign = new EmailCampanha();
	
	private String sendOn;
	
	private ArrayList<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
	
	private EmailSendSettings sendSettings = new EmailSendSettings();

	public EmailContent getContent() {
		return content;
	}

	public void setContent(EmailContent content) {
		this.content = content;
	}

	public ArrayList<String> getFlags() {
		return flags;
	}

	public void setFlags(ArrayList<String> flags) {
		this.flags = flags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public EmailFromField getFromField() {
		return fromField;
	}

	public void setFromField(EmailFromField fromField) {
		this.fromField = fromField;
	}

	public EmailReplyTo getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(EmailReplyTo replyTo) {
		this.replyTo = replyTo;
	}

	public EmailCampanha getCampaign() {
		return campaign;
	}

	public void setCampaign(EmailCampanha campaign) {
		this.campaign = campaign;
	}

	public String getSendOn() {
		return sendOn;
	}

	public void setSendOn(String sendOn) {
		this.sendOn = sendOn;
	}

	public ArrayList<EmailAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<EmailAttachment> attachments) {
		this.attachments = attachments;
	}

	public EmailSendSettings getSendSettings() {
		return sendSettings;
	}

	public void setSendSettings(EmailSendSettings sendSettings) {
		this.sendSettings = sendSettings;
	}

}
