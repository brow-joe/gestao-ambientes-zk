package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AlunoService;
import com.gestao.zk.domain.model.Aluno;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoViewModelTest {
    @Mock
    private AlunoService service;

    @InjectMocks
    private AlunoViewModel model;

    private MockedStatic<NotificationUtil> notification;

    @BeforeEach
    void setUp() {
        notification = Mockito.mockStatic(NotificationUtil.class);
        when(service.listarTodos()).thenReturn(List.of());
        model.init();
    }

    @AfterEach
    void tearDown() {
        notification.close();
    }

    @Test
    void deveConfigurarNovoAlunoCorretamente() {
        model.novo();
        assertThat(model.getAluno()).isNotNull();
        assertThat(model.getAluno().getId()).isNull();
    }

    @Test
    void naoDeveSalvarSeCamposEstiveremVazios() {
        model.setAluno(new Aluno());
        model.salvar();

        verify(service, never()).salvar(any());
        notification.verify(() -> NotificationUtil.warning(anyString()), times(1));
    }

    @Test
    void deveSalvarAlunoComSucesso() {
        var aluno = Aluno.builder()
                .nome("Carlos")
                .matricula("123")
                .email("carlos@mail.com").build();

        model.setAluno(aluno);
        model.salvar();

        verify(service, times(1)).salvar(aluno);
        verify(service, atLeastOnce()).listarTodos();
        notification.verify(() -> NotificationUtil.success(anyString()), times(1));
    }

    @Test
    void deveTratarErroAoSalvar() {
        var aluno = Aluno.builder()
                .nome("Jose")
                .matricula("456")
                .email("jose@mail.com").build();

        model.setAluno(aluno);
        doThrow(new RuntimeException("Erro genérico")).when(service).salvar(aluno);
        model.salvar();

        verify(service, times(1)).salvar(aluno);
        notification.verify(() -> NotificationUtil.error(anyString()), times(1));
    }

    @Test
    void deveEditarAlunoClonandoPropriedades() {
        var id = 1L;
        var nome = "Ana";
        var matricula = "789";
        var email = "ana@mail.com";

        model.editar(Aluno.builder()
                .id(id)
                .nome(nome)
                .matricula(matricula)
                .email(email)
                .build());

        var aluno = model.getAluno();
        assertThat(aluno.getId()).isEqualTo(id);
        assertThat(aluno.getNome()).isEqualTo(nome);
        assertThat(aluno.getMatricula()).isEqualTo(matricula);
        assertThat(aluno.getEmail()).isEqualTo(email);
    }
}
