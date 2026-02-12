package com.alura.forum_hub.infra;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alura.forum_hub.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenAutentificacao {

	@Value("${api.password.secret}")
	private String secret;

	public String gerarToken(Usuario usuario) {
		try {
			System.out.println("entrou em gerar token");
			var algoritmo = Algorithm.HMAC256(secret);
			return JWT.create()
					.withIssuer("forum_hub_token")
					.withSubject(usuario.getLogin())
					.withExpiresAt(tempoInspirarToken())
					.sign(algoritmo);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar TOKEN JWT", exception);
		}
	}

	public String getSubject(String tokenJWT) {
		try {

			var algorithm = Algorithm.HMAC256(secret);
			System.out.println("entrou em getsubject");
			return JWT.require(algorithm)
					.withIssuer("forum_hub_token")
					.build()
					.verify(tokenJWT)
					.getSubject();

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token Inspirado ou Invalido", exception);
		}
	}

	private Instant tempoInspirarToken() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

	}

}
