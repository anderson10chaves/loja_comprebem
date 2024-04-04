package adcsistemas.loja_comprebem;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoEnvioDTO;
import adcsistemas.loja_comprebem.service.TokenAssasService.PagamentoSaasApiService;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TesteGerarChavePix {
	
	@Autowired
	private PagamentoSaasApiService pagamentoSaasApiService;
	
	@Test
	public void TestegerarBoletosApiAsaas() throws Exception {
		
		CobrancaPagamentoEnvioDTO dados = new CobrancaPagamentoEnvioDTO();
		dados.setEmail("andchaves10@gmail.com");
		dados.setPayerName("Anderson de Oliveira Chaves");
		dados.setPayerCpfCnpj("34538502898");
		dados.setPayerPhone("18981436774");
		dados.setIdVenda(2L);
		
		String retorno = pagamentoSaasApiService.gerarBoletosApiAsaas(dados);
		
		System.out.println(retorno);
	}
	
	@Test
	public void TesteBuscaClientPessoaApiAssas() throws Exception {
		
		CobrancaPagamentoEnvioDTO dados = new CobrancaPagamentoEnvioDTO();
		dados.setEmail("andchaves10@gmail.com");
		dados.setPayerName("Anderson de Oliveira Chaves");
		dados.setPayerCpfCnpj("34538502898");
		dados.setPayerPhone("18981436774");
		
		String customer_id = pagamentoSaasApiService.buscaPessoaFisicaApiAsaas(dados);
		
		assertEquals("cus_000080840954", customer_id); 
	}	
	
	@Test
	public void TesteChavePixAssas() throws Exception {
		
		String chaveAssasApi = pagamentoSaasApiService.criarChavePixAssas();
		
		System.out.println(chaveAssasApi);
	}

}
