package adcsistemas.loja_comprebem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.ItemVendaLoja;
import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;
import adcsistemas.loja_comprebem.model.dto.ItemVendaDTO;
import adcsistemas.loja_comprebem.model.dto.VendaCompraLojaVirtualDTO;
import adcsistemas.loja_comprebem.repository.VendaCompraLojaVirtualRepository;

@Service
public class VendaService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	public void exclusaoTotalVendaBanco2(Long idVenda) {
		String sql = "begin; update vd_cp_loja_virt set excluido = true where id = " + idVenda +"; commit;";
		jdbcTemplate.execute(sql);;
	}
	
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


	public List<VendaCompraLojaVirtual> pesquisaVendaDinamicaData(String data1, String data2) throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1 = dateFormat.parse(data1);
		Date date2 = dateFormat.parse(data2);

		return vendaCompraLojaVirtualRepository.pesquisaVendaDinamicaData(date1, date2);
		
	}

	public VendaCompraLojaVirtualDTO consultaVenda(VendaCompraLojaVirtual compraLojaVirtual) {
		

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoaFisica());

		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());

		compraLojaVirtualDTO.setValorDesconto(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

		for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {

			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		return compraLojaVirtualDTO;
	}
	
	

	
}
