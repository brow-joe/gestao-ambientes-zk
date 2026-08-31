package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AlunoService;
import com.gestao.zk.application.service.PresencaService;
import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.userinterfaces.utils.NotificationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresencaViewModelTest {
    @Mock
    private AlunoService alunoService;

    @Mock
    private PresencaService presencaService;

    @InjectMocks
    private PresencaViewModel model;

    private MockedStatic<NotificationUtil> notification;

    @BeforeEach
    void setUp() {
        notification = Mockito.mockStatic(NotificationUtil.class);
        when(alunoService.listarTodos()).thenReturn(List.of());
        when(presencaService.listarAmbientes()).thenReturn(List.of());
        model.init();
    }

    @AfterEach
    void tearDown() {
        notification.close();
    }

    @Test
    void deveRetornarOcupacaoAtualCorretamente() {
        var ambiente = Ambiente.builder()
                .id(1L)
                .nome("Lab")
                .capacidadeMaxima(10)
                .build();
        when(presencaService.calcularOcupacaoAtual(ambiente)).thenReturn(4L);

        var ocupacao = model.getOcupacaoAtual(ambiente);
        assertThat(ocupacao).isEqualTo(4L);
        verify(presencaService, times(1)).calcularOcupacaoAtual(ambiente);
    }

    @Test
    void naoDeveRegistrarEntradaSeAlunoOuAmbienteForemNulos() {
        model.setAluno(null);
        model.setAmbiente(null);
        model.registrarEntrada();

        verify(presencaService, never()).registrarEntrada(any(), any());
        notification.verify(() -> NotificationUtil.warning(anyString()), times(1));
    }

    @Test
    void deveRegistrarEntradaComSucesso() {
        var aluno = Aluno.builder()
                .id(1L)
                .nome("João")
                .build();
        var ambiente = Ambiente.builder()
                .id(1L)
                .nome("Sala")
                .build();

        model.setAluno(aluno);
        model.setAmbiente(ambiente);
        model.registrarEntrada();

        verify(presencaService, times(1)).registrarEntrada(aluno, ambiente);
        verify(presencaService, atLeastOnce()).listarAmbientes();
        notification.verify(() -> NotificationUtil.success(anyString()), times(1));
    }

    @Test
    void deveTratarErroAoRegistrarEntrada() {
        var aluno = Aluno.builder()
                .id(1L)
                .nome("João")
                .build();
        var ambiente = Ambiente.builder()
                .id(1L)
                .nome("Sala")
                .build();

        model.setAluno(aluno);
        model.setAmbiente(ambiente);
        doThrow(new IllegalStateException("Capacidade esgotada")).when(presencaService).registrarEntrada(aluno, ambiente);

        model.registrarEntrada();

        verify(presencaService, times(1)).registrarEntrada(aluno, ambiente);
        notification.verify(() -> NotificationUtil.error(anyString()), times(1));
    }

    @Test
    void naoDeveRegistrarSaidaSeAlunoForNulo() {
        model.setAluno(null);
        model.registrarSaida();

        verify(presencaService, never()).registrarSaida(any());
        notification.verify(() -> NotificationUtil.warning(anyString()), times(1));
    }

    @Test
    void deveRegistrarSaidaComSucesso() {
        var aluno = Aluno.builder()
                .id(1L)
                .nome("Maria")
                .build();

        model.setAluno(aluno);
        model.registrarSaida();

        verify(presencaService, times(1)).registrarSaida(aluno);
        verify(presencaService, atLeastOnce()).listarAmbientes();
        notification.verify(() -> NotificationUtil.success(anyString()), times(1));
    }

    @Test
    void deveTratarErroAoRegistrarSaida() {
        var aluno = Aluno.builder()
                .id(1L)
                .nome("Maria")
                .build();

        model.setAluno(aluno);
        doThrow(new IllegalArgumentException("Sem registro ativo")).when(presencaService).registrarSaida(aluno);

        model.registrarSaida();

        verify(presencaService, times(1)).registrarSaida(aluno);
        notification.verify(() -> NotificationUtil.error(anyString()), times(1));
    }
}
