package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.AvaliacaoProduto;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

	@Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1")
	List<AvaliacaoProduto> pesquisaAvaliacaoProdutoId(Long idProduto);
	
	@Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1 and a.pessoa.id = ?2")
	List<AvaliacaoProduto> pesquisaAvaliacaoProdutoPessoa(Long idProduto, Long idPessoa);
	
	@Query(value ="select a from AvaliacaoProduto a where a.pessoa.id = ?1")
	List<AvaliacaoProduto> pesquisaAvaliacaoPessoa(Long idPessoa);

	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from avaliacao_produto where id = ?1")
	void deleteAvaliacaoPessoa(Long id);
}
