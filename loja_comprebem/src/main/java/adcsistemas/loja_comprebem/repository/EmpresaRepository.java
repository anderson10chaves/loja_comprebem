package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.Empresa;

@Repository
@Transactional
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	
	@Query(value = "select pj from Empresa pj where upper(trim(pj.nome)) like %?1%")
	public List<Empresa> pesquisaPorNomePJ(String nome);
	
	@Query(value = "select pj from Empresa pj where pj.cnpj = ?1")
	public Empresa existeCnpj(String cnpj);
	
	@Query(value = "select pj from Empresa pj where upper(trim(pj.cnpj)) like %?1%")
	public List<Empresa> existeCnpjList(String cnpj);
	
	@Query(value = "select pj from Empresa pj where pj.inscEstadual = ?1")
	public Empresa existeinscEstadual(String inscEstadual);

	@Query(value = "select pj from Empresa pj where upper(trim(pj.inscEstadual)) like %?1%")
	public List<Empresa> existeinscEstadualList(String inscEstadual);
	
	@Query(value = "select pj from Empresa pj where pj.inscMunicipal = ?1")
	public Empresa existeinscMunicipal(String inscMunicipal);

	@Query(value = "select pj from Empresa pj where upper(trim(pj.inscMunicipal)) like %?1%")
	public List<Empresa> existeinscMunicipallList(String inscMunicipal);
	
	@Query(value = "select pj from Empresa pj where pj.nomeFantasia = ?1")
	public Empresa existeNomeFantasia(String nomeFantasia);

	@Query(value = "select pj from Empresa pj where upper(trim(pj.nomeFantasia)) like %?1%")
	public List<Empresa> existeNomeFantasiaList(String nomeFantasia);
	
	@Query(value = "select pj from Empresa pj where pj.razaoSocial = ?1")
	public Empresa existeRazaoSocial(String razaoSocial);

	@Query(value = "select pj from Empresa pj where upper(trim(pj.razaoSocial)) like %?1%")
	public List<Empresa> existeRazaoSocialList(String razaoSocial);
	
	@Query(value = "select pj from Empresa pj where pj.categoria = ?1")
	public Empresa existeCategoria(String categoria);

	@Query(value = "select pj from Empresa pj where upper(trim(pj.categoria)) like %?1%")
	public List<Empresa> existeCategoriaList(String categoria);
}
