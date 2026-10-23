package persistencia;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerfilDAO {

    public void deletarTudo(){
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            session.run("MATCH(p) DETACH DELETE p;");
        } finally {
            driver.close();
        }

    }

    public void seguir(int perfilOrigem, int perfilDestino) {
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            session.run(
                    "MATCH(p1: Perfil), (p2: Perfil) WHERE ID(p1) = $p1 and ID(p2) = $p2 CREATE(p1)-[:SEGUE]->(p2);",
                    parameters("p1", perfilOrigem, "p2", perfilDestino));
        } finally {
            driver.close();
        }

    }

    public void inserir(String nome) {
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            session.run("CREATE(p:Perfil {nome:$nome});",
                    parameters("nome", nome));
        } finally {
            driver.close();
        }
    }

    private Driver criaConexao() {
        return GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));
    }

    public void adicionarPublicacao(int id, String texto) {
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            Result result = session.run("CREATE(p:Publicacao {texto: $texto}) RETURN ID(p)",
                    parameters("texto", texto));
            if (result.hasNext()) {
                int publicacaoID = result.next().get(0).asInt();
                session.run(
                        "MATCH(p1: Perfil), (p2: Publicacao) WHERE ID(p1) = $id and ID(p2) = $publicacaoID CREATE(p1)-[:PUBLICOU]->(p2);",
                        parameters("id", id, "publicacaoID", publicacaoID));

            }

        } finally {
            driver.close();
        }
    }

    public void desfazer(int id1, int id2) {
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            session.run("MATCH (p1:Perfil)-[s:SEGUE]->(p2:Perfil) where ID(p1) = $id1 and ID(p2) = $id2 DELETE s", parameters("id1", id1, "id2", id2));
        }
        finally {
            driver.close();
        }
    }

    public void adicionarPerfil(String nome) {
        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            session.run("CREATE (p:Perfil{nome:$nome});", parameters("nome", nome));
        }
        finally {
            driver.close();
        }
    }

    public void recomendacao(int id) {
        String cypher = "MATCH(p1:Perfil) where ID(p1) = $id \n" + //
                        "MATCH(p2:Perfil) \n" + //
                        "MATCH(p3:Perfil)\n" + //
                        "MATCH (p1)-[s1:SEGUE]->(p2)-[s2:SEGUE]->(p3) where  ID(p3) <> $id \n" + //
                        "MATCH (p3) WHERE NOT EXISTS ((p1)-[:SEGUE]->(p3))\n" + //
                        "RETURN p3;";

        Driver driver = criaConexao();
        try (Session session = driver.session()) {
            List<Record> records = session.run(cypher, parameters("id", id)).list();
            for (Record record : records) {
                System.out.println(record.get(0).asNode().id()  + ":"+record.get(0).get("nome").asString());
            }           
        }
        finally {
            driver.close();
        }
    
    }



}
