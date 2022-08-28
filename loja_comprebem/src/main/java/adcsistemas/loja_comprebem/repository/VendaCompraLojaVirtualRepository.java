package adcsistemas.loja_comprebem.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	@Query(value = "select a from VendaCompraLojaVirtual a where a.id = ?1 and a.ativo = false")
	VendaCompraLojaVirtual findByIdAtivo(Long id);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);
	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaNomeProduto(String valor);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and i.pessoaFisica.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorNomeCliente(Long idCliente);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProdutoAtivo(Long idProduto);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = true and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProdutoInativo(Long idProduto);
	
	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.vendaCompraLojaVirtual.pessoaFisica.nome)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaNomeCliente(String nomepessoa);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaEndCobranca(String endCobranca);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaEndEntrega(String endEntrega);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.cep)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaCep(String cep);
    
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and i.vendaCompraLojaVirtual.dataVenda >= ?1 and i.vendaCompraLojaVirtual.dataVenda <= ?2 ")
	List<VendaCompraLojaVirtual> pesquisaVendaDinamicaData(Date data1, Date data2);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = false and upper(trim(i.vendaCompraLojaVirtual.pessoaFisica.cpf)) like %?1%")
	List<VendaCompraLojaVirtual> pesquisaVendaCpf(String cpf);
}
