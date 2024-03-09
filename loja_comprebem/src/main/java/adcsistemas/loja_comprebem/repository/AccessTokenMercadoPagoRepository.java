package adcsistemas.loja_comprebem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adcsistemas.loja_comprebem.model.AccessTokenMercadoPagoClient;

@Repository
@Transactional
public interface AccessTokenMercadoPagoRepository extends JpaRepository<AccessTokenMercadoPagoClient, Long>{

}
