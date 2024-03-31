package adcsistemas.loja_comprebem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.cobranca_pagamento.CobrancaPagamento;

@Repository
public interface CobrancaPagamentoAsaasRepository extends JpaRepository<CobrancaPagamento, Long>{

	
}