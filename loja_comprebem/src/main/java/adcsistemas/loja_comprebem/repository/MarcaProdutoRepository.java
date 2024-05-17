package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

	@Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	List<MarcaProduto> pesquisaMarcaProdutoNome(String nomeDesc);

	@Query(nativeQuery = true, value = "select cast (count(1) / 6 + 1 as integer) as qtdpagina from marca_produto where empresa_id = ?1")
	public Integer qtdPagina(Long idEmpresa);

	@Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1% and a.empresa.id = ?2")
	List<MarcaProduto> pesquisaMarcaProdutoNomeDescEmpresa(String upperCase, Long empresa);
	
	@Query(value = "select a from MarcaProduto a where a.empresa.id = ?1 ")
	public List<MarcaProduto>  findPage(Long idEmpresa, Pageable pageable);

	@Query("select a from MarcaProduto a where a.empresa.id = ?1")
	List<MarcaProduto> findAll(Long codEmpresa);

	@Query(nativeQuery = true, value = "select count(1) > 0 from marca_produto where upper(trim(nome_desc)) = upper(trim(?1))")
	public boolean existeMarcaProduto(String nomeDesc);
	
}
