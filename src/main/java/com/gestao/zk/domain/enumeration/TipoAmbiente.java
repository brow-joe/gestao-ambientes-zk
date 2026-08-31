package com.gestao.zk.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoAmbiente {
    SALA_DE_AULA("Sala de Aula"),
    LABORATORIO("Laboratório"),
    SALA_DE_ESTUDOS("Sala de Estudos");

    private final String descricao;
}
