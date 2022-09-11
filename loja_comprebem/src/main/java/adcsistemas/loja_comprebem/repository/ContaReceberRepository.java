package adcsistemas.loja_comprebem.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.ContaReceber;

@Repository
@Transactional
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long>{

}
