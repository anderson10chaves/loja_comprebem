package adcsistemas.loja_comprebem.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI.NotificacaoPagamentoApiAsaasDTO;
import adcsistemas.loja_comprebem.model.cobranca_pagamento.CobrancaPagamento;
import adcsistemas.loja_comprebem.repository.PagamentoAsaasRepository;


@Controller
@RequestMapping(value = "/requisicaoasass")
public class ReceberPagamentoWebHookApiAsaas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PagamentoAsaasRepository pagamentoAsaasRepository;
	
	@ResponseBody
	@RequestMapping(value = "/notificacaoapiassas", consumes = {"application/json;charset=UTF-8"},
	headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
	public ResponseEntity<String> recebeNotificacaoPagamentoApiAsaas(@RequestBody NotificacaoPagamentoApiAsaasDTO notificacaoPagamentoApiAsaas) {
		
		CobrancaPagamento cobrancaPagamento = pagamentoAsaasRepository.findByCode(notificacaoPagamentoApiAsaas.idFatura());
				
		if(cobrancaPagamento == null) {
			return new ResponseEntity<String>("Boleto ou Fatura não encontrada no banco de dados!", HttpStatus.OK);
		}
	
		if(cobrancaPagamento != null 
				&& notificacaoPagamentoApiAsaas.boletoPixFaturaPaga()
				&& !cobrancaPagamento.isQuitado()) {
			
			pagamentoAsaasRepository.quitarBoletoById(cobrancaPagamento.getId());
			System.out.println("Cobranca: " + cobrancaPagamento.getCode() + "foi pago");
			/*Podemos fazer qualquer regra de negocio*/
			
			return new ResponseEntity<String>("Fatura recebida do Asaas, boleto id: " + cobrancaPagamento.getId(), HttpStatus.OK);
		}else {
			System.out.println("Fatura : " + notificacaoPagamentoApiAsaas.idFatura()
			+ "Não foi processada, em aberto: " + notificacaoPagamentoApiAsaas.boletoPixFaturaPaga() 
			+ "valor pago: " + cobrancaPagamento.isQuitado());
		}
		
		return new ResponseEntity<String>("Não foi processado a fatura : " + notificacaoPagamentoApiAsaas.idFatura(), HttpStatus.OK);
	}
}
