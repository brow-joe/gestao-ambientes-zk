package com.gestao.zk.domain.repository;

import com.gestao.zk.domain.model.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AlunoRepositoryTest {
    @Autowired
    private AlunoRepository repository;

    @Test
    void deveVerificarSeMatriculaExiste() {
        var aluno = Aluno.builder()
                .matricula("999888777")
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .build();
        repository.save(aluno);

        assertThat(repository.existsByMatricula(aluno.getMatricula()))
                .isTrue();
        assertThat(repository.existsByMatricula("0000000"))
                .isFalse();
    }
}
