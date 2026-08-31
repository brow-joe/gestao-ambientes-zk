package com.gestao.zk.application.service;

import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.model.RegistroPresenca;
import com.gestao.zk.domain.repository.AmbienteRepository;
import com.gestao.zk.domain.repository.RegistroPresencaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PresencaService {
    private final RegistroPresencaRepository presencaRepository;
    private final AmbienteRepository ambienteRepository;

    public void registrarEntrada(Aluno aluno, Ambiente ambiente) {
        presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno)
            .ifPresent(p -> {
                throw new IllegalStateException("O aluno já está presente no ambiente: " + p.getAmbiente().getNome());
            });
        if (calcularOcupacaoAtual(ambiente) >= ambiente.getCapacidadeMaxima()) {
            throw new IllegalStateException("Capacidade máxima do ambiente (" + ambiente.getCapacidadeMaxima() + " pessoas) atingida!");
        }
        presencaRepository.save(RegistroPresenca.builder()
                .aluno(aluno)
                .ambiente(ambiente)
                .dataHoraEntrada(LocalDateTime.now())
                .build());
    }

    public void registrarSaida(Aluno aluno) {
        var presenca = presencaRepository.findByAlunoAndDataHoraSaidaIsNull(aluno)
            .orElseThrow(() -> new IllegalArgumentException("Este aluno não possui registro de entrada ativo no momento."));
        presenca.setDataHoraSaida(LocalDateTime.now());
        presencaRepository.save(presenca);
    }

    public long calcularOcupacaoAtual(Ambiente ambiente) {
        return presencaRepository.countByAmbienteAndDataHoraSaidaIsNull(ambiente);
    }

    public List<Ambiente> listarAmbientes() {
        return ambienteRepository.findAll();
    }
}
