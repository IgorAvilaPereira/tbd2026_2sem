package br.edu.ifrs.minicurso.springsolidapi.service.interfaces;

import br.edu.ifrs.minicurso.springsolidapi.dto.AlunoDTO;
import br.edu.ifrs.minicurso.springsolidapi.model.Aluno;

public interface AlunoService extends IService<Aluno, AlunoDTO> {
    Aluno save(Aluno aluno);
}
