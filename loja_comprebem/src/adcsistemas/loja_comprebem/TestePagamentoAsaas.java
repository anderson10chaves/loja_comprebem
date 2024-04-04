package adcsistemas.loja_comprebem;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import adcsistemas.loja_comprebem.api_pagamentos.controller.PagamentoAssasController;
import adcsistemas.loja_comprebem.controller.ReceberPagamentoWebHookApiAsaas;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TestePagamentoAsaas extends TestCase{
	
	@Autowired
	private PagamentoAssasController pagamentoAssasController;
	
	@Autowired
	private ReceberPagamentoWebHookApiAsaas receberPagamentoWebHookApiAsaas;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Test
	public void TesterecebeNotificacaoPagamentoApiAsaas() throws Exception {

		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);
		MockMvc mockMvc = builder.build();
		
		/*ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);*/
		
		String json = new String(Files.readAllBytes(Paths.get("/Users/andersonchaves/git/loja_comprebem/loja_comprebem/src/test/java/adcsistemas/compra_frete/jsonwebhookasaas")));
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.post("/requisicaoasass/notificacaoapiassas")
				.content(json)
				.accept("application/json;charset=UTF-8")
				.contentType("application/json;charset=UTF-8"));
		
		System.out.println(retornoApi.andReturn().getRequest().getContentAsString());
	}
	
	@Test
	public void TestfinalizarCompraCartaoAsaas() throws Exception {
		pagamentoAssasController.finalizarCompraCartaoAsaas("4174010174040775", "Anderson de Oliveira Chaves",
				"833", "07",
				"2028", 2L, "34538502898",
				1, "19274000", "Travessa Alamandas",
				"106", "SP", "Rosana");
		
	}
	
}
