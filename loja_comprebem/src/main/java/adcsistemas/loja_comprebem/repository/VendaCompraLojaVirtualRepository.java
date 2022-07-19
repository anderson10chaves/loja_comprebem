package adcsistemas.loja_comprebem.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	//@Query("select a from VendaCompraLojaVirtual a where a.id = ?1")
	//List<VendaCompraLojaVirtual> pesquisaVendaId(Long idVenda);
}
