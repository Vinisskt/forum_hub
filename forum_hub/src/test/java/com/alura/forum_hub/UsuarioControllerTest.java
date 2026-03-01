package com.alura.forum_hub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.alura.forum_hub.controller.UsuarioController;
import com.alura.forum_hub.infra.SecurityConfiguration;
import com.alura.forum_hub.infra.TokenAutentificacao;
import com.alura.forum_hub.infra.validacoes.ValidacaoAtualizarUsuario;
import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.usuario.CadastroUsuarios;
import com.alura.forum_hub.usuario.DadosCadastroUsuario;

@WebMvcTest(UsuarioController.class)
@AutoConfigureJsonTesters
@Import(SecurityConfiguration.class)
public class UsuarioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<DadosCadastroUsuario> cJacksonTester;

  @MockitoBean
  private UsuarioRepository usuarioRepository;

  @MockitoBean
  private TokenAutentificacao tokenAutentificacao;

  @MockitoBean
  private AuthenticationManager authenticationManager;

  @MockitoBean
  private CadastroUsuarios cadastroUsuarios;

  @MockitoBean
  private ValidacaoAtualizarUsuario validacaoAtualizarUsuario;

  @MockitoBean
  private ValidacaoIdExiste validacaoIdExiste;

  @Test
  @DisplayName("Deveria retornar erro 400 quando informado entradas invalidas")
  @WithMockUser
  void cadastrar_cenario1() throws Exception {

    var cadastro = new DadosCadastroUsuario("", "");
    var response = mockMvc.perform(post("/usuario/cadastro")
        .contentType(MediaType.APPLICATION_JSON)
        .content(cJacksonTester.write(cadastro).getJson()))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar status 200 quando informado entradas validas")
  @WithMockUser
  void cadastrar_cenario2() throws Exception {

    var cadastro = new DadosCadastroUsuario("vinissktmaloca", "123456");
    var response = mockMvc.perform(post("/usuario/cadastro")
        .contentType(MediaType.APPLICATION_JSON)
        .content(cJacksonTester.write(cadastro).getJson()))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

}
