package com.gestao.zk.domain.repository;

import com.gestao.zk.domain.enumeration.TipoAmbiente;
import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.model.RegistroPresenca;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RegistroPresencaRepositoryTest {
    @Autowired
    private TestEntityManager manager;

    @Autowired
    private RegistroPresencaRepository repository;

    @Test
    void deveEncontrarRegistroAtivoPorAluno() {
        var aluno = Aluno.builder()
                .matricula("2026101")
                .nome("Ana Souza")
                .email("ana@email.com")
                .build();
        manager.persist(aluno);

        var ambiente = Ambiente.builder()
                .nome("Laboratório 01")
                .tipo(TipoAmbiente.LABORATORIO)
                .capacidadeMaxima(30)
                .build();
        manager.persist(ambiente);

        var registro = RegistroPresenca.builder()
                .aluno(aluno)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now())
                .dataHoraSaida(null)
                .build();
        manager.persist(registro);
        manager.flush();

        var resultado = repository.findByAlunoAndDataHoraSaidaIsNull(aluno);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(registro.getId());
    }

    @Test
    void naoDeveEncontrarRegistroAtivoSeAlunoJaTiverSaidaRegistrada() {
        var aluno = Aluno.builder()
                .matricula("2026102")
                .nome("Bruno Lima")
                .email("bruno@email.com")
                .build();
        manager.persist(aluno);

        var ambiente = Ambiente.builder()
                .nome("Laboratório 02")
                .tipo(TipoAmbiente.LABORATORIO)
                .capacidadeMaxima(20)
                .build();
        manager.persist(ambiente);

        var registro = RegistroPresenca.builder()
                .aluno(aluno)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now().minusHours(2))
                .dataHoraSaida(LocalDateTime.now())
                .build();
        manager.persist(registro);
        manager.flush();

        var resultado = repository.findByAlunoAndDataHoraSaidaIsNull(aluno);
        assertThat(resultado).isNotPresent();
    }

    @Test
    void deveContarCorretamenteOcupantesAtivosNoAmbiente() {
        var ambiente = Ambiente.builder()
                .nome("Auditório")
                .tipo(TipoAmbiente.LABORATORIO)
                .capacidadeMaxima(150)
                .build();
        manager.persist(ambiente);

        var carla = Aluno.builder()
                .matricula("2026103")
                .nome("Carla")
                .email("carla@email.com")
                .build();
        var daniel = Aluno.builder()
                .matricula("2026104")
                .nome("Daniel")
                .email("daniel@email.com")
                .build();
        manager.persist(carla);
        manager.persist(daniel);

        manager.persist(RegistroPresenca.builder()
                .aluno(carla)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now())
                .build());

        manager.persist(RegistroPresenca.builder()
                .aluno(daniel)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now())
                .build());

        var eva = Aluno.builder()
                .matricula("2026105")
                .nome("Eva")
                .email("eva@email.com")
                .build();
        manager.persist(eva);
        manager.persist(RegistroPresenca.builder()
                .aluno(eva)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now().minusHours(1))
                .dataHoraSaida(LocalDateTime.now())
                .build());

        manager.flush();

        var total = repository.countByAmbienteAndDataHoraSaidaIsNull(ambiente);
        assertThat(total).isEqualTo(2);
    }
}
