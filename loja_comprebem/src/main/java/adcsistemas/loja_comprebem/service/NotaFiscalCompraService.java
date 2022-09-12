package adcsistemas.loja_comprebem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioCompraNotaFiscalDTO;

@Service
public class NotaFiscalCompraService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<RelatorioCompraNotaFiscalDTO> gerarrelatorioProdutoCompNotaFiscal(
			RelatorioCompraNotaFiscalDTO relatorioCompraNotaFiscalDTO) {
		
		List<RelatorioCompraNotaFiscalDTO> retorno = new ArrayList<RelatorioCompraNotaFiscalDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorvendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, cfc.data_compra as dataCompra "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";
		
		sql += " cfc.data_compra >= '" + relatorioCompraNotaFiscalDTO.getDataInicial()+ "' and ";
		sql += " cfc.data_compra <= '" + relatorioCompraNotaFiscalDTO.getDataFinal()+ "'";
		
		if(!relatorioCompraNotaFiscalDTO.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + relatorioCompraNotaFiscalDTO.getCodigoNota() + " ";
		}
		
		if(!relatorioCompraNotaFiscalDTO.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + relatorioCompraNotaFiscalDTO.getCodigoNota() + " ";
		}
		
		if(!relatorioCompraNotaFiscalDTO.getNomeProduto().isEmpty()) {
			sql += " or upper(p.nome) like upper('%"+relatorioCompraNotaFiscalDTO.getNomeProduto()+"')";
		}
		
		if(!relatorioCompraNotaFiscalDTO.getNomeFornecedor().isEmpty()) {
			sql += " or upper(pj.nome) like upper('%"+relatorioCompraNotaFiscalDTO.getNomeFornecedor()+"') ";

		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatorioCompraNotaFiscalDTO.class));
		
		return retorno;
	}

}
