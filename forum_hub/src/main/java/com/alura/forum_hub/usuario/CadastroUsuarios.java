package com.alura.forum_hub.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastroUsuarios {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String gerarBcrypt(String senha) {
		return passwordEncoder.encode(senha);
	}

}
