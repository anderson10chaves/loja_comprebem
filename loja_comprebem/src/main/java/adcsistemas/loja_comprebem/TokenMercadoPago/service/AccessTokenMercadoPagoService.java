package adcsistemas.loja_comprebem.TokenMercadoPago.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.AccessTokenMercadoPagoClient;

@Service
public class AccessTokenMercadoPagoService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public AccessTokenMercadoPagoClient obterTokenMercadoPago() {
		
		try {
			AccessTokenMercadoPagoClient accessTokenMercadoPagoClient = 
					(AccessTokenMercadoPagoClient) entityManager
					.createQuery("select a from AccessTokenMercadoPagoClient a")
					.setMaxResults(1).getSingleResult();
			
			return accessTokenMercadoPagoClient;
		} catch (NoResultException e) {
			return null;
		}
	
	}
}
