package adcsistemas.loja_comprebem.repository;

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
	List<VendaCompraLojaVirtual> vendaPorProdutoAtivo(Long idProduto);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.ativo = true and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProdutoInativo(Long idProduto);
	
}
