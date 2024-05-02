package adcsistemas.loja_comprebem.exception;

import java.util.ArrayList;
import java.util.List;

public class ErroResponseApiAsaas {

	List<ObjetoErroResponseApiAsaas> errors = new ArrayList<ObjetoErroResponseApiAsaas>();
	
	public String listaErros() {
		StringBuilder builder = new StringBuilder();
		
		for(ObjetoErroResponseApiAsaas  error : errors) {
			builder.append(error.getDescription()).append(" - Code: - ").append(error.getCode()).append("\n");
		}
		
		return builder.toString();
	}

	public List<ObjetoErroResponseApiAsaas> getErrors() {
		return errors;
	}

	public void setErrors(List<ObjetoErroResponseApiAsaas> errors) {
		this.errors = errors;
	}
	

}
