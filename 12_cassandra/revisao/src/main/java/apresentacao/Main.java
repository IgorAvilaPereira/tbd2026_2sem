package apresentacao;

import java.util.UUID;

import negocio.Aluno;
import negocio.Disciplina;
import persistencia.AlunoDAO;
import persistencia.DisciplinaDAO;

public class Main {
    public static void main(String[] args) {
        // Aluno aluno = new Aluno();
        // aluno.setEmail("igor.pereira@riogrande.ifrs.edu.br");
        // aluno.setNome("Igor");
        // new AlunoDAO().inserir(aluno);

        Aluno igor = new AlunoDAO().obter(UUID.fromString("cb80d45d-8e53-42dd-94e5-4e7c7f1ee770"));
        
        // System.out.println(igor.getVetDisciplina().get(0).getNome());
        // System.out.println(igor.getVetDisciplina().get(1).getNome());
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        Disciplina logica = disciplinaDAO.obter(2);
        disciplinaDAO.matricularAluno(logica, igor);

    }
}