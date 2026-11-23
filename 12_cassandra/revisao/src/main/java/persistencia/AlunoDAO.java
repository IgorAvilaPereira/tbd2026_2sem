package persistencia;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import static org.neo4j.driver.Values.parameters;

import negocio.Aluno;
import negocio.Disciplina;

public class AlunoDAO {

    public AlunoDAO() {
    }

    public Aluno obter(UUID id) {
        Aluno aluno = new Aluno();
        Cluster cluster = null;    
               
        try {

            cluster = Cluster.builder().addContactPoint("127.0.0.1")
                    .build();
            Session session = cluster.connect("exemplo_cassandra");
             PreparedStatement cql = session
                    .prepare("SELECT * FROM aluno where id = ?;");
            BoundStatement bound = cql.bind(id);
            List<Row> vet = session.execute(bound).all();
            Iterator<Row> iterator = vet.iterator();
            if (iterator.hasNext()) {
                Row row = iterator.next();
                aluno.setId(row.getUUID("id"));
                aluno.setNome(row.getString("nome"));
                aluno.setEmail(row.getString("email"));
                aluno.setVetDisciplina(this.disciplinasMatriculadas(aluno));

            } 


        } finally {
            if (cluster != null)
                cluster.close();

           
        }
        return aluno;
    }

    public List<Disciplina> disciplinasMatriculadas(Aluno aluno){
        List<Disciplina> vetDisciplina = new ArrayList<Disciplina>();
         Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));
        try (org.neo4j.driver.Session session = driver.session()) {
            Result result = session.run("MATCH p=(a:Aluno)-[r:MATRICULADO]->(d:Disciplina) WHERE a.uuid = $uuid RETURN d;", parameters("uuid", aluno.getId().toString()));
            while (result.hasNext()) {
                Record r = result.next();
                Node node = r.get(0).asNode();
                Disciplina disciplina = new Disciplina();
                disciplina.setId(node.id());
                disciplina.setNome(node.get("nome").asString());
                disciplina.setHoras(node.get("horas").asInt());
                disciplina.setSemestre(Integer.parseInt(node.get("semestre").asString()));
                disciplina.setSigla(node.get("sigla").asString());
                vetDisciplina.add(disciplina);
            }

        } finally {
            driver.close();
        }
        return vetDisciplina;
    }
    public List<Aluno> listar() {
        Cluster cluster = null;
        List<Aluno> vetAluno = new ArrayList<Aluno>();
        try {
            cluster = Cluster.builder().addContactPoint("127.0.0.1")
                    .build();
            Session session = cluster.connect("exemplo_cassandra");
            List<Row> vet = session.execute("SELECT * FROM aluno").all();
            Iterator<Row> iterator = vet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Aluno aluno = new Aluno();
                aluno.setId(row.getUUID("id"));
                aluno.setNome(row.getString("nome"));
                aluno.setEmail(row.getString("email"));
                vetAluno.add(aluno);
            }
        } finally {
            if (cluster != null)
                cluster.close();
        }
        return vetAluno;
    }

    public void inserir(Aluno aluno) {
        Cluster cluster = null;
        try {
            cluster = Cluster.builder().addContactPoint("127.0.0.1")
                    .build();
            Session session = cluster.connect("exemplo_cassandra");
            PreparedStatement cqlInsert = session
                    .prepare("insert into exemplo_cassandra.aluno(id, nome, email) VALUES (?,?,?);");
            BoundStatement bound = cqlInsert.bind(UUID.randomUUID(), aluno.getNome(), aluno.getEmail());
            session.execute(bound);
        } finally {
            if (cluster != null)
                cluster.close();
        }
    }
}
