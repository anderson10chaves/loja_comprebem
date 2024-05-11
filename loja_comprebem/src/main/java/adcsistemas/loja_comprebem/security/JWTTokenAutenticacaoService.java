package adcsistemas.loja_comprebem.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import adcsistemas.loja_comprebem.ApiTokenIntegracao;
import adcsistemas.loja_comprebem.ApplicationContextLoad;
import adcsistemas.loja_comprebem.model.Usuario;
import adcsistemas.loja_comprebem.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* Validade Token expira em 11 dias */
	private static final long EXPIRATION_TIME = 950400000;

	private static final String SECRET = "@adcsistemas.tecnology";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		String token = TOKEN_PREFIX + " " + JWT;

		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		
		Usuario usuario = ApplicationContextLoad.getApplicationContext()
				.getBean(UsuarioRepository.class)
				.findUserByLogin(username);
		
		System.out.println(usuario.getEmpresa().getId());

		response.getWriter().write("{\"Authorization\": \"" + token + "\", \"username\": \""+username+"\", \"empresa\": \""+usuario.getEmpresa().getId()+"\"}");
	}

	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String token = request.getHeader(HEADER_STRING);

		try {

			if (token != null) {
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

				if (user != null) {
					Usuario usuario = ApplicationContextLoad.getApplicationContext()
							.getBean(UsuarioRepository.class)
							.findUserByLogin(user);

					if (usuario != null) {
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), 
								usuario.getPassword(),
								usuario.getAuthorities());
					}
				}
			}
		} catch (SignatureException e) {
			response.getWriter().write("Token informado esta inválido ou incorreto");
		
		} catch (ExpiredJwtException e) {
			response.getWriter().write("Token está expirado, realize o login novamente!");

		} finally {
			liberacaoCors(response);
		}
		return null;
	}

	private void liberacaoCors(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
	
}
