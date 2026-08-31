package com.gestao.zk.userinterfaces.view;

import com.gestao.zk.application.service.AmbienteService;
import com.gestao.zk.domain.enumeration.TipoAmbiente;
import com.gestao.zk.domain.model.Ambiente;
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

import java.util.Objects;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class EspacoViewModel {
    @WireVariable("ambienteService")
    private AmbienteService service;

    private Ambiente espaco = new Ambiente();
    private ListModelList<Ambiente> espacos = new ListModelList<>();
    private TipoAmbiente[] tiposAmbiente = TipoAmbiente.values();

    @Init
    public void init() {
        this.carregarEspacos();
    }

    @Command
    @NotifyChange({"espaco", "espacos"})
    public void salvar() {
        if (StringUtils.isEmpty(espaco.getNome()) ||
                Objects.isNull(espaco.getTipo()) ||
                espaco.getCapacidadeMaxima() <= 0
        ) {
            NotificationUtil.warning("Por favor, preencha todos os campos obrigatórios corretamente.");
            return;
        }

        try {
            service.salvar(espaco);
            NotificationUtil.success("Espaço salvo com sucesso!");

            this.novo();
        } catch (Exception e) {
            NotificationUtil.error(e.getMessage());
        } finally {
            this.carregarEspacos();
        }
    }

    @Command
    @NotifyChange("espaco")
    public void editar(@BindingParam("espaco") Ambiente ambiente) {
        this.espaco = ambiente;
    }

    @Command
    @NotifyChange({"espaco", "espacos"})
    public void deletar(@BindingParam("id") Long id) {
        Messagebox.show(
                "Deseja realmente excluir este espaço?",
                "Confirmação",
                new Messagebox.Button[]{ Messagebox.Button.YES, Messagebox.Button.NO },
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            service.deletar(id);
                            NotificationUtil.success("Espaço removido com sucesso!");

                            this.carregarEspacos();
                            BindUtils.postNotifyChange(null, null, this, "espacos");
                        } catch (Exception e) {
                            NotificationUtil.error("Não foi possível excluir o espaço.");
                        }
                    }
                }
        );
    }

    @Command
    @NotifyChange("espaco")
    public void novo() {
        this.espaco = new Ambiente();
    }

    private void carregarEspacos() {
        this.espacos.clear();
        this.espacos.addAll(service.listarTodos());
    }
}
