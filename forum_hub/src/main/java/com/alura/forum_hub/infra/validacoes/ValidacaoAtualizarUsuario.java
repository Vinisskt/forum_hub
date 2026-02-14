package com.alura.forum_hub.infra.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.usuario.DadosAtualizarUsuario;
import com.alura.forum_hub.usuario.Usuario;

@Component
public class ValidacaoAtualizarUsuario {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioRepository repository;

	public Usuario validar(DadosAtualizarUsuario dados) {
		var idExiste = repository.existsByLogin(dados.login());
		if (idExiste == false) {
			throw new RuntimeException("Login inserido Invalido");
		}

		var usuario = repository.findUsuarioByLogin(dados.login());

		var validaçao = passwordEncoder.matches(dados.senhaAntiga(), usuario.getSenha());
		if (validaçao == false) {
			throw new RuntimeException("Senha inserida invalida");
		}

		return usuario;
	}
}
