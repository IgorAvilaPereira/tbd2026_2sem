package negocio;

import org.bson.types.ObjectId;

public class Responsavel {
    private ObjectId id;
    private String nome;
    private String email;
    private String senha;

    public Responsavel(){
        this.id = new ObjectId();
        this.nome = "";
        this.email =  "";
        this.senha = "";

    }

    public Responsavel(String nome) {
        this.nome = nome;
    }

    public Responsavel(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getNome() {
        if (this.nome == null) throw new IllegalArgumentException("Sem nome atribuído!");
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    


}
