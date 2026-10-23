**Aula de Hoje**

* Apresentar cronograma

* Apresentar Trabalho 2

* Apresentar 2º bim.

* Neo4j (aula 1)


<!--
* [Neo4j (Aula 1)](https://github.com/IgorAvilaPereira/tbd2024_2sem/raw/main/slides/neo4j.pdf)

* [Neo4j (Aula 2)](https://github.com/IgorAvilaPereira/tbd2024_2sem/raw/main/slides/neo4j_cypher.pdf)

* [Código](https://github.com/IgorAvilaPereira/tbd2024_2sem/tree/main/codigos/neo4aula1)
-->

* **Instalação**
```
-- 1ª vez
sudo docker run \
--publish=7474:7474 --publish=7687:7687 \
--volume=$HOME/neo4j/data:/data \
--env=NEO4J_AUTH=neo4j/password \
--name neo4j neo4j

-- demais vezes
sudo docker start neo4j
sudo docker exec -it neo4j bash
```
Obs: Acessar por http://localhost:7474/

**login:** _neo4j_ e **senha:** _password_

```
-- criar um nó
CREATE(p:Pessoa{cpf: "111.111.111-11", nome: "joao", idade:20})
return p

-- retornar todas pessoas
MATCH(p:Pessoa) RETURN p

-- select
match(p:Pessoa) where p.cpf = "111.111.111-11"
return p

match(p:Pessoa) where p.cpf = "111.111.111-11" or ....
return p

match(p:Pessoa) where p.cpf = "111.111.111-11" and ....
return p

-- amigos dos meus amigos (eu sendo "Pedro"). Retorno: "João"
MATCH (:Pessoa{nome: "Pedro"})<-[:AMIGO]-()<-[:AMIGO]-(p) return p

-- criar uma aresta entre nodos

-- exemplo 1
MATCH (p1:Pessoa), (p2:Pessoa)
where p1.nome = "João" and p2.nome = "Maria" 
CREATE (p1)-[:AMIGO]->(p2)

-- exemplo 2
MATCH (p1:Pessoa), (p2:Pessoa)
where ID(p1) = 10 and ID(p2) = 11
CREATE (p1)-[:AMIGO]->(p2)

-- exemplo3 (criando os nodos e a aresta ao mesmo tempo)
CREATE (a:a {a: 'a'})-[r:a]->(b:a {a: 'a'})

-- aresta com atributo
MATCH (p1:Pessoa), (p2:Pessoa) 
WHERE p1.nome = "João" AND p2.nome = "Maria" 
CREATE (p1)-[:AMIGO1{desde:"19/11/2020"}]->(p2)

-- update
MATCH (p:Pessoa) where p.nome = "Pedro"
set p.nome = "Pedro silva"

-- delete o nodo
MATCH (p:Pessoa) where p.nome = "Pedro silva"
Delete p

-- deletar relacionamentos AMIGO de joão
MATCH (:Pessoa{nome: 'joao'})-[a:AMIGO]-()
DELETE a

-- delete o nodo e seus relacionamentos
MATCH (p:Pessoa) where p.nome = "Pedro silva"
detach Delete p

-- delete tudo
MATCH (n)
DETACH DELETE n
```

**Driver JAVA-NEO4J:**
```xml
<dependencies>
<dependency>
<groupId>org.neo4j.driver</groupId>
<artifactId>neo4j-java-driver</artifactId>
<version>4.4.0</version>
</dependency>
</dependencies>
```

```java
package view;
import model.Pessoa;
import org.neo4j.driver.*;
import java.time.LocalDate;
import java.util.stream.Collectors;
import static org.neo4j.driver.Values.parameters;

public class App {
    public static void main(String[] args) {

        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "neo4j*"));

        Pessoa pessoa = new Pessoa("222.222.222-02", "Maria",
                LocalDate.of(1993,10,25));

        try(Session session = driver.session()){

            // Adicionando uma pessoa
            Result result = session.run("CREATE (p:Pessoa{cpf:$cpf, nome:$nome, nascimento:$nascimento})",
                    parameters("cpf", pessoa.getCpf(), "nome", pessoa.getNome(),
                            "nascimento", pessoa.getNascimento()));
            System.out.println(result.consume().counters().nodesCreated());

            // Criar um relacionamento
            Result result = session.run("MATCH (p1:Pessoa{cpf:$cpf}),(p2:Pessoa{cpf:$cpf2})" +
                    "CREATE (p1)-[:AMIGO]->(p2)",
                    parameters("cpf", "111.111.111-01", "cpf2", "222.222.222-02"));
            System.out.println(result.consume().counters().relationshipsCreated());

            // Recuperando todas as pessoas
            Result result = session.run("MATCH (p:Pessoa) RETURN p.cpf, p.nome, p.nascimento");
            System.out.println(result.stream().map(record ->
                    new Pessoa(record.get(0).asString(),
                    record.get(1).asString(),
                    record.get(2).asLocalDate()))
                    .collect(Collectors.toList()));

          // Buscando os CPFs de todos os amigos de uma pessoa
          Result result = session.run("MATCH (p:Pessoa{cpf:$cpf})-[:AMIGO]->(p2) RETURN p2",
                    parameters("cpf", "111.111.111-01"));
            result.list().forEach(r -> System.out.println(r.get(0).asNode().values()));
        } finally {
            driver.close();
        }
    }
}
```

**Links Complementares:**

* https://neo4j.com/docs/cypher-manual/current/introduction/
* https://neo4j.com/docs/cypher-manual/current/clauses/create/
* https://github.com/santanche/lab2learn/blob/master/network/cypher/s01exercises/s01b-cypher-basics.md
* https://neo4j.com/developer/java/
* https://neo4j.com/docs/java-manual/current/get-started/

**Vídeos:**

* https://youtube.com/playlist?list=PLdvBzqdsNjSePpPN_9ro4TBAFr3xRGyuA&si=Jne-M3GEQUE3pIUW

&nbsp;
[Baixar todo o material da aula](https://download-directory.github.io/?url=http://github.com/IgorAvilaPereira/tbd2026_2sem/tree/main/./09_neo4j_aula1)
