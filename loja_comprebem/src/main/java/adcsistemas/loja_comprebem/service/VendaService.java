package adcsistemas.loja_comprebem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void deleteVendaLogicaBanco(Long idVenda) {
		String sql = "begin; update vd_cp_loja_virt set ativo = true where id = " + idVenda +"; commit;";
		jdbcTemplate.execute(sql);
		
	}
	
	public void ativaVendaRegistroBanco(Long idVenda) {
		String sql = "begin; update vd_cp_loja_virt set ativo = false where id = " + idVenda +"; commit;";
		jdbcTemplate.execute(sql);
	}

	public void deleteVendaTotalBanco(Long idVenda) {

		String value = "begin;"
				+ "update nota_fiscal_venda set venda_compra_loja_virt_id = null where venda_compra_loja_virt_id = "+idVenda+";"
				+ "delete from nota_fiscal_venda where venda_compra_loja_virt_id = "+idVenda+";"
				+ "delete from item_venda_loja where venda_compra_loja_virt_id = "+idVenda+";"
				+ "delete from status_rastreio where venda_compra_loja_virt_id = "+idVenda+";"
				+ "delete from vd_cp_loja_virt where id = "+idVenda+";"
				+ "commit;";

		jdbcTemplate.execute(value);
	}

	

	
}
