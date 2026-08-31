package com.gestao.zk.domain.repository;

import com.gestao.zk.domain.model.Aluno;
import com.gestao.zk.domain.model.Ambiente;
import com.gestao.zk.domain.model.RegistroPresenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistroPresencaRepository extends JpaRepository<RegistroPresenca, Long> {
    Optional<RegistroPresenca> findByAlunoAndDataHoraSaidaIsNull(Aluno aluno);
    long countByAmbienteAndDataHoraSaidaIsNull(Ambiente ambiente);
}
