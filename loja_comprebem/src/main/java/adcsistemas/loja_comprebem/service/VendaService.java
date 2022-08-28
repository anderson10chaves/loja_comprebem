package adcsistemas.loja_comprebem.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;

@Service
public class VendaService {
	
	@PersistenceContext
	private EntityManager entityManager;

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

	
	@SuppressWarnings("unchecked")
	public List<VendaCompraLojaVirtual> pesquisaVendaDinamicaData(String data1, String data2) {
		
		String sql = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
				+ " where i.vendaCompraLojaVirtual.ativo = false "
				+ " and i.vendaCompraLojaVirtual.dataVenda >= '" + data1 + "'"
				+ " and i.vendaCompraLojaVirtual.dataVenda <= '" + data2 + "'";
		
		return entityManager.createQuery(sql).getResultList();
		
	}

	
	

	
}
