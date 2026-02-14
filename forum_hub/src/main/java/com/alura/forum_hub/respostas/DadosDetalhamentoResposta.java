package com.alura.forum_hub.respostas;

import com.alura.forum_hub.topicos.Topico;
import com.alura.forum_hub.usuario.Usuario;

public record DadosDetalhamentoResposta(
		Usuario usuario,
		Topico topico,
		String resposta) {

	public DadosDetalhamentoResposta(Resposta resposta) {
		this(resposta.getUsuario(), resposta.getTopico(), resposta.getResposta());
	}
}
