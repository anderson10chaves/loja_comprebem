package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import adcsistemas.loja_comprebem.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean existeProduto(String nomeProduto);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1)) and empresa_id = ?2")
	public boolean existeProduto(String nomeProduto, Long idEmpresa);

	@Query("select a from Produto a where upper(trim(a.nome)) like %?1%")
	List<Produto> pesquisaProdutoNome(String nome);
	
	@Query("select a from Produto a where upper(trim(a.nome)) like %?1% and a.empresa.id = ?2")
	List<Produto> pesquisaProdutoNome(String nome, Long idEmpresa);
}
