package adcsistemas.loja_comprebem.api_pagamentos.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoEnvioDTO;
import adcsistemas.loja_comprebem.service.TokenAssasService.AccessTokenAsaasService;
import adcsistemas.loja_comprebem.service.TokenAssasService.PagamentoSaasApiService;

@RestController
public class CobrancaAssasController {
	
	@Autowired
	private AccessTokenAsaasService accessTokenAsaasService;
	
	@Autowired
	private PagamentoSaasApiService pagamentoSaasApiService;
	

	
	
	
	
	
	/**
	 * EndPoint referente a API JUNO ou IUGU mas não vamos usar
	 * @param cobrancaPagamentoEnvioDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/gerarBoletoPix")
	public ResponseEntity<String> gerarBoletoPix(@RequestBody @Valid CobrancaPagamentoEnvioDTO cobrancaPagamentoEnvioDTO) throws Exception {
		
		
		return new ResponseEntity<String>(pagamentoSaasApiService.gerarBoletosApi(cobrancaPagamentoEnvioDTO), HttpStatus.OK); 
		
	}
	
	/**
	 * Cancelamento Boleto Pix 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/cancelarBoletoPix")
	public ResponseEntity<String> cancelarBoletoPix(@RequestBody @Valid String code) throws Exception {
		
		
		return new ResponseEntity<String>(pagamentoSaasApiService.cancelaBoletoPix(code), HttpStatus.OK); 
		
	}

}
