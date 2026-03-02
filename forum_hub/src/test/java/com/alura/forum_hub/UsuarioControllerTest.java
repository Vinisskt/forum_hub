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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

import com.alura.forum_hub.controller.UsuarioController;
import com.alura.forum_hub.infra.SecurityConfiguration;
import com.alura.forum_hub.infra.TokenAutentificacao;
import com.alura.forum_hub.infra.validacoes.ValidacaoAtualizarUsuario;
import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.usuario.CadastroUsuarios;
import com.alura.forum_hub.usuario.DadosAutenticacaoUsuario;
import com.alura.forum_hub.usuario.DadosCadastroUsuario;
import com.alura.forum_hub.usuario.Usuario;

@WebMvcTest(UsuarioController.class)
@AutoConfigureJsonTesters
@Import(SecurityConfiguration.class)
public class UsuarioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<DadosCadastroUsuario> cJacksonTester;

  @Autowired
  private JacksonTester<DadosAutenticacaoUsuario> aJacksonTester;

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
  @DisplayName("Deveria retornar erro 400 quando informado entradas invalidas | menos de 8 digitos no campo senha")
  @WithMockUser
  void cadastrar_cenario1() throws Exception {
    var response = testCadastro("vinisskt", "123456", "/usuario/cadastro");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar erro 400 quando informado entradas invalidas | usuario vazio")
  @WithMockUser
  void cadastrar_cenario2() throws Exception {
    var response = testCadastro("", "123456", "/usuario/cadastro");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar erro 400 quando informado entradas invalidas | mais de 8 digitos no campo senha")
  @WithMockUser
  void cadastrar_cenario3() throws Exception {
    var response = testCadastro("vinisskt", "123456789", "/usuario/cadastro");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @DisplayName("Deveria retornar status 200 quando informado entradas validas")
  @WithMockUser
  void cadastrar_cenario() throws Exception {
    var response = testCadastro("vinisskt", "12345678", "/usuario/cadastro");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  // corpo do testcadastro
  private MockHttpServletResponse testCadastro(String user, String senha, String rota) throws Exception {
    var cadastro = new DadosCadastroUsuario(user, senha);
    var response = mockMvc.perform(post(rota)
        .contentType(MediaType.APPLICATION_JSON)
        .content(cJacksonTester.write(cadastro).getJson()))
        .andReturn().getResponse();

    return response;
  }

  @Test
  @DisplayName("Deveria retornar status 400 quando informado entradas invalidas | menos de 8 digitos no campo senha")
  @WithMockUser
  void logar_cenario1() throws Exception {
    var response = testLogar("vinisskt", "123456", "/usuario/login");
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar status 400 quando informado entradas validas como a senha conter algum caractere que nao seja digitos")
  @WithMockUser
  void logar_cenario2() throws Exception {
    var response = testLogar("vinisskt", "1234h678", "/usuario/login");
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar status 400 quando informado entradas invalidas | mais de 8 digitos no campo senha")
  @WithMockUser
  void logar_cenario3() throws Exception {
    var response = testLogar("vinisskt", "123456789", "/usuario/login");
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria retornar status 200 quando informado entradas validas")
  @WithMockUser
  void logar_cenario4() throws Exception {
    var response = testLogar("vinisskt", "12345678", "/usuario/login");
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  // corpo do testLogar
  private MockHttpServletResponse testLogar(String user, String senha, String rota) throws Exception {
    var login = new DadosAutenticacaoUsuario(user, senha);

    var usuarioFalso = new Usuario();
    var auteticacaoFalsa = new UsernamePasswordAuthenticationToken(usuarioFalso, null);

    when(authenticationManager.authenticate(any())).thenReturn(auteticacaoFalsa);
    when(tokenAutentificacao.gerarToken(any())).thenReturn("fake-123");

    System.out.println(login);
    var response = mockMvc.perform(post(rota)
        .contentType(MediaType.APPLICATION_JSON)
        .content(aJacksonTester.write(login).getJson()))
        .andReturn().getResponse();

    return response;

  }
}
