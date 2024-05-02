package adcsistemas.loja_comprebem.model.dto;

import java.io.Serializable;

public class ObjetoMsgEmailRecSenha implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public ObjetoMsgEmailRecSenha(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
