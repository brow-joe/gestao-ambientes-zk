package com.gestao.zk.domain.repository;

import com.gestao.zk.domain.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {
    @Autowired
    private UsuarioRepository repository;

    @Test
    void deveBuscarUsuarioPorUsername() {
        var password = new BCryptPasswordEncoder()
                .encode("qa");
        var usuario = Usuario.builder()
                .username("qa")
                .password(password)
                .role("TESTER")
                .build();
        repository.save(usuario);

        var resultado = repository.findByUsername(usuario.getUsername());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getRole()).isEqualTo(usuario.getRole());
        assertThat(repository.findByUsername("outro"))
                .isNotPresent();
    }
}
