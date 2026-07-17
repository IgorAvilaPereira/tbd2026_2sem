package apresentacao;

import java.util.List;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import negocio.Aluno;
import persistencia.AlunoDAO;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        
        AlunoDAO alunoDAO = new AlunoDAO(emf);

        // adicionar
        Aluno jaime = new Aluno();
        jaime.setNome("Jaime");       
        alunoDAO.adicionar(jaime);

        // deletando...
        alunoDAO.remover(Integer.toUnsignedLong(1));
        alunoDAO.listar().forEach(p -> System.out.println(p.getNome()));
      

    }
}
