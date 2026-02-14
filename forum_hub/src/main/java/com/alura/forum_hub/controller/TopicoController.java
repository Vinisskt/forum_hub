package com.alura.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.infra.validacoes.ValidacoesTopicoCadastro;
import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.topicos.*;
import com.alura.forum_hub.usuario.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

	@Autowired
	private TopicoRepository repository;

	@Autowired
	private ValidacaoIdExiste idExiste;

	@Autowired
	private ValidacoesTopicoCadastro validacoesTopicoCadastro;

	// controler Topicos
	@PostMapping
	@Transactional
	public ResponseEntity cadastrarTopico(@RequestBody @Valid DadosCadastroTopico dados,
			UriComponentsBuilder uriComponentsBuilder) {
		var usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		validacoesTopicoCadastro.validar(dados);
		var topico = new Topico(dados, usuarioLogado);
		repository.save(topico);
		var uri = uriComponentsBuilder.path("topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity AtualizarTopico(@RequestBody @Valid DadosAtualizarTopico dados, @PathVariable Long id) {
		idExiste.validar(id);
		var topico = repository.getReferenceById(id);
		topico.atualizarTopico(dados);
		return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity deletarTopico(@PathVariable Long id) {
		idExiste.validar(id);
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Page> listarTopicos(@PageableDefault(size = 10, sort = { "data" }) Pageable page) {
		var topicos = repository.findAll(page).map(DadosDetalhamentoTopico::new);
		return ResponseEntity.ok(topicos);
	}

	@GetMapping("/{id}")
	public ResponseEntity DetalharTopico(@PathVariable Long id) {
		idExiste.validar(id);
		var detalhamento = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoTopico(detalhamento));
	}
}
