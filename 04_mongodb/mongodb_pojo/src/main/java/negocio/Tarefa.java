package negocio;

import org.bson.types.ObjectId;


public class Tarefa {
    private ObjectId id;
    private String descricao;
    private Responsavel responsavel;
    
    public Tarefa() {
        this.responsavel = new Responsavel();
    }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    
}
