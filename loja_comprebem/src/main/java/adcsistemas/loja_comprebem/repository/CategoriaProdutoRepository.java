package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.CategoriaProduto;

@Repository
@Transactional
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

	@Query("select a from CategoriaProduto a where a.empresa.id = ?1")
	List<CategoriaProduto> pesquisaCategoriaProdutosId(Long id);
	
	@Query("select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	List<CategoriaProduto> pesquisaCategoriaProdutoNomeDesc(String nomeDesc);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from categoria_produto where upper(trim(nome_desc)) = upper(trim(?1))")
	public boolean existeCategoriaProduto(String nomeCategoria);

	@Query("select a from CategoriaProduto a where a.empresa.id = ?1")
	List<CategoriaProduto> findAll(Long codEmpresa);

	@Query("select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1% and a.empresa.id = ?2")
	List<CategoriaProduto> pesquisaCategoriaProdutoNomeDescEmpresa(String upperCase, Long empresa);
	
	@Query(nativeQuery = true, value = "select cast (count(1) / 5 + 1 as integer) as qtdpagina from categoria_produto where empresa_id = ?1")
	public Integer qtdPagina(Long idEmpresa);

	@Query(value = "select a from CategoriaProduto a where a.empresa.id = ?1 ")
	public List<CategoriaProduto>  findPage(Long idEmpresa, Pageable pageable);
}
