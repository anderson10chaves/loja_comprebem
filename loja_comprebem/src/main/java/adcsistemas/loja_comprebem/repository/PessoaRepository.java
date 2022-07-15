package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.PessoaJuridica;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<PessoaJuridica, Long>{
	
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> pesquisaPorNomePJ(String nome);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpj(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.cnpj)) like %?1%")
	public List<PessoaJuridica> existeCnpjList(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public PessoaJuridica existeinscEstadual(String inscEstadual);

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.inscEstadual)) like %?1%")
	public List<PessoaJuridica> existeinscEstadualList(String inscEstadual);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscMunicipal = ?1")
	public PessoaJuridica existeinscMunicipal(String inscMunicipal);

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.inscMunicipal)) like %?1%")
	public List<PessoaJuridica> existeinscMunicipallList(String inscMunicipal);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.nomeFantasia = ?1")
	public PessoaJuridica existeNomeFantasia(String nomeFantasia);

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nomeFantasia)) like %?1%")
	public List<PessoaJuridica> existeNomeFantasiaList(String nomeFantasia);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.razaoSocial = ?1")
	public PessoaJuridica existeRazaoSocial(String razaoSocial);

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.razaoSocial)) like %?1%")
	public List<PessoaJuridica> existeRazaoSocialList(String razaoSocial);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.categoria = ?1")
	public PessoaJuridica existeCategoria(String categoria);

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.categoria)) like %?1%")
	public List<PessoaJuridica> existeCategoriaList(String categoria);
}
