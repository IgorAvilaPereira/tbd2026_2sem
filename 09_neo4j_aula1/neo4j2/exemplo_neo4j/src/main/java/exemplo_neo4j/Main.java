package exemplo_neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import static org.neo4j.driver.Values.parameters;


public class Main {
    public static void main(String[] args) {

        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));


        try (Session session = driver.session()) { 
            session.run("CREATE(p:Pessoa{cpf:$cpf, nome:$nome})", parameters("cpf", "8888888888",  "nome", "david"));
        } finally {
            driver.close();
        }
    }
}