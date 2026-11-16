package negocio;

import java.util.List;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "exemplo_cassandra", name = "pessoa")
public class Pessoa {
    @PartitionKey
    private String cpf;
    private String nome;
    private LocalDate nascimento;
    private List<String> gostos;

    public Pessoa() {
    }

    public Pessoa(String cpf, String nome, LocalDate dataNascimento, List<String> gostos) {
        this.cpf = cpf;
        this.nome = nome;
        this.nascimento = dataNascimento;
        this.gostos = gostos;
    }

    @PartitionKey
    public String getCpf() {
        return this.cpf;
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

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public List<String> getGostos() {
        return gostos;
    }

    public void setGostos(List<String> gostos) {
        this.gostos = gostos;
    }

    @Override
    public String toString() {
        return "Pessoa [cpf=" + cpf + ", nome=" + nome + ", nascimento=" + nascimento + ", gostos=" + gostos + "]";
    }

    
}