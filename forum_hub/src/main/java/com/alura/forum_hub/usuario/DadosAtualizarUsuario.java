package com.alura.forum_hub.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizarUsuario(
    @NotBlank String login,
    @NotBlank @Pattern(regexp = "\\d{8") String senhaAntiga,
    @NotBlank @Pattern(regexp = "\\d{8}") String newSenha) {

}
