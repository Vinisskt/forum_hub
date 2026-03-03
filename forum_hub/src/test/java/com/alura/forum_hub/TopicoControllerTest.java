package com.alura.forum_hub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

import com.alura.forum_hub.controller.TopicoController;
import com.alura.forum_hub.cursos.Cursos;
import com.alura.forum_hub.infra.SecurityFilter;
import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.infra.validacoes.ValidacoesTopicoCadastro;
import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.topicos.DadosCadastroTopico;
import com.alura.forum_hub.topicos.DadosDetalhamentoTopico;
import com.alura.forum_hub.topicos.Topico;
import com.alura.forum_hub.usuario.Usuario;

@WebMvcTest(TopicoController.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)
public class TopicoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<DadosCadastroTopico> cJacksonTester;

  @Autowired
  private JacksonTester<DadosDetalhamentoTopico> dJacksonTester;
  @MockitoBean
  private TopicoRepository topicoRepository;

  @MockitoBean
  private UsuarioRepository usuarioRepository;

  @MockitoBean
  private ValidacaoIdExiste validacaoIdExiste;

  @MockitoBean
  private ValidacoesTopicoCadastro validacoesTopicoCadastro;

  @MockitoBean
  private SecurityFilter security;

  @Test
  @DisplayName("validacao deve retornar status 201 quando todos os campos sao validos | valida a saida de DadosDetalhamentoTopico")
  @WithMockUser
  public void cadastrarCenario1() throws Exception {
    var response = testCadastro("erro com mockito", "erro com import static", Cursos.JAVA, "/topicos");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  @DisplayName("validacao deve retornar status 400 quando o campo titulo esta vazio")
  @WithMockUser
  public void cadastrarCenario2() throws Exception {
    var response = testCadastro("", "erro com import static", Cursos.JAVA, "/topicos");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("validacao deve retornar status 400 qunado o campo mensagem esta vazio")
  @WithMockUser
  public void cadastrarCenario3() throws Exception {
    var response = testCadastro("erro com mockito", "", Cursos.JAVA, "/topicos");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  // criaçao do json de DadosDetalhamentoTopico
  private String testCadastroSaidaDTO(Topico topico) throws Exception {
    var dados = new DadosDetalhamentoTopico(topico);
    return dJacksonTester.write(dados).getJson();
  }

  // corpo dos testes de cadastro
  private MockHttpServletResponse testCadastro(String titulo, String mensagem, Cursos cursos, String rota)
      throws Exception {

    var cadastro = new DadosCadastroTopico(titulo, mensagem, cursos);
    var usuario = new Usuario(1L, "vinisskt", "12345678");

    var topicos = new Topico(1L, cadastro.titulo(), cadastro.curso(), usuario, cadastro.mensagem(), LocalDateTime.now(),
        "nao respondido");

    var detalhemnto = testCadastroSaidaDTO(topicos);
    when(topicoRepository.save(any())).thenReturn(topicos);

    var response = mockMvc.perform(post(rota)
        .contentType(MediaType.APPLICATION_JSON)
        .content(cJacksonTester.write(cadastro).getJson()))
        .andReturn().getResponse();

    if (response.getStatus() == HttpStatus.CREATED.value()) {
      assertThat(response.getContentAsString()).isEqualTo(detalhemnto);
    }

    return response;
  }
}
