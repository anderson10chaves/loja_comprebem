package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import adcsistemas.loja_comprebem.model.MarcaProduto;

public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

	@Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	List<MarcaProduto> pesquisaMarcaProdutoNome(String nomeDesc);
	
}
