package com.gestao.zk.application.service;

import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.repository.AmbienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AmbienteService {
    private final AmbienteRepository repository;

    public List<Ambiente> listarTodos() {
        return repository.findAll();
    }

    public Ambiente salvar(Ambiente ambiente) {
        return repository.save(ambiente);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
