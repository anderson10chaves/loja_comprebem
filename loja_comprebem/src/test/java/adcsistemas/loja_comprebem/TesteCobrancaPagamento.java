package adcsistemas.loja_comprebem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.model.CobrancaEnvio.dto.CobrancaPagamentoEnvioDTO;
import adcsistemas.loja_comprebem.service.TokenAssasService.PagamentoSaasApiService;


@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TesteCobrancaPagamento {
	
	@Autowired
	PagamentoSaasApiService pagamentoSaasApiService;
	
	@Test
	public void TestesCobrancaPixeBoletosAsaas() throws Exception {
		
		CobrancaPagamentoEnvioDTO cobrancaPagamentoEnvioDTO = new CobrancaPagamentoEnvioDTO();
		
		cobrancaPagamentoEnvioDTO.setDescription("Teste de geração de cobranca, boleto e pix");
		cobrancaPagamentoEnvioDTO.setEmail("andchaves10@gmail.com");
		cobrancaPagamentoEnvioDTO.setIdVenda(1L);
		cobrancaPagamentoEnvioDTO.setInstallments("2");
		cobrancaPagamentoEnvioDTO.setPayerCpfCnpj("34538502898");
		cobrancaPagamentoEnvioDTO.setPayerName("Hudson Chaves");
		cobrancaPagamentoEnvioDTO.setPayerPhone("18998120899");
		cobrancaPagamentoEnvioDTO.setReference("Venda da Loja Comprebem cod: 1");
		cobrancaPagamentoEnvioDTO.setTotalAmount("1000.00");
		
		String retorno = pagamentoSaasApiService.gerarBoletosApiAsaas(cobrancaPagamentoEnvioDTO);
	}
	
}
