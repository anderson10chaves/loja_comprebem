package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.StatusRastreio;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {
	
	@Query(value = "select s from StatusRastreio s where s.vendaCompraLojaVirtual.id = ?1 order by s.id")
	public List<StatusRastreio> listaRastreioVenda(Long idVenda);

}
