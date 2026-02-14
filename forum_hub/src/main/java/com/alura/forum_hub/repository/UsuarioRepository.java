package com.alura.forum_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.alura.forum_hub.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	UserDetails findByLogin(String login);

	Usuario findUsuarioByLogin(String login);

	boolean existsByLogin(String login);

	Usuario findBySenha(String token);

}
