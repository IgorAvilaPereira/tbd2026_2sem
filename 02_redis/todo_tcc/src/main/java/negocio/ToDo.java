package negocio;

import java.time.LocalDate;
import java.util.UUID;

public class ToDo {
    private UUID id;
    private String titulo;
    private String texto;
    private Usuario responsavel; 
    private LocalDate data;
    private int prioridade;
    private byte arquivo[];
    private String base64;

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

  

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }



    public int getPrioridade() {
        return prioridade;
    }



    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    


    @Override
    public String toString() {
        return "ToDo [id=" + id + ", titulo=" + titulo + ", texto=" + texto + ", responsavel=" + responsavel + ", data="
                + data + ", prioridade=" + prioridade + "]";
    }



    public byte[] getArquivo() {
        return arquivo;
    }



    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }



    public void setBase64(String encodeToString) {
        this.base64 = encodeToString;
    }



    public String getBase64() {
        return base64;
    }

    
    
    

}
