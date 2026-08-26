package negocio;

import org.bson.types.ObjectId;

public class Musica {
    private ObjectId id;
    private String titulo;
    private int duracao;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public int getDuracao() {
        return duracao;
    }
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    @Override
    public String toString() {
        return "Musica [id=" + id + ", titulo=" + titulo + ", duracao=" + duracao + "]";
    }
    

}
