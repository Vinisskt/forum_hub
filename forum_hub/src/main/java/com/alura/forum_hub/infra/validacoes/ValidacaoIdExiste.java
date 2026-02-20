package com.alura.forum_hub.infra.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.repository.UsuarioRepository;

@Component
public class ValidacaoIdExiste {

  @Autowired
  private TopicoRepository repositoryTopico;

  @Autowired
  private UsuarioRepository repositoryUsuario;

  public void validar(Long id) {
    var idExiste = repositoryTopico.existsById(id);
    if (!idExiste) {
      throw new RuntimeException("Id informado nao existe");
    }
  }

  public void validarUsuario(Long id) {
    var idExiste = repositoryUsuario.existsById(id);
    if (!idExiste) {
      throw new RuntimeException("Id informado nao existe");
    }
  }
}
