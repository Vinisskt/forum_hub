package com.alura.forum_hub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.forum_hub.respostas.Resposta;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

  Page<Resposta> findAllByTopicoId(Long id, Pageable page);
}
