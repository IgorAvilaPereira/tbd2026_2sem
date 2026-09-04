# Trabalho 2: Sistema de gerenciamento de eventos

* **MongoDB**: Armazena **eventos e participantes**, aproveitando a flexibilidade de JSON (eventos podem ter diferentes tipos de dados e anexos).

* **Neo4j**: Representa **relações entre participantes e eventos**, como "participa de", "organiza", permitindo consultas de rede social ou conexões.

* **Java**: Aplicação principal que faz CRUD nos eventos, gerencia usuários e conecta MongoDB + Neo4j.

---

## **2️⃣ Arquitetura do Sistema**

```
Java App
  ├─ MongoDB (Eventos & Pessoas)
  │    └─ Coleções: Eventos, Pessoas
  └─ Neo4j (Relacionamentos)
       └─ Nós: Pessoa, Evento
       └─ Relacionamentos: ORGANIZOU, PARTICIPOU
```

* **MongoDB** → Armazenamento de dados principais (documentos JSON).
* **Neo4j** → Consultas de relacionamento, grafos de participação e organização.
* **Java** → API ou console app que gerencia ambos os bancos.

---
<!--
## **3️⃣ Configuração**

### Dependências Maven (pom.xml)

```xml
<dependencies>
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongodb-driver-sync</artifactId>
        <version>4.11.0</version>
    </dependency>
    <dependency>
        <groupId>org.neo4j.driver</groupId>
        <artifactId>neo4j-java-driver</artifactId>
        <version>5.11.0</version>
    </dependency>
</dependencies>
```
---

## **4️⃣ Exemplo de Código Java**

### Conexão com MongoDB

```java
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBConnection {
    private MongoDatabase database;

    public MongoDBConnection() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        this.database = client.getDatabase("events_db");
    }

    public MongoCollection<Document> getEventsCollection() {
        return database.getCollection("events");
    }
}
```

---

### Conexão com Neo4j

```java
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public class Neo4jConnection implements AutoCloseable {
    private final Driver driver;

    public Neo4jConnection() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "senha"));
    }

    public void createUserNode(String userName) {
        try (Session session = driver.session()) {
            session.run("CREATE (u:User {name: $name})", 
                        Map.of("name", userName));
        }
    }

    public void createEventNode(String eventName) {
        try (Session session = driver.session()) {
            session.run("CREATE (e:Event {name: $name})",
                        Map.of("name", eventName));
        }
    }

    public void createRelationship(String userName, String eventName, String relation) {
        try (Session session = driver.session()) {
            session.run(
                "MATCH (u:User {name: $userName}), (e:Event {name: $eventName}) " +
                "CREATE (u)-[r:" + relation + "]->(e)",
                Map.of("userName", userName, "eventName", eventName)
            );
        }
    }

    @Override
    public void close() {
        driver.close();
    }
}
```

---

### Uso Integrado (MongoDB + Neo4j)

```java
public class App {
    public static void main(String[] args) {
        MongoDBConnection mongo = new MongoDBConnection();
        Neo4jConnection neo = new Neo4jConnection();

        // Criar evento no MongoDB
        Document event = new Document("name", "Java Conference")
                .append("date", "2025-12-01")
                .append("location", "São Paulo");
        mongo.getEventsCollection().insertOne(event);

        // Criar evento e usuário no Neo4j
        neo.createEventNode("Java Conference");
        neo.createUserNode("Igor Pereira");

        // Criar relacionamento
        neo.createRelationship("Igor Pereira", "Java Conference", "ATTENDS");

        System.out.println("Evento e relacionamento criados com sucesso!");
    }
}
```

---
-->

## **3️⃣ Funcionalidades Exigidas**

1. Dashboard em Java → interface gráfica ou API REST que combina dados de PostgreSQL, MongoDB e Neo4j. (1.0)

2. Neo4j: Quem participou do evento como ouvinte/participante e como organizador. (1.0)

3. Migrar um participante para organizador em **Neo4j** de um determinado evento, lembrando que em um evento posso ser organizador, e em outro evento posso ser participante ou, até mesmo, em um mesmo evento posso ser organizador e participante (simultaneamente) (1.0)

3. Filtros dinâmicos em MongoDB → eventos por local, data ou palavra-chave. (1.0)

5. Exportação JSON → integração com sistemas externos. (1.0)

6. Exportação para uma base de dados SQL (1.0)
