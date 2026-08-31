package com.gestao.zk.application.service;

import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.model.RegistroPresenca;
import com.gestao.zk.domain.repository.AmbienteRepository;
import com.gestao.zk.domain.repository.RegistroPresencaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresencaServiceTest {
    @Mock
    private RegistroPresencaRepository presencaRepository;

    @Mock
    private AmbienteRepository ambienteRepository;

    @InjectMocks
    private PresencaService service;

    @Test
    void deveLancarExcecaoSeAlunoJaEstiverPresente() {
        var aluno = new Aluno();
        var ambiente = Ambiente.builder()
                .nome("Sala 1")
                .capacidadeMaxima(10)
                .build();
        
        when(presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno))
                .thenReturn(Optional.of(RegistroPresenca.builder().ambiente(ambiente).build()));

        assertThatThrownBy(() -> service.registrarEntrada(aluno, ambiente))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("O aluno já está presente");
    }

    @Test
    void deveLancarExcecaoSeCapacidadeMaximaAtingida() {
        var aluno = new Aluno();
        var ambiente = Ambiente.builder()
                .nome("Sala 1")
                .capacidadeMaxima(2)
                .build();

        when(presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno)).thenReturn(Optional.empty());
        when(presencaRepository.countByAmbienteAndDataHoraSaidaIsNull(ambiente)).thenReturn(2L);

        assertThatThrownBy(() -> service.registrarEntrada(aluno, ambiente))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Capacidade máxima do ambiente");
    }

    @Test
    void deveRegistrarSaidaComSucesso() {
        var aluno = new Aluno();
        var presenca = new RegistroPresenca();
        when(presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno)).thenReturn(Optional.of(presenca));

        service.registrarSaida(aluno);
        verify(presencaRepository, times(1)).save(any(RegistroPresenca.class));
    }

    @Test
    void deveRegistrarEntradaComSucesso() {
        var aluno = new Aluno();
        var ambiente = Ambiente.builder()
                .nome("Sala 1")
                .capacidadeMaxima(5)
                .build();

        when(presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno)).thenReturn(Optional.empty());
        when(presencaRepository.countByAmbienteAndDataHoraSaidaIsNull(ambiente)).thenReturn(3L);

        service.registrarEntrada(aluno, ambiente);

        verify(presencaRepository, times(1)).save(any(RegistroPresenca.class));
    }

    @Test
    void deveLancarExcecaoAoTentarRegistrarSaidaSemRegistroAtivo() {
        var aluno = new Aluno();
        when(presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrarSaida(aluno))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Este aluno não possui registro de entrada ativo");
    }

    @Test
    void deveCalcularOcupacaoAtual() {
        var ambiente = Ambiente.builder().nome("Sala 1").build();
        when(presencaRepository.countByAmbienteAndDataHoraSaidaIsNull(ambiente))
                .thenReturn(4L);

        assertThat(service.calcularOcupacaoAtual(ambiente))
                .isEqualTo(4L);
    }

    @Test
    void deveListarAmbientes() {
        when(ambienteRepository.findAll()).thenReturn(List.of(new Ambiente()));

        assertThat(service.listarAmbientes()).hasSize(1);
        verify(ambienteRepository, times(1)).findAll();
    }
}
