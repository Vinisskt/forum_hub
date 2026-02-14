package com.alura.forum_hub.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizarUsuario(
		@NotBlank String login,
		@NotBlank String senhaAntiga,
		@NotBlank String newSenha) {

}
