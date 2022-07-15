package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.NotaItemProduto;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {
	
	
	@Query("select a from NotaItemProduto a where a.produto.id = ?1 and a.notaFiscalCompra.id = ?2")
	List<NotaItemProduto> pesquisaNotaItemProdutoNota(Long idProduto, Long idNotaFiscal);
	
	@Query("select a from NotaItemProduto a where a.produto.id = ?1")
	List<NotaItemProduto> pesquisaNotaItemProdutoId(Long idProduto);

	@Query("select a from NotaItemProduto a where a.notaFiscalCompra.id = ?1")
	List<NotaItemProduto> pesquisaNotaItemNotaFiscal(Long idNotaFiscal);
	
	@Query("select a from NotaItemProduto a where a.pessoaJuridica.id = ?1")
	List<NotaItemProduto> pesquisaNotaItemEmpresa(Long idEmpresa);
			
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from imagem_produto where id = ?1")
	void deleteNotaItemProduto(Long id);

}
