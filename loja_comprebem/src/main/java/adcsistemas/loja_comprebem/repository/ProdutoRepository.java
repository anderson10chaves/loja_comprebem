package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.Produto;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean existeProdutoPorNome(String nomeProduto);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1)) and pessoaJuridica_id = ?2")
	public boolean existeProdutoNomeEmpresa(String nomeProduto, Long idEmpresa);

	@Query("select a from Produto a where upper(trim(a.nome)) like %?1%")
	List<Produto> pesquisaProdutoPorNome(String nome);
	
	@Query("select a from Produto a where upper(trim(a.nome)) like %?1% and a.pessoaJuridica.id = ?2")
	List<Produto> pesquisaProdutoNomeEmpresa(String nome, Long idEmpresa);
}
