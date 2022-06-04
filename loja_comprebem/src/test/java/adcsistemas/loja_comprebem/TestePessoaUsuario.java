package adcsistemas.loja_comprebem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.model.PessoaJuridica;
import adcsistemas.loja_comprebem.repository.PessoaRepository;
import adcsistemas.loja_comprebem.service.PessoaUserService;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TestePessoaUsuario {
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj("1487845456454");
		pessoaJuridica.setNome("Anderson Chaves");
		pessoaJuridica.setEmail("andchaves10@gmail.com");
		pessoaJuridica.setTipoPessoa("Fisisca");
		pessoaJuridica.setTelefone("(18)98110-1517");
		pessoaJuridica.setInscEstadual("5457578878888");
		pessoaJuridica.setNomeFantasia("testes");
		pessoaJuridica.setRazaoSocial("Testando");
		pessoaJuridica.setEmpresa(pessoaJuridica);
		
		pessoaRepository.save(pessoaJuridica);
		
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
