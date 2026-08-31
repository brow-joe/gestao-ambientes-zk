package com.gestao.zk.domain.model;

import com.gestao.zk.domain.enumeration.TipoAmbiente;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ambientes")
public class Ambiente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAmbiente tipo;

    @Column(nullable = false)
    private Integer capacidadeMaxima;
}
