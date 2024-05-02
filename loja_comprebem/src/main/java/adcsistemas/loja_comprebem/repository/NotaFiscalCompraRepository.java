package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.NotaFiscalCompra;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {
	
	@Query("select a from NotaFiscalCompra a where upper(trim(a.numeroNota)) like %?1%")
	List<NotaFiscalCompra> pesquisaNotaFiscalNumeroNota(String numeroNota);
	
	@Query("select a from NotaFiscalCompra a where upper(trim(a.descricaoObservacao)) like %?1%")
	List<NotaFiscalCompra> pesquisaNotaFiscalDesc(String descricaoObservacao);
	
	@Query("select a from NotaFiscalCompra a where a.pessoaFornecedor.id = ?1")
	List<NotaFiscalCompra> pesquisaNotaFiscalPessoa(Long idPessoa);
	
	@Query("select a from NotaFiscalCompra a where a.contaPagar.id = ?1")
	List<NotaFiscalCompra> pesquisaNotaFiscalContaPagar(Long idContapagar);
	
	@Query("select a from NotaFiscalCompra a where a.empresa.id = ?1")
	List<NotaFiscalCompra> pesquisaNotaFiscalEmpresa(Long idEmpresa);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from nota_item_produto where nota_fiscal_compra_id = ?1")
	void deleteItemNotaFiscalCompra(Long idNotaFiscalCompra);

}
