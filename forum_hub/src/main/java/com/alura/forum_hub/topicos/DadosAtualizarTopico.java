package com.alura.forum_hub.topicos;

import com.alura.forum_hub.cursos.Cursos;

public record DadosAtualizarTopico(
		String titulo,
		String mensagem,
		Cursos curso) {

}
