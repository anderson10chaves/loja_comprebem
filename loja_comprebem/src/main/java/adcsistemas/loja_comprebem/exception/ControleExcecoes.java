package adcsistemas.loja_comprebem.exception;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import adcsistemas.loja_comprebem.model.dto.ObjetoErroDTO;
import adcsistemas.loja_comprebem.service.SendEmailService;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {
	
	@Autowired
	private SendEmailService sendEmailService;

	@ExceptionHandler({ ExceptionLojaComprebem.class })
	public ResponseEntity<Object> handleExceptionCustom(ExceptionLojaComprebem ex) {
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		objetoErroDTO.setError(ex.getMessage());
		objetoErroDTO.setCode(HttpStatus.OK.toString());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
	}

	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		}
		
		else if (ex instanceof HttpMessageNotReadableException) {

			msg = "Não contém dados para ser salvo";

		} else {
			msg = ex.getMessage();
		}

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

		ex.printStackTrace();
		try {
			sendEmailService.enviarEmailHtml("Erro na Loja CompreBem - Grave",
					ExceptionUtils.getStackTrace(ex), "andchaves10@icloud.com");
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if (ex instanceof DataIntegrityViolationException) {
			msg = "Erro de Integridade do Banco: "
					+ ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		} else

		if (ex instanceof ConstraintViolationException) {
			msg = "Erro de chave Estrangeira: "
					+ ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		} else

		if (ex instanceof SQLException) {
			msg = "Erro de SQL do banco de dados: " + ((SQLException) ex).getCause().getCause().getMessage();
		} else {
			msg = ex.getMessage();
		}

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		ex.printStackTrace();
		try {
			sendEmailService.enviarEmailHtml("Erro na Loja CompreBem - Grave",
					ExceptionUtils.getStackTrace(ex), "andchaves10@icloud.com");
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
