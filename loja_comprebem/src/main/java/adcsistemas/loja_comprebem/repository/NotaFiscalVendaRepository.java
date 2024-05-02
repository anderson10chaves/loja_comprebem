package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.NotaFiscalVenda;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long>{

	@Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
	List<NotaFiscalVenda> pesquisaPorVenda(Long idVenda);
	
	@Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
	NotaFiscalVenda pesquisaNotaVendaUnica(Long idVenda);
}
