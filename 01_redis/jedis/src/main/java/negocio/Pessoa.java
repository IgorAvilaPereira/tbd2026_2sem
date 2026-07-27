package negocio;

import java.time.LocalDate;
import java.util.UUID;

public class Pessoa {
    private String id;
    private String cpf;
    private String nome;
    private Endereco endereco;
    private LocalDate dataNascimento;

   public Pessoa(){
        this.id = UUID.randomUUID().toString();
   } 

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String hsetFormat(){
        return "pessoa:"+this.id+" cpf "+this.cpf+ " nome"+this.nome;
    }
    
    
    

}
