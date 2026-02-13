package com.alura.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.forum_hub.infra.DadosTokenAutenticacao;
import com.alura.forum_hub.infra.TokenAutentificacao;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.usuario.CadastroUsuarios;
import com.alura.forum_hub.usuario.DadosAutenticacaoUsuario;
import com.alura.forum_hub.usuario.DadosCadastroUsuario;
import com.alura.forum_hub.usuario.DadosDetalhamentoUsuario;
import com.alura.forum_hub.usuario.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class UsuarioController {

	@Autowired
	private TokenAutentificacao tokenAutentificacao;

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private CadastroUsuarios cadastroUsuarios;

	@PostMapping("/cadastro")
	@Transactional
	public ResponseEntity cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados) {
		var senha = cadastroUsuarios.gerarBcrypt(dados.senha());
		var usuario = new Usuario(dados, senha);
		repository.save(usuario);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
	}

	@PostMapping
	public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoUsuario dados) {
		var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
		var autenticacao = authenticationManager.authenticate(token);
		var TokenJWT = tokenAutentificacao.gerarToken((Usuario) autenticacao.getPrincipal());
		return ResponseEntity.ok(new DadosTokenAutenticacao(TokenJWT));
	}
}
