package com.gestao.zk.application.service;

import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.repository.AlunoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Aluno salvar(Aluno aluno) {
        if (Objects.isNull(aluno.getId()) && repository.existsByMatricula(aluno.getMatricula())) {
            throw new IllegalArgumentException("Já existe um aluno cadastrado com a matrícula: " + aluno.getMatricula());
        }
        return repository.save(aluno);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
