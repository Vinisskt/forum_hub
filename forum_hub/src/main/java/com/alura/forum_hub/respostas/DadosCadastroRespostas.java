package com.alura.forum_hub.respostas;

import com.alura.forum_hub.usuario.Usuario;

public record DadosCadastroRespostas(
		Usuario usuario,
		String resposta) {

}
