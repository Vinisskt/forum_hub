package com.alura.forum_hub.infra.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.forum_hub.repository.TopicoRepository;
import com.alura.forum_hub.topicos.DadosCadastroTopico;

@Component
public class ValidacoesTopicoCadastro {

	@Autowired
	private TopicoRepository repository;

	public void validar(DadosCadastroTopico dados) {

		var verificacaoDados = repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem());
		if (verificacaoDados == true) {
			throw new RuntimeException("Topico ja Existe");
		}

		return;
	}
}
