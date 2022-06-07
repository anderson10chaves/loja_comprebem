package adcsistemas.loja_comprebem;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.controller.PessoaController;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
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
		pessoaJuridica.setEmail("testecadastro@gmail.com");
		pessoaJuridica.setTipoPessoa("Juridica");
		pessoaJuridica.setTelefone("(18)98106-6022");
		pessoaJuridica.setInscEstadual("5457578878888");
		pessoaJuridica.setNomeFantasia("testes");
		pessoaJuridica.setRazaoSocial("Testando");
		pessoaJuridica.setEmpresa(pessoaJuridica);
		
		pessoaController.salvarPj(pessoaJuridica);
		
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

}
