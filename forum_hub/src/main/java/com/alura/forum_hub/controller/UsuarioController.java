package com.alura.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.forum_hub.infra.DadosTokenAutenticacao;
import com.alura.forum_hub.infra.TokenAutentificacao;
import com.alura.forum_hub.infra.validacoes.ValidacaoAtualizarUsuario;
import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.usuario.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
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

	@Autowired
	private ValidacaoIdExiste idExiste;

	@Autowired
	private ValidacaoAtualizarUsuario validacaoAtualizarUsuario;

	@PostMapping("/cadastro")
	@Transactional
	public ResponseEntity cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados) {
		var senha = cadastroUsuarios.gerarBcrypt(dados.senha());
		var usuario = new Usuario(dados, senha);
		repository.save(usuario);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
	}

	@PostMapping("/login")
	public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoUsuario dados) {
		var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
		var autenticacao = authenticationManager.authenticate(token);
		var TokenJWT = tokenAutentificacao.gerarToken((Usuario) autenticacao.getPrincipal());
		return ResponseEntity.ok(new DadosTokenAutenticacao(TokenJWT));
	}

	@GetMapping
	public ResponseEntity<Page> listarUsuarios(@PageableDefault(size = 10, sort = { "login" }) Pageable page) {
		var usuarios = repository.findAll(page).map(DadosDetalhamentoUsuario::new);
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity detalharUsuario(@PathVariable Long id) {
		idExiste.validarUsuario(id);
		var detalhamento = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(detalhamento));
	}

	@PutMapping("/senha")
	@Transactional
	public ResponseEntity AtualizarSenha(@RequestBody @Valid DadosAtualizarUsuario dados) {
		var usuario = validacaoAtualizarUsuario.validar(dados);
		var senha = cadastroUsuarios.gerarBcrypt(dados.newSenha());
		usuario.atualizar(senha);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity deletarUsuario(@PathVariable Long id) {
		idExiste.validarUsuario(id);
		repository.deleteById(id);
		return ResponseEntity.noContent().build();

	}
}
