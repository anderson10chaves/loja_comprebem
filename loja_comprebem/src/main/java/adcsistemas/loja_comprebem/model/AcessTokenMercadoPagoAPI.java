package adcsistemas.loja_comprebem.model;

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
@Table(name = "access_token_mercadopagoapi")
@SequenceGenerator(name = "seq_access_token_mercadopagoapi", sequenceName = "seq_access_token_mercadopagoapi", allocationSize = 1, initialValue = 1)
public class AcessTokenMercadoPagoAPI {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access_token_junoapi")
	private Long id;

	@Column(columnDefinition = "text")
	private String access_token;

	private String token_type;

	//Tempo fixo de expiração do access_token expresso em segundos. Por padrão o tempo de expiração é 180 dias (15552000 segundos).
	private String expires_in;

	private String scope;
	//indica se a aplicação está em modo de produção ou de teste.
	private String user_id;

	private String refresh_token;
	
	private String public_key;
	
	//indica se a aplicação está em modo de produção ou de teste.
	private Boolean live_mode;

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

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	public Boolean getLive_mode() {
		return live_mode;
	}

	public void setLive_mode(Boolean live_mode) {
		this.live_mode = live_mode;
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
		
		Long tempo = dataAtual.getTime() - this.dataCadastro.getTime(); /*Tempo entre datas*/
		
		Long minutos = (tempo / 15552000) / 180; /*difereca de minutos entra dastas e horas inicial e final*/
		
		if (minutos.intValue() > 179) {
			return true;
		}
		
		return false;
	}

}
