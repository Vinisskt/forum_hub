package com.alura.forum_hub.infra.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.forum_hub.repository.TopicoRepository;

@Component
public class ValidacaoIdExiste {

	@Autowired
	private TopicoRepository repository;

	public void validar(Long id) {
		var idExiste = repository.existsById(id);
		if (!idExiste) {
			throw new RuntimeException("Id informado nao existe");
		}
	}
}
