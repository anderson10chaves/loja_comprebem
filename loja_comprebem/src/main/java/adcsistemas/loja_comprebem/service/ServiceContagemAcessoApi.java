package adcsistemas.loja_comprebem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceContagemAcessoApi {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void atualizaAcessoEndPoint() {
		jdbcTemplate.execute("begin; update tabela_acesso_endpoint set qtd_acesso_endpoint = qtd_acesso_endpoint + 1 where nome_endpoint = 'END_POINT_NOME_PESSOA_FISICA'; commit;");
		
		jdbcTemplate.execute("begin; update tabela_acesso_endpoint set qtd_acesso_endpoint = qtd_acesso_endpoint + 1 where nome_endpoint = 'END_POINT_NOME_PESSOA_JURIDICA'; commit;");
	}
}
