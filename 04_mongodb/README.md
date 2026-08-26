https://cheatsheets.zip/mongodb

```sh
-- 1ª vez
sudo docker run --name some-mongo -p 27017:27017 -d mongo:latest
sudo docker exec -it some-mongo mongosh
-- obs: o sudo pode ser opcional
-- demais vezes
sudo docker start some-mongo
sudo docker exec -it some-mongo mongosh
-- obs: o sudo pode ser opcional
``` 

<!--
[Slides](https://github.com/IgorAvilaPereira/tbd2025_2sem/raw/main/slides/mongodb.pdf)

[Código](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/mongodb1)

[Código](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/mongodb)


[Instalação/Rodando](https://github.com/IgorAvilaPereira/tbd2025_2sem/wiki/Setup#mongodb)
-->

<!--
[Trabalho 3 - MongoDB](https://github.com/IgorAvilaPereira/tbd2025_2sem/wiki/Trabalhos#trabalho-3---mongodb)

[Exemplo - Aula Mongo Pojo](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/TBD_MongoAula2)

[Exemplo - Aula - POJO](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/mongo-aula2)
-->

**Material Complementar:**

* https://www.mongodb.com/docs/manual/reference/operator/query/

* https://www.mongodb.com/docs/manual/reference/operator/query-comparison/

* :exclamation: https://www.mongodb.com/docs/manual/core/aggregation-pipeline/

* :exclamation: https://www.mongodb.com/docs/manual/aggregation/


* **Mongo Java Driver com Pojo:**

```java
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    public static void main(String[] args) {

        String uri = "mongodb://localhost:27017";
        String dbname = "test";
        String collectionName = "tarefas";

        Tarefa tarefaNova = new Tarefa();
        tarefaNova.setTitulo("Estudar MongoDB");
        tarefaNova.setDescricao("Aprender integração Java com MongoDB");

        ConnectionString connectionString = new ConnectionString(uri);

        CodecRegistry pojoCodecRegistry =
                fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry codecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                );

        MongoClientSettings clientSettings =
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .codecRegistry(codecRegistry)
                        .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {

            MongoDatabase db = mongoClient.getDatabase(dbname);

            MongoCollection<Tarefa> tarefaCollection =
                    db.getCollection(collectionName, Tarefa.class);

            tarefaCollection.insertOne(tarefaNova);

            System.out.println("Tarefa inserida com sucesso!");
        }
    }
}


import org.bson.types.ObjectId;

public class Tarefa {

    private ObjectId id;
    private String titulo;
    private String descricao;
    private boolean concluida;

    public Tarefa() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    // demais getters e setters...
}

```  
   * https://www.mongodb.com/developer/languages/java/java-mapping-pojos/
   * https://mongodb.github.io/mongo-java-driver/3.7/driver/getting-started/quick-start-pojo/
   * https://mongodb.github.io/mongo-java-driver/3.7/bson/pojos/
   * https://mongodb.github.io/mongo-java-driver/3.7/driver/tutorials/

**Outros:**

* https://www.mongodb.com/docs/manual/tutorial/insert-documents/

* https://www.mongodb.com/docs/manual/tutorial/query-documents/

* https://www.mongodb.com/docs/manual/tutorial/query-arrays/

* https://www.mongodb.com/docs/manual/reference/operator/query/#std-label-query-selectors

* https://www.mongodb.com/docs/manual/core/aggregation-pipeline/#std-label-aggregation-pipeline-examples

* https://studio3t.com/knowledge-base/articles/mongodb-aggregation-framework/

* https://www.mongodb.com/docs/manual/applications/data-models/

<!--* [Código - Aula](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/TBD_MongoAula1)-->

* **Principais Comandos**

```
-- Criando/Selecionar um BD
use minhaBase
-- retornar collection users
1) db.users
2) db.getCollection("users")
-- inserir
db.users.insert({"name": "codigo"}) -- legacy
db.users.insertOne({"name": "codigo"}) -- current
-- listar todos
db.users.find()
-- Update
db.users.update({"name": "Igor"},{"name": "novo"}) -- legacy
db.users.updateOne({"name": "Igor"},{$set:{"name":"Igor Pereira"}}) -- current
-- Delete
db.users.remove({"name": "codigo"}) -- legacy
db.users.deleteOne({"name": "codigo"}) -- current
```
**Links:**

* https://www.mongodb.com/docs/manual/crud/

* https://www.mongodb.com/docs/manual/tutorial/insert-documents/

* https://www.mongodb.com/docs/manual/tutorial/query-documents/#query-documents

### MongoDB Java Driver

* [MongoDB Java Driver - Quick Start](https://www.mongodb.com/docs/drivers/java/sync/current/quick-start/)
 
  * **Connect to MongoDB:** https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/connect/#std-label-connect-to-mongodb

  * **URI de Conexão**
```java
String uri = "mongodb://localhost:27017";
```
* [MongoDB Java Driver - Reference](https://www.mongodb.com/docs/drivers/java/sync/current/quick-reference/)

* **MongoDB Java Driver - Examples**
  * https://github.com/Banco-II-2020-1/crud-mongo
  * https://github.com/mongodb-developer/java-quick-start/tree/master/src/main/java/com/mongodb/quickstart

<!--
* [Exemplo - Aula](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/aula-mongodb)

* [Exemplo - Aula - POJO](https://github.com/IgorAvilaPereira/tbd2025_2sem/tree/main/codigos/mongo-aula2)
-->

* **Instalação via Maven**
```maven
    <dependencies>
        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongodb-driver-sync</artifactId>
            <version>4.9.1</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.7</version>
        </dependency>
    </dependencies>
```

**References:**

* **insertOne:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/insertOne/

* **updateOne:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/updateOne/

* **deleteOne:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/deleteOne/

* **findOne:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/findOne/

* **findMany:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/find/

* **count:** https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/count/

* :exclamation: https://mongodb.github.io/mongo-java-driver/3.7/driver/getting-started/quick-start-pojo/

* :exclamation: **java-mapping-pojos:** https://www.mongodb.com/developer/languages/java/java-mapping-pojos/

**Extras:**

* [Tutorial](https://www.mongodb.com/docs/manual/tutorial/)

* https://www.mongodb.com/docs/guides/crud/read_queries/

* :exclamation: https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/crud/read-operations/

* :exclamation: https://www.mongodb.com/docs/manual/reference/operator/

* :exclamation: https://www.mongodb.com/docs/manual/reference/sql-comparison/

* [robo 3t](https://studio3t.com/download-studio3t-free/)

```
-- updateOne
try {
   db.users.updateOne(
      { "nome" : "max" },
      { $set: { "nome" : "maximiliano" } }
   );
} catch (e) {
   print(e);
}

-- removeOne
try {
   db.users.deleteOne( { "_id" : ObjectId("634dea8922941a787369e69a")} );
} catch (e) {
   print(e);
}

-- find 
db.users.find({"nome": "igor"})
db.users.find({nome:{$in:["igor", "erick"]}})
```



[Baixar todo o material da aula](https://download-directory.github.io/?url=http://github.com/IgorAvilaPereira/tbd2026_2sem/tree/main/./04_mongodb)
