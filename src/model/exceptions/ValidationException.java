package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//guardar erro de cada campo do formul�rio. primeiro String nome do campo e segundo msg de erro
	//cole��o carregar todos erros poss�veis
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return errors;
	}
	
	//adicionar error na cole��o nome e msg
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}

}
