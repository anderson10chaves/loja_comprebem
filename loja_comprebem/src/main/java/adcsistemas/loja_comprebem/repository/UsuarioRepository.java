package adcsistemas.loja_comprebem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import adcsistemas.loja_comprebem.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query(value = "select u from Usuario u where u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaExpirada();

	@Query(value = "select u from Usuario u where u.login.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage \n"
			+ "	where table_name = 'usuarios_acesso' and column_name = 'acesso_id'\n"
			+ "and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER'))")
	void insereAcessoUser(Long iduser);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = ?2 limit 1))")
	void insereAcessoUserPj(Long iduser, String acesso);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "update usuario set senha = ?1 where login  = ?2", nativeQuery = true)
	void updateSenhaUser(String senha, String login);
}
