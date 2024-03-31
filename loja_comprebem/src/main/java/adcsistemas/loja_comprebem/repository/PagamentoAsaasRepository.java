package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.cobranca_pagamento.CobrancaPagamento;

@Repository
public interface PagamentoAsaasRepository extends JpaRepository<CobrancaPagamento, Long> {
	
	@Query("select c from CobrancaPagamento c where c.vendaCompraLojaVirtual.id = ?1 and c.quitado = false")
	public List<CobrancaPagamento> cobrancaDaVendaCompra(Long idVendaCompra);

	@Query("select c from CobrancaPagamento c where c.code = ?1")
	public CobrancaPagamento findByCode(String id); 
	
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "update CobrancaPagamento set quitado = true where code = ?1")
	public void quitarBoleto(String code);
	
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "update CobrancaPagamento set quitado = true where id = ?1")
	public void quitarBoletoById(Long id);

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "delete from CobrancaPagamento where code = ?1")
	public void deleteByCode(String code);
	
}
