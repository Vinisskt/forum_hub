package com.alura.forum_hub.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
		@NotBlank String login,
		@NotBlank String senha) {

}
