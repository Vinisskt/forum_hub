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

import com.alura.forum_hub.controller.RespostasController;
import com.alura.forum_hub.controller.TopicoController;
import com.alura.forum_hub.cursos.Cursos;
import com.alura.forum_hub.infra.SecurityFilter;
import com.alura.forum_hub.infra.validacoes.ValidacaoIdExiste;
import com.alura.forum_hub.infra.validacoes.ValidacoesTopicoCadastro;
import com.alura.forum_hub.repository.RespostaRepository;
import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.repository.UsuarioRepository;
import com.alura.forum_hub.respostas.DadosCadastroRespostas;
import com.alura.forum_hub.respostas.DadosDetalhamentoResposta;
import com.alura.forum_hub.respostas.Resposta;
import com.alura.forum_hub.topicos.Topico;
import com.alura.forum_hub.usuario.Usuario;

@WebMvcTest(RespostasController.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)
public class RespostasControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<DadosCadastroRespostas> cJacksonTester;

  @Autowired
  private JacksonTester<DadosDetalhamentoResposta> dJacksonTester;

  @MockitoBean
  private TopicoRepository topicoRepository;

  @MockitoBean
  private UsuarioRepository usuarioRepository;

  @MockitoBean
  private RespostaRepository respostaRepository;

  @MockitoBean
  private ValidacaoIdExiste validacaoIdExiste;

  @MockitoBean
  private ValidacoesTopicoCadastro validacoesTopicoCadastro;

  @MockitoBean
  private SecurityFilter security;

  @Test
  @DisplayName("validacao deve retornar status 201 quando todos os campos sao validos | valida a saida de DadosDetalhamentoResposta")
  @WithMockUser
  public void cadastrarCenario1() throws Exception {
    var response = testCadastro("precisa adicionar manualmente ou configurar editor para imports",
        "/topicos/1/resposta");

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  // criaçao do json de DadosDetalhamentoTopico
  private String testCadastroSaidaDTO(Resposta resposta) throws Exception {
    var dados = new DadosDetalhamentoResposta(resposta);
    return dJacksonTester.write(dados).getJson();
  }

  // corpo dos testes de cadastro
  private MockHttpServletResponse testCadastro(String resposta, String rota)
      throws Exception {

    var usuario = new Usuario(1L, "vinisskt", "12345678");
    when(usuarioRepository.findUsuarioByLogin(any())).thenReturn(usuario);

    var topicos = new Topico(1L, "erro com mockito", Cursos.JAVA, usuario, "erro com import static",
        LocalDateTime.now(),
        "nao respondido");

    var cadastro = new DadosCadastroRespostas(resposta);
    var respostaTopico = new Resposta(topicos, cadastro.resposta(), usuario);

    when(topicoRepository.getReferenceById(topicos.getId())).thenReturn(topicos);

    var detalhamento = testCadastroSaidaDTO(respostaTopico);

    when(respostaRepository.save(any())).thenReturn(respostaTopico);

    var response = mockMvc.perform(post(rota)
        .contentType(MediaType.APPLICATION_JSON)
        .content(cJacksonTester.write(cadastro).getJson()))
        .andReturn().getResponse();

    if (response.getStatus() == HttpStatus.CREATED.value()) {
      assertThat(response.getContentAsString()).isEqualTo(detalhamento);
    }

    return response;
  }
}
