package com.gestao.zk.application.service;

import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {
    @Mock
    private AlunoRepository repository;

    @InjectMocks
    private AlunoService service;

    @Test
    void deveListarTodosAlunos() {
        when(repository.findAll()).thenReturn(List.of(new Aluno()));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoSalvarMatriculaDuplicada() {
        var aluno = Aluno.builder()
                .matricula("123")
                .nome("Teste")
                .email("t@t.com")
                .build();
        when(repository.existsByMatricula(aluno.getMatricula()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.salvar(aluno))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Já existe um aluno cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    void deveSalvarAlunoComSucesso() {
        var aluno = Aluno.builder()
                .matricula("123")
                .nome("Teste")
                .email("t@t.com")
                .build();
        when(repository.existsByMatricula(aluno.getMatricula()))
                .thenReturn(false);
        when(repository.save(aluno)).thenReturn(aluno);

        assertThat(service.salvar(aluno)).isNotNull();
        verify(repository, times(1)).save(aluno);
    }

    @Test
    void deveAtualizarAlunoComSucessoQuandoIdNaoForNuloMesmoComMatriculaExistente() {
        var aluno = Aluno.builder()
                .id(1L)
                .matricula("123")
                .nome("Teste Atualizado")
                .email("t@t.com")
                .build();

        when(repository.save(aluno)).thenReturn(aluno);

        assertThat(service.salvar(aluno)).isNotNull();
        verify(repository, never()).existsByMatricula(any());
        verify(repository, times(1)).save(aluno);
    }

    @Test
    void deveDeletarAlunoPorId() {
        var id = 1L;
        doNothing().when(repository).deleteById(id);
        service.deletar(id);
        verify(repository, times(1)).deleteById(id);
    }
}
