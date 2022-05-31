package adcsistemas.loja_comprebem.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/*Validade Token expira em 11 dias*/
	private static final long EXPIRATION_TIME = 950400000;
	
	private static final String SECRET = "@adcsistemas.tecnology";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		String token = TOKEN_PREFIX + " " + JWT;
		
		response.addHeader(HEADER_STRING, token);
		
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
}
