package adcsistemas.loja_comprebem.service.TokenAssasService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.model.AccessTokenAssasClient;

@Service
public class AccessTokenAsaasService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public AccessTokenAssasClient obterTokenAssas() {
		
		try {
			AccessTokenAssasClient accessTokenMercadoPagoClient = 
					(AccessTokenAssasClient) entityManager
					.createQuery("select a from AccessTokenMercadoPagoClient a")
					.setMaxResults(1).getSingleResult();
			
			return accessTokenMercadoPagoClient;
		} catch (NoResultException e) {
			return null;
		}
	
	}
}
