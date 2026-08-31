package com.gestao.zk.application.service;

import com.gestao.zk.domain.enumeration.TipoAmbiente;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.repository.AmbienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmbienteServiceTest {
    @Mock
    private AmbienteRepository repository;

    @InjectMocks
    private AmbienteService service;

    @Test
    void deveListarTodosAmbientes() {
        when(repository.findAll()).thenReturn(List.of(new Ambiente()));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveSalvarAmbiente() {
        var ambiente = Ambiente.builder()
                .nome("Lab 1")
                .tipo(TipoAmbiente.LABORATORIO)
                .capacidadeMaxima(20)
                .build();
        when(repository.save(ambiente)).thenReturn(ambiente);

        assertThat(service.salvar(ambiente).getNome())
                .isEqualTo(ambiente.getNome());
    }

    @Test
    void deveDeletarAmbiente() {
        var id = 1L;
        doNothing().when(repository).deleteById(id);
        service.deletar(id);
        verify(repository, times(1)).deleteById(id);
    }
}
