package persistencia;

import negocio.Aluno;
import negocio.Disciplina;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;


import static org.neo4j.driver.Values.parameters;

public class DisciplinaDAO {

    public Disciplina obter(int id) {
        Disciplina disciplina = new Disciplina();
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (d:Disciplina) where ID(d) = $id RETURN d;", parameters("id", id));
            if (result.hasNext()) {
                Record r = result.next();
                Node node = r.get(0).asNode();
                disciplina.setId(node.id());
                disciplina.setNome(node.get("nome").asString());
                disciplina.setHoras(node.get("horas").asInt());
                disciplina.setSemestre(Integer.parseInt(node.get("semestre").asString()));
                disciplina.setSigla(node.get("sigla").asString());
                System.out.println(disciplina);
            }

        } finally {
            driver.close();
        }
        return disciplina;

    }

    public void matricularAluno(Disciplina disciplina, Aluno aluno) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));

        try (Session session = driver.session()) {

            Result result0 = session.run("MATCH (a: Aluno) where a.uuid = $uuid return a;",
                    parameters("uuid", aluno.getId().toString()));
            if (!result0.hasNext()) {
                Result result = session.run("CREATE(a: Aluno {uuid: $uuid, nome: $nome, email: $email});",
                        parameters("uuid", aluno.getId().toString(), "nome", aluno.getNome(), "email",
                                aluno.getEmail()));
                result.consume().counters().nodesCreated();
            }

            Result result2 = session.run("MATCH (d:Disciplina{sigla:$sigla}),(a:Aluno{uuid:$uuid})" +
                    "CREATE (a)-[:MATRICULADO]->(d)",
                    parameters("sigla", disciplina.getSigla().trim(), "uuid", aluno.getId().toString()));
            System.out.println(result2.consume().counters().relationshipsCreated());

        } finally {
            driver.close();
        }

    }

}
