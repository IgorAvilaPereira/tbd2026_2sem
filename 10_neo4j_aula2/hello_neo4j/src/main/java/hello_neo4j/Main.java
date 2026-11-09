package hello_neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import static org.neo4j.driver.Values.parameters;

public class Main {

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));

        try (Session session = driver.session()) {

                    Result result2 = session.run("CREATE (p:Pessoa{cpf:$cpf, nome:$nome})",
                    parameters("cpf", "232323-2323", "nome", "kethelem"));
            System.out.println(result2.consume().counters().nodesCreated());

            // Adicionando uma pessoa
            Result result = session.run("MATCH(p:Pessoa) RETURN p;");
            for (org.neo4j.driver.Record r : result.list()) {
                System.out.println(r.get(0).asNode().get("nome").asString());
                System.out.println(r.get(0).asNode().get("idade").asInt());
            }
        

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
