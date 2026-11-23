package negocio;

import java.util.List;
import java.util.UUID;

public class Aluno {
    private UUID id;
    private String nome;
    private String email;
    private List<Disciplina> vetDisciplina;

    public  Aluno(){
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getNome() {
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
    public List<Disciplina> getVetDisciplina() {
        return vetDisciplina;
    }
    public void setVetDisciplina(List<Disciplina> vetDisciplina) {
        this.vetDisciplina = vetDisciplina;
    }

    @Override
    public String toString() {
        return "Aluno [id=" + id + ", nome=" + nome + ", email=" + email + "]";
    }

    
    

}
