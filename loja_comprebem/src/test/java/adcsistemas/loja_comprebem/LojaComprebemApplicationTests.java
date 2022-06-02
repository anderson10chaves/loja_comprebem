package adcsistemas.loja_comprebem;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import adcsistemas.loja_comprebem.controller.AcessoController;
import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.repository.AcessoRepository;
import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojaComprebemApplication.class)
class LojaComprebemApplicationTests extends TestCase {
	
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	
	@Autowired
	private WebApplicationContext wac;
	
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_COMPRADOR");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
				.content(objectMapper.writeValueAsString(acesso))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da API" + retornoApi.andReturn().getResponse().getContentAsString());
		
		/*Converter o retorno da api para obejto de acesso*/
		
		Acesso objetoRetorno = objectMapper.
				readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				Acesso.class);
		
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
		
	}
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso")
				.content(objectMapper.writeValueAsString(acesso))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da API" + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso excluÃ­do com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
	}
	
	@Test
	public void testRestApiDeleteAcessoId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_DELETE_ACESSO_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcessoId/" + acesso.getId())
				.content(objectMapper.writeValueAsString(acesso))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da API" + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso excluÃ­do com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
	}
	
	@Test
	public void testRestApiConsultaAcessoId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_CONSULTA_ACESSO_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/consultaAcessoId/" + acesso.getId())
				.content(objectMapper.writeValueAsString(acesso))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		
		
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		
		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		
		assertEquals(acesso.getId(), acessoRetorno.getId());
		
	}
	
	@Test
	public void testRestApiConsultaAcessoDesc() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_CONSULTA_ACESSO_DESC");
		
		//acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/consultaAcessoDesc/ACESSO_DESC")
				.content(objectMapper.writeValueAsString(acesso))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		List<Acesso> retornoApiList = objectMapper.
					readValue(retornoApi.andReturn().getResponse().getContentAsString(),
							new TypeReference<List<Acesso>>() {});
		
		assertEquals(4, retornoApiList.size());
		
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		
		//acessoRepository.deleteById(acesso.getId());
		
	}
	
	@Test
	public void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		
		//acesso = acessoController.salvarAcesso(acesso).getBody();
		
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
		
		//acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ROLE_USER".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}

}
