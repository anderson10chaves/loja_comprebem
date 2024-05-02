package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.ContaPagar;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {
	
	@Query("select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
	List<ContaPagar> pesquisaContaPagarDesc(String descricao);
	
	@Query("select a from ContaPagar a where a.pessoaFisica.id = ?1")
	List<ContaPagar> pesquisaContaPagarPessoa(Long idPessoa);
	
	@Query("select a from ContaPagar a where a.pessoa_fornecedor.id = ?1")
	List<ContaPagar> pesquisaContaPagarPessoaFornecedor(Long idPessoaFornecedor);
	
	@Query("select a from ContaPagar a where a.empresa.id = ?1")
	List<ContaPagar> pesquisaContaPagarEmpresa(Long idEmpresa);

}
