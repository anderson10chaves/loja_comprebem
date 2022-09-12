package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.CupomDesconto;

@Repository
@Transactional
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long>{

	@Query(value = "select c from CupomDesconto c where c.pessoaJuridica.id = ?1")
	public List<CupomDesconto> listaCupomDescontoEmpresa(Long idEmpresa);
}
