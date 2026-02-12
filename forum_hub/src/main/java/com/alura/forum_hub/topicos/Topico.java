package com.alura.forum_hub.topicos;

import java.time.LocalDateTime;

import com.alura.forum_hub.cursos.Cursos;
import com.alura.forum_hub.usuario.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;

	@Enumerated(EnumType.STRING)
	private Cursos curso;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	private String mensagem;
	private LocalDateTime data;
	private String status;

	public Topico(DadosCadastroTopico dados, Usuario idusuario) {
		this.titulo = dados.titulo();
		this.curso = dados.curso();
		this.usuario = idusuario;
		this.mensagem = dados.mensagem();
		this.data = LocalDateTime.now();
		this.status = "Não Respondido";
	}

	public void atualizarTopico(DadosAtualizarTopico dados) {
		if (dados.titulo() != null) {
			this.titulo = dados.titulo();
		}

		if (dados.mensagem() != null) {
			this.mensagem = dados.mensagem();
		}

		if (dados.curso() != null) {
			this.curso = atualizarCurso(dados.curso());
		}
	}

	public Cursos atualizarCurso(Cursos cursos) {
		return cursos;
	}
}
