package adcsistemas.loja_comprebem.model;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "access_token_asaas_client")
@SequenceGenerator(name = "seq_access_token_asaas_client", sequenceName = "seq_access_token_asaas_client", allocationSize = 1, initialValue = 1)
public class AccessTokenAssasClient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access_token_asaas_client")
	private Long id;
	
	private String access_token;
	
	private String token_type;
	
	private String expires_in;
	
	private String scope;
	
	private String user_id;
	
	//indica se a aplicação está em modo de produção ou de teste.
	private Boolean test_token = true;
	
	private String token_acesso;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = Calendar.getInstance().getTime();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Boolean getTest_token() {
		return test_token;
	}

	public void setTest_token(Boolean test_token) {
		this.test_token = test_token;
	}

	public String getToken_acesso() {
		return token_acesso;
	}

	public void setToken_acesso(String token_acesso) {
		this.token_acesso = token_acesso;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	
	public boolean expirado() {
		Date dataAtual = Calendar.getInstance().getTime();
		
		Long tempo = dataAtual.getTime() - this.dataCadastro.getTime();
		
		Long minutos = (tempo / 15552000) / 180;
		
		if (minutos.intValue() > 179) {
			return true;
		}
				return false;
	}
}
