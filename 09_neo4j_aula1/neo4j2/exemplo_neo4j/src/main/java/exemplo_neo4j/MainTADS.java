package exemplo_neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class MainTADS {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        try (Session session = driver.session()) { 
            // add propriedade em uma disciplina em especifico
            // session.run("MATCH (d:Disciplina) WHERE d.sigla = 'TBD' SET d += {ementa: 'Neo4j'};");
            // // retornando todas as disciplinas
            // Result result = session.run("MATCH(d:Disciplina) RETURN d;");
            // while(result.hasNext()) {
            //     Record r = result.next();
            //     System.out.println(r.get(0).get("nome").asString());
            //     System.out.println(r.get(0).get("ementa").asString());
            // }

            // prerequisitos de uma disciplina especifica IBD
            Result result2 = session.run("""
                                       MATCH (d1)-[p:PREREQUISITO]->(d2) 
WHERE d1.sigla = 'IOB' 
MATCH (d2)-[p2:PREREQUISITO]->(d3)
RETURN d1,d2,d3,p,p2;""" 
            );
             while(result2.hasNext()) {
                Record r2 = result2.next();
                System.out.println(r2.get(2).asNode().get("nome").asString());
            }
        } finally {
            driver.close();
        }
    }
    
}
