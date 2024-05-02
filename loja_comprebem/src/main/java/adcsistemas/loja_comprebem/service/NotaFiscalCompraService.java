package adcsistemas.loja_comprebem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioCompraNotaFiscalDTO;
import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioProdutoAlertaEstoqueDTO;
import adcsistemas.loja_comprebem.model.dto.relatorios.RelatorioStatusVendaAbandonadaDTO;

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
	
	/**
	 * Este relatorio consulta alerta em estoque se estiver baixo do estoque principal
	 * @param relatorioProdutoAlertaEstoqueDTO
	 * @return RelatorioProdutoAlertaEstoqueDTO
	 */
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
	
	
	public List<RelatorioStatusVendaAbandonadaDTO> gerarrelatorioStatusVendaAbandonada(RelatorioStatusVendaAbandonadaDTO relatorioStatusVendaAbandonada) {
		
		List<RelatorioStatusVendaAbandonadaDTO> retorno = new ArrayList<RelatorioStatusVendaAbandonadaDTO>();
		
		String sql = " select p.id as codigoProduto, "
				+ "p.nome as nomeProduto, "
				+ "pf.email as emailCliente, "
				+ "pf.telefone as foneCliente, "
				+ "p.valor_venda as valorVenda, "
				+ "pf.id as codigoCliente, "
				+ "pf.nome as nomeCliente, "
				+ "p.qtd_estoque as qtdEstoque, "
				+ "cfc.id as codigoVenda, "
				+ "cfc.status_venda_loja_virtual as statusVenda "
				+ "from vd_cp_loja_virt as cfc "
				+ "inner join item_venda_loja as ntp on  ntp.venda_compra_loja_virt_id = cfc.id "
				+ "inner join produto as p on p.id = ntp.produto_id "
				+ "inner join pessoa_fisica as pf on pf.id = cfc.pessoa_id ";
				
		
		sql += " where cfc.data_venda >= '" +relatorioStatusVendaAbandonada.getDataInicial()+ "' and cfc.data_venda <= '"+relatorioStatusVendaAbandonada.getDataFinal()+"'";
		
		
		
		if(!relatorioStatusVendaAbandonada.getCodigoVenda().isEmpty()) {
			sql += " and cfc.id = " +relatorioStatusVendaAbandonada.getCodigoVenda() + " ";
		}
		
		if(!relatorioStatusVendaAbandonada.getCodidoCliente().isEmpty()) {
			sql += " and p.id = " +relatorioStatusVendaAbandonada.getCodidoCliente() + " ";
		}
		
		if(!relatorioStatusVendaAbandonada.getStatusVenda().isEmpty()) {
			sql += " cfc.status_venda_loja_virtual in ('"+relatorioStatusVendaAbandonada.getStatusVenda()+"')";
		}
		
		if(!relatorioStatusVendaAbandonada.getNomeProduto().isEmpty()) {
			sql += " or upper(p.nome) like upper('%"+relatorioStatusVendaAbandonada.getNomeProduto()+"%')";
		}
		
		if(!relatorioStatusVendaAbandonada.getNomeCliente().isEmpty()) {
			sql += " and pf.nome like upper('%"+relatorioStatusVendaAbandonada.getNomeCliente()+"%') ";
		}
		
		if(!relatorioStatusVendaAbandonada.getEmailCliente().isEmpty()) {
			sql += " and pf.email like upper('%"+relatorioStatusVendaAbandonada.getEmailCliente()+"%') ";
		}
		
		if(!relatorioStatusVendaAbandonada.getFoneCliente().isEmpty()) {
			sql += " and pf.telefone like upper('%"+relatorioStatusVendaAbandonada.getFoneCliente()+"%') ";
		}
		
		if(!relatorioStatusVendaAbandonada.getValorVendaProduto().isEmpty()) {
			sql += " and p.valor_venda('"+relatorioStatusVendaAbandonada.getValorVendaProduto()+"') ";
		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatorioStatusVendaAbandonadaDTO.class));
		
		return retorno;
	}

}
