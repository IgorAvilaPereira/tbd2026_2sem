package negocio;

import java.util.List;

import org.bson.types.ObjectId;

import persistencia.TarefaDAO;

public class TarefaController {
    private TarefaDAO tarefaDAO;

    public TarefaController(){
        this.tarefaDAO = new TarefaDAO();
    }

    public List<Tarefa> listar() {
        return this.tarefaDAO.listar();
    }

    public String remover(String id) {
        this.tarefaDAO.remover(new ObjectId(id));
        return "removido";
    }

}
