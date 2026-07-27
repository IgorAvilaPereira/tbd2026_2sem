package negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pessoa {
    private String id;
    private String nome;
    private int nroJoelho;
    private LocalDateTime dataCirurgia;
    private Endereco endereco;

    

    public LocalDateTime getDataCirurgia() {
        return dataCirurgia;
    }


    public void setDataCirurgia(LocalDateTime dataCirurgia) {
        this.dataCirurgia = dataCirurgia;
    }


    public Pessoa(){
        this.id = UUID.randomUUID().toString();
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getNroJoelho() {
        return nroJoelho;
    }

    public void setNroJoelho(int nroJoelho) {
        this.nroJoelho = nroJoelho;
    }


    public Endereco getEndereco() {
        return endereco;
    }


    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }



  

    

}
