package adcsistemas.loja_comprebem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.controller.PessoaController;
import adcsistemas.loja_comprebem.enums.TipoEndereco;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.PessoaJuridica;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TestePessoaUsuario {
	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionLojaComprebem {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Denise Mendonça Chaves");
		pessoaJuridica.setEmail("andchaves10@icloud.com");
		pessoaJuridica.setTipoPessoa("Juridica");
		pessoaJuridica.setTelefone("(18)98106-6022");
		pessoaJuridica.setInscEstadual("5457578878888");
		pessoaJuridica.setNomeFantasia("testes");
		pessoaJuridica.setRazaoSocial("Testando");
		pessoaJuridica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		
		endereco1.setBairro("Centro");
		endereco1.setCep("19274-000");
		endereco1.setCidade("Primavera");
		endereco1.setComplemento("Quadra 150");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setNumero("84");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogradouro("Rua Diamantina");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("SP");
		
		Endereco endereco2 = new Endereco();
		
		endereco2.setBairro("Centro");
		endereco2.setCep("19274-000");
		endereco2.setCidade("Primavera");
		endereco2.setComplemento("Quadra 31");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setNumero("135");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogradouro("Travessa 1470");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("SP");
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);
		
		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
			assertEquals(2, pessoaJuridica.getEnderecos().size());
	
}
		/*
		 * PessoaFisica pessoaFisica = new PessoaFisica();
		 * 
		 * pessoaFisica.setCpf("1487845456454");
		 * pessoaFisica.setNome("Anderson Chaves");
		 * pessoaFisica.setEmail("andchaves10@gmail.com");
		 * pessoaFisica.setTipoPessoa("Fisisca");
		 * pessoaFisica.setTelefone("(18)98110-1517");
		 * pessoaFisica.setEmpresa(pessoaFisica);
		 */
		
	

}
