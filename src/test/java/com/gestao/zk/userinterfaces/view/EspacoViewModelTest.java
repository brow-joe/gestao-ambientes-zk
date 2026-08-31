package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AmbienteService;
import com.gestao.zk.domain.enumeration.TipoAmbiente;
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
class EspacoViewModelTest {
    @Mock
    private AmbienteService service;

    @InjectMocks
    private EspacoViewModel model;

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
    void deveConfigurarNovoEspacoCorretamente() {
        model.novo();
        assertThat(model.getEspaco()).isNotNull();
        assertThat(model.getEspaco().getId()).isNull();
    }

    @Test
    void deveCarregarTiposAmbienteCorretamente() {
        var tipos = model.getTiposAmbiente();
        assertThat(tipos).isEqualTo(TipoAmbiente.values());
    }

    @Test
    void naoDeveSalvarSeCamposEstiveremInvalidos() {
        model.setEspaco(Ambiente.builder().nome("").tipo(TipoAmbiente.LABORATORIO).capacidadeMaxima(10).build());
        model.salvar();

        model.setEspaco(Ambiente.builder().nome("Lab").tipo(null).capacidadeMaxima(10).build());
        model.salvar();

        model.setEspaco(Ambiente.builder().nome("Lab").tipo(TipoAmbiente.LABORATORIO).capacidadeMaxima(0).build());
        model.salvar();

        verify(service, never()).salvar(any());
        notification.verify(() -> NotificationUtil.warning(anyString()), times(3));
    }

    @Test
    void deveSalvarEspacoComSucesso() {
        var ambiente = Ambiente.builder()
                .nome("Sala 101")
                .tipo(TipoAmbiente.SALA_DE_AULA)
                .capacidadeMaxima(30)
                .build();

        model.setEspaco(ambiente);
        model.salvar();

        verify(service, times(1)).salvar(ambiente);
        verify(service, atLeastOnce()).listarTodos();
        notification.verify(() -> NotificationUtil.success(anyString()), times(1));
    }

    @Test
    void deveTratarErroAoSalvar() {
        var ambiente = Ambiente.builder()
                .nome("Lab 2")
                .tipo(TipoAmbiente.LABORATORIO)
                .capacidadeMaxima(15)
                .build();

        model.setEspaco(ambiente);
        doThrow(new RuntimeException("Erro de persistência")).when(service).salvar(ambiente);
        model.salvar();

        verify(service, times(1)).salvar(ambiente);
        notification.verify(() -> NotificationUtil.error(anyString()), times(1));
    }

    @Test
    void deveEditarEspacoCorretamente() {
        var ambiente = Ambiente.builder()
                .id(1L)
                .nome("Estudo A")
                .tipo(TipoAmbiente.SALA_DE_ESTUDOS)
                .capacidadeMaxima(5)
                .build();

        model.editar(ambiente);

        assertThat(model.getEspaco()).isEqualTo(ambiente);
        assertThat(model.getEspaco().getId()).isEqualTo(1L);
        assertThat(model.getEspaco().getNome()).isEqualTo("Estudo A");
    }
}
