package adcsistemas.loja_comprebem.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	@Query(value = "select a from VendaCompraLojaVirtual a where a.id = ?1 and a.excluido = false")
	VendaCompraLojaVirtual findByIdExcluido(Long id);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);
	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaNomeProduto(String valor);
	
	
	  @Query(value ="select i.vendaCompraLojaVirtual from ItemVendaLoja i" +
	  " where i.vendaCompraLojaVirtual.excluido = false and i.vendaCompraLojaVirtual.pessoaFisica.id = ?1")
	  List<VendaCompraLojaVirtual> vendaPorCliente(Long idCliente);
	 
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProdutoAtivo(Long idProduto);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = true and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProdutoInativo(Long idProduto);
	
	
	@Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.pessoaFisica.nome)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaNomeCliente(String nomepessoa);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaEndCobranca(String endCobranca);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaEndEntrega(String endEntrega);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.cep)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaCep(String cep);
    
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and i.vendaCompraLojaVirtual.dataVenda >= ?1 and i.vendaCompraLojaVirtual.dataVenda <= ?2 ")
	List<VendaCompraLojaVirtual> pesquisaVendaDinamicaData(Date data1, Date data2);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i"
			+ " where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.pessoaFisica.cpf)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaCpf(String cpf);

	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "update vd_cp_loja_virt set codigo_etiqueta = ?1 where id = ?2")
	void updateEtiqueta(String idEtiqueta, Long idVenda);

	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "update vd_cp_loja_virt set url_imp_etiqueta = ?1 where id = ?2")
	void updateUrlEtiqueta(String urlEtiqueta, Long id);
}
