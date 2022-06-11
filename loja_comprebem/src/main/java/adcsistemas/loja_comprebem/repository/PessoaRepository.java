package adcsistemas.loja_comprebem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.Pessoa;
import adcsistemas.loja_comprebem.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpj(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public PessoaJuridica existeinscEstadual(String inscEstadual);

}
