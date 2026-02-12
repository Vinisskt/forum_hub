package com.alura.forum_hub.topicos;

import java.time.LocalDateTime;

import com.alura.forum_hub.cursos.Cursos;

public record DadosDetalhamentoTopico(
		String titulo,
		String mensagem,
		String autor,
		Cursos curso,
		String status,
		LocalDateTime data) {
	public DadosDetalhamentoTopico(Topico topico) {
		this(topico.getTitulo(),
				topico.getMensagem(),
				topico.getUsuario().getUsername(),
				topico.getCurso(),
				topico.getStatus(),
				topico.getData());
	}
}
