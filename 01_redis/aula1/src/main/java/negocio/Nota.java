package negocio;

import java.time.LocalDateTime;
import java.util.UUID;

public class Nota {
    private UUID id;
    private String titulo;
    private String texto;
    private LocalDateTime dataHora;

    public Nota() {
        this.id = UUID.randomUUID();
        this.dataHora = LocalDateTime.now();
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
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    

    
    

}
