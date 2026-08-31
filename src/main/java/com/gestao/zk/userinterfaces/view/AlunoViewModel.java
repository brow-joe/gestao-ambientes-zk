package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AlunoService;
import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.userinterfaces.utils.NotificationUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AlunoViewModel {
    @WireVariable("alunoService")
    private AlunoService service;

    private Aluno aluno = new Aluno();
    private ListModelList<Aluno> alunos = new ListModelList<>();

    @Init
    public void init() {
        this.carregarAlunos();
    }

    @Command
    @NotifyChange({"aluno", "alunos"})
    public void salvar() {
        if (StringUtils.isAnyEmpty(
                aluno.getNome(),
                aluno.getMatricula(),
                aluno.getEmail()
        )) {
            NotificationUtil.warning("Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        try {
            service.salvar(aluno);
            NotificationUtil.success("Aluno salvo com sucesso!");

            this.novo();
        } catch (Exception e) {
            NotificationUtil.error(e.getMessage());
        } finally {
            this.carregarAlunos();
        }
    }

    @Command
    @NotifyChange("aluno")
    public void editar(@BindingParam("aluno") Aluno aluno) {
        this.aluno = Aluno.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .matricula(aluno.getMatricula())
                .email(aluno.getEmail())
                .build();
    }

    @Command
    @NotifyChange({"aluno", "alunos"})
    public void deletar(@BindingParam("id") Long id) {
        Messagebox.show(
                "Deseja realmente excluir este aluno?",
                "Confirmação",
                new Messagebox.Button[]{ Messagebox.Button.YES, Messagebox.Button.NO },
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            service.deletar(id);
                            NotificationUtil.success("Registro removido com sucesso!");

                            this.carregarAlunos();
                            BindUtils.postNotifyChange(null, null, this, "alunos");
                        } catch (Exception e) {
                            NotificationUtil.error("Não foi possível excluir o aluno.");
                        }
                    }
                }
        );
    }

    @Command
    @NotifyChange("aluno")
    public void novo() {
        this.aluno = new Aluno();
    }

    private void carregarAlunos() {
        this.alunos.clear();
        this.alunos.addAll(this.service.listarTodos());
    }
}
