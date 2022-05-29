package adcsistemas.loja_comprebem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import adcsistemas.loja_comprebem.controller.AcessoController;
import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.service.AcessoService;

@SpringBootTest(classes = LojaComprebemApplication.class)
class LojaComprebemApplicationTests {
	
	@Autowired
	private AcessoService acessoService;

	//@Autowired
	//private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
	}

}
