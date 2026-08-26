package apresentacao;

import java.util.UUID;

import org.bson.types.ObjectId;

import negocio.Responsavel;
import negocio.Tarefa;
import persistencia.TarefaDAO;

public class Main {
    public static void main(String[] args) {
        TarefaDAO tarefaDAO = new TarefaDAO();
        Tarefa tarefaNova = new Tarefa();
        tarefaNova.setDescricao("oi");
        // tarefaNova.setResponsavel(new Responsavel("Igor", "igor.pereira@riogrande.ifrs.edu.br", UUID.randomUUID().toString()));
        tarefaDAO.adicionar(tarefaNova);
        // tarefaDAO.remover(new ObjectId("681937d4d2b9da4c1b588665"));

        // Tarefa t = tarefaDAO.obter(new ObjectId("681937d4d2b9da4c1b588665"));
        // System.out.println(t.getResponsavel().getNome());
        // t.setDescricao("tcc do can");
        // tarefaDAO.atualizar(t);
      
    }
}