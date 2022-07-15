package adcsistemas.loja_comprebem.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.FormaPagamento;
import adcsistemas.loja_comprebem.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarFormaPagamento")
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionLojaComprebem {

		if (formaPagamento.getId() == null) {
			List<FormaPagamento> formaPagamentos = formaPagamentoRepository.buscarFormaPagamentoDesc(formaPagamento.getDescricao().toUpperCase());

			if (!formaPagamentos.isEmpty()) {
				throw new ExceptionLojaComprebem("Forma de Pagamento já existe com essa descrição: " + formaPagamento.getDescricao());
			}
		}

		FormaPagamento formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamento);

		return new ResponseEntity<FormaPagamento>(formaPagamentoSalvo, HttpStatus.OK);
	}
}
