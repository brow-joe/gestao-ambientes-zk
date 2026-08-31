package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AlunoService;
import com.gestao.zk.application.service.PresencaService;
import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.userinterfaces.utils.NotificationUtil;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModelList;

import java.util.Objects;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PresencaViewModel {
    @WireVariable
    private AlunoService alunoService;
    @WireVariable
    private PresencaService presencaService;

    private Aluno aluno;
    private Ambiente ambiente;

    private ListModelList<Aluno> alunos = new ListModelList<>();
    private ListModelList<Ambiente> ambientes = new ListModelList<>();

    @Init
    public void init() {
        this.carregarAlunos();
        this.carregarAmbientes();
    }

    @Command
    @NotifyChange({"ambientes"})
    public void registrarEntrada() {
        if (Objects.isNull(aluno) || Objects.isNull(ambiente)) {
            NotificationUtil.warning("Selecione um aluno e um ambiente para continuar.");
            return;
        }

        try {
            presencaService.registrarEntrada(aluno, ambiente);
            NotificationUtil.success("Entrada registrada com sucesso!");
        } catch (Exception e) {
            NotificationUtil.error(e.getMessage());
        } finally {
            this.carregarAmbientes();
        }
    }

    @Command
    @NotifyChange({"ambientes"})
    public void registrarSaida() {
        if (Objects.isNull(aluno)) {
            NotificationUtil.warning("Selecione o aluno para registrar a saída.");
            return;
        }

        try {
            presencaService.registrarSaida(aluno);
            NotificationUtil.success("Saída registrada com sucesso!");
        } catch (Exception e) {
            NotificationUtil.error(e.getMessage());
        } finally {
            this.carregarAmbientes();
        }
    }

    public long getOcupacaoAtual(Ambiente ambiente) {
        return presencaService.calcularOcupacaoAtual(ambiente);
    }

    private void carregarAlunos() {
        this.alunos.clear();
        this.alunos.addAll(this.alunoService.listarTodos());
    }

    private void carregarAmbientes() {
        this.ambientes.clear();
        this.ambientes.addAll(presencaService.listarAmbientes());
    }
}
