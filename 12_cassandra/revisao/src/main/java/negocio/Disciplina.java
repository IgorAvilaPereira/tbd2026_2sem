package negocio;

public class Disciplina {
    private Long id;
    private int horas;
    private String nome;
    private int semestre;
    private String sigla;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getHoras() {
        return horas;
    }
    public void setHoras(int horas) {
        this.horas = horas;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    @Override
    public String toString() {
        return "Disciplina [id=" + id + ", horas=" + horas + ", nome=" + nome + ", semestre=" + semestre + ", sigla="
                + sigla + "]";
    }

    

}
