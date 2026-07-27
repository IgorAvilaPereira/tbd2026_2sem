package negocio;

import java.time.LocalDate;
import java.util.UUID;

public class ToDo {
    private UUID id;
    private String titulo;
    private String texto;
    private Usuario responsavel; 
    private LocalDate data;

    public ToDo(){
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public String toString() {
        return "ToDo [id=" + id.toString() + ", titulo=" + titulo + ", texto=" + texto + ", responsavel=" + responsavel + "]";
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    
    
    

}
