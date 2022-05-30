package adcsistemas.loja_comprebem;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import adcsistemas.loja_comprebem.controller.AcessoController;
import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaComprebemApplication.class)
class LojaComprebemApplicationTests extends TestCase {
	
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Test
	public void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		assertEquals("ROLE_ADMIN", acesso.getDescricao());
		
		Acesso acessoSalvo = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acessoSalvo.getId());
		
		/*Teste de deletar*/
		
		acessoRepository.deleteById(acessoSalvo.getId());
		
		acessoRepository.flush();
		
		Acesso acessoDelete = acessoRepository.findById(acessoSalvo.getId()).orElse(null);
		
		assertEquals(true, acessoDelete == null);
		
		/*Teste Query*/
		
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_USER");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ROLE_USER".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}

}
