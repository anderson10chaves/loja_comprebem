package adcsistemas.loja_comprebem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioCompraNotaFiscalDTO;
import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioProdutoAlertaEstoqueDTO;

@Service
public class NotaFiscalCompraService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Title: Histórico de Compra para venda
	 * Este Relatório consulta os produto comprados pela empresa ou loja,
	 *  referente a compra/notafiscal de novos produtos
	 * @param relatorioCompraNotaFiscalDTO
	 * @return List<RelatorioCompraNotaFiscalDTO>
	 */

	public List<RelatorioCompraNotaFiscalDTO> gerarrelatorioProdutoCompNotaFiscal(
			RelatorioCompraNotaFiscalDTO relatorioCompraNotaFiscalDTO) {
		
		List<RelatorioCompraNotaFiscalDTO> retorno = new ArrayList<RelatorioCompraNotaFiscalDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorvendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, cfc.data_compra as dataCompra "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join empresa as pj on pj.id = cfc.pessoa_id where ";
		
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
	
	public List<RelatorioProdutoAlertaEstoqueDTO> gerarRelatorioAlertaEstoque(RelatorioProdutoAlertaEstoqueDTO alertaEstoque) {
		
		List<RelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<RelatorioProdutoAlertaEstoqueDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorvendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, cfc.data_compra as dataCompra, "
				+ " p.qtd_estoque as qtdEstoque, p.qtd_alerta_estoque as qtdAlertaEstoque "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join empresa as pj on pj.id = cfc.pessoa_id where ";
		
		sql += " cfc.data_compra >= '" + alertaEstoque.getDataInicial()+ "' and ";
		sql += " cfc.data_compra <= '" + alertaEstoque.getDataFinal()+ "'";
		sql += " and p.qtd_estoque <= p.qtd_alerta_estoque ";
		sql += " and p.alerta_qtd_estoque = true";
		
		if(!alertaEstoque.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + alertaEstoque.getCodigoNota() + " ";
		}
		
		if(!alertaEstoque.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + alertaEstoque.getCodigoNota() + " ";
		}
		
		if(!alertaEstoque.getNomeProduto().isEmpty()) {
			sql += " or upper(p.nome) like upper('%"+alertaEstoque.getNomeProduto()+"')";
		}
		
		if(!alertaEstoque.getNomeFornecedor().isEmpty()) {
			sql += " or upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"') ";

		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatorioProdutoAlertaEstoqueDTO.class));
		
		return retorno;
	}

}
