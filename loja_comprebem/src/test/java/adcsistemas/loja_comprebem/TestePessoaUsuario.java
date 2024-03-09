package adcsistemas.loja_comprebem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.TokenMercadoPago.service.AccessTokenMercadoPagoService;
import adcsistemas.loja_comprebem.TokenMercadoPago.service.MercadoPagoBoletoAPIservice;
import adcsistemas.loja_comprebem.controller.PessoaController;
import adcsistemas.loja_comprebem.enums.TipoEndereco;
import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Empresa;
import adcsistemas.loja_comprebem.model.Endereco;
import adcsistemas.loja_comprebem.model.PessoaFisica;
import adcsistemas.loja_comprebem.repository.EmpresaRepository;


@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TestePessoaUsuario {
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private EmpresaRepository pessoaRepository;
	
	@Autowired
	private MercadoPagoBoletoAPIservice mercadoPagoBoletoAPIservice;
	
	@Test
	public void TesteToken() throws Exception {
		
		mercadoPagoBoletoAPIservice.obterToken();
	}
	
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionLojaComprebem {

		Empresa empresa = new Empresa();

		empresa.setCnpj("98.947.729/0001-74");
		empresa.setNome("Denise Mendonça Chaves");
		empresa.setEmail("andchaves10@icloud.com");
		empresa.setTipoPessoa("Empresa");
		empresa.setTelefone("(18)98106-6022");
		empresa.setInscEstadual("5457578878888");
		empresa.setNomeFantasia("testes");
		empresa.setRazaoSocial("Testando");
		empresa.setEmpresa(empresa);

		Endereco endereco1 = new Endereco();
		
		
		empresa.setId(empresa.getId());
		endereco1.setBairro("Centro");
		endereco1.setCep("19274-000");
		endereco1.setCidade("Primavera");
		endereco1.setComplemento("Quadra 150");
		endereco1.setEmpresa(empresa);
		endereco1.setNumero("84");
		endereco1.setEmpresa(empresa.getEmpresa());
		endereco1.setPessoa(empresa.getEmpresa());
		endereco1.setLogradouro("Rua Diamantina");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("SP");

		Endereco endereco2 = new Endereco();

		empresa.setId(empresa.getId());
		endereco2.setBairro("Centro");
		endereco2.setCep("19274-000");
		endereco2.setCidade("Primavera");
		endereco2.setComplemento("Quadra 31");
		endereco2.setEmpresa(empresa);
		endereco2.setNumero("135");
		endereco2.setEmpresa(empresa.getEmpresa());
		endereco2.setPessoa(empresa.getEmpresa());
		endereco2.setLogradouro("Travessa 1470");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("SP");

		empresa.getEnderecos().add(endereco1);
		empresa.getEnderecos().add(endereco2);

		empresa = pessoaController.salvarEmpresa(empresa).getBody();

		assertEquals(true, empresa.getId() > 0);

		for (Endereco endereco : empresa.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		assertEquals(2, empresa.getEnderecos().size());

	}
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaComprebem {
		
		Empresa empresa = pessoaRepository.existeCnpj("98.947.729/0001-74");
		
		  PessoaFisica pessoaFisica = new PessoaFisica();
		  
		  pessoaFisica.setCpf("358.657.778-82");
		  pessoaFisica.setNome("Anderson Chaves");
		  pessoaFisica.setEmail("andchaves10@gmail.com");
		  pessoaFisica.setTipoPessoa("Fisica");
		  pessoaFisica.setTelefone("(18)98110-1517");
		  pessoaFisica.setEmpresa(empresa.getEmpresa());
		  
		  Endereco endereco1 = new Endereco();

		  
			endereco1.setBairro("Centro");
			endereco1.setCep("19274-000");
			endereco1.setCidade("Primavera");
			endereco1.setComplemento("Quadra 150");
			endereco1.setNumero("84");
			endereco1.setPessoa(pessoaFisica);
			endereco1.setLogradouro("Rua Diamantina");
			endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
			endereco1.setUf("SP");
			endereco1.setEmpresa(empresa);

			Endereco endereco2 = new Endereco();

			endereco2.setBairro("Centro");
			endereco2.setCep("19274-000");
			endereco2.setCidade("Primavera");
			endereco2.setComplemento("Quadra 31");
			endereco2.setNumero("135");
			endereco2.setPessoa(pessoaFisica);
			endereco2.setLogradouro("Travessa 1470");
			endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
			endereco2.setUf("SP");
			endereco2.setEmpresa(empresa);

			pessoaFisica.getEnderecos().add(endereco1);
			pessoaFisica.getEnderecos().add(endereco2);

			pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();

			assertEquals(true, pessoaFisica.getId() > 0);

			for (Endereco endereco : pessoaFisica.getEnderecos()) {
				assertEquals(true, endereco.getId() > 0);
			}
			assertEquals(2, pessoaFisica.getEnderecos().size());
		 
	}

}
