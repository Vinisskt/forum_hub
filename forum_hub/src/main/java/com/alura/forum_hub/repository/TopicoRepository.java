package com.alura.forum_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.forum_hub.topicos.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

  Boolean existsByTituloAndMensagem(String titulo, String mensagem);

}
