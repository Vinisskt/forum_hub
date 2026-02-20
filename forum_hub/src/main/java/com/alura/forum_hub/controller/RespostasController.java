package com.alura.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.repository.RespostaRepository;
import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.respostas.DadosCadastroRespostas;
import com.alura.forum_hub.respostas.DadosDetalhamentoResposta;
import com.alura.forum_hub.respostas.Resposta;
import com.alura.forum_hub.usuario.Usuario;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class RespostasController {

  @Autowired
  private TopicoRepository topicoRepository;

  @Autowired
  private RespostaRepository respostaRepository;

  @Autowired
  private ValidacaoIdExiste idExiste;

  @PostMapping("/{id}/resposta")
  @Transactional
  public ResponseEntity cadastrarResposta(@RequestBody @Valid DadosCadastroRespostas dados,
      @PathVariable Long id) {
    var usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var topico = topicoRepository.getReferenceById(id);
    var resposta = new Resposta(topico, dados.resposta(), usuarioLogado);
    respostaRepository.save(resposta);
    return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
  }

  @GetMapping("/{id}/resposta")
  public ResponseEntity<Page> listarRespostas(@PathVariable Long id, @PageableDefault(size = 10) Pageable page) {
    idExiste.validar(id);
    var resposta = respostaRepository.findAllByTopicoId(id, page).map(DadosDetalhamentoResposta::new);
    return ResponseEntity.ok(resposta);
  }

}
