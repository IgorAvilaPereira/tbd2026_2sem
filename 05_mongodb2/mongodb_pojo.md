# MongoDB com Java Vanilla

Exemplo didático de utilização do **MongoDB Driver para Java**, utilizando classes **POJO (Plain Old Java Object)** e o padrão **DAO (Data Access Object)**.

A proposta é trabalhar diretamente com o driver oficial do MongoDB, sem utilizar frameworks como Spring Data MongoDB.

---

## 📚 Conteúdo

* [1. Dependência](#1-dependência)
* [2. Estrutura do projeto](#2-estrutura-do-projeto)
* [3. POJO — Aluno](#3-pojo--aluno)
* [4. Conexão com o MongoDB](#4-conexão-com-o-mongodb)
* [5. DAO](#5-dao)
* [6. CREATE — Inserir](#6-create--inserir)
* [7. READ — Consultar](#7-read--consultar)
* [8. UPDATE — Atualizar](#8-update--atualizar)
* [9. DELETE — Excluir](#9-delete--excluir)
* [10. Main — CRUD completo](#10-main--crud-completo)
* [11. Como os objetos Java são armazenados](#11-como-os-objetos-java-são-armazenados)
* [12. Resumo](#12-resumo)

---

# 1. Dependência

Para utilizar o MongoDB com Java, adicione o driver oficial ao `pom.xml`.

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.6.1</version>
</dependency>
```

O `mongodb-driver-sync` permite trabalhar com o MongoDB de forma síncrona:

```java
MongoClient
MongoDatabase
MongoCollection
```

---

# 2. Estrutura do projeto

Uma organização simples para o exemplo:

```text
src/
└── main/
    └── java/
        └── br/edu/ifrs/
            ├── Main.java
            ├── MongoDB.java
            ├── Aluno.java
            └── AlunoDAO.java
```

Responsabilidade de cada classe:

| Classe     | Responsabilidade                  |
| ---------- | --------------------------------- |
| `Aluno`    | Representa os dados do aluno      |
| `MongoDB`  | Configura a conexão com o MongoDB |
| `AlunoDAO` | Executa operações de persistência |
| `Main`     | Executa e demonstra o CRUD        |

---

# 3. POJO — Aluno

Um **POJO** é simplesmente uma classe Java utilizada para representar dados.

```java
public class Aluno {

    private String nome;
    private int idade;

    public Aluno() {
    }

    public Aluno(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                '}';
    }
}
```

### Por que existe um construtor vazio?

O driver utiliza o POJO para converter:

```text
Java → BSON
```

e:

```text
BSON → Java
```

Por isso, é importante disponibilizar um construtor sem parâmetros:

```java
public Aluno() {
}
```

---

# 4. Conexão com o MongoDB

Crie a classe `MongoDB.java`.

```java
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.*;

public class MongoDB {

    private final MongoClient client;
    private final MongoDatabase database;

    public MongoDB() {

        var pojoCodecProvider =
                PojoCodecProvider.builder()
                        .automatic(true)
                        .build();

        var codecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(pojoCodecProvider)
                );

        var settings =
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(
                                        "mongodb://localhost:27017"
                                )
                        )
                        .codecRegistry(codecRegistry)
                        .build();

        client = MongoClients.create(settings);

        database = client.getDatabase("escola");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        client.close();
    }
}
```

A parte mais importante para trabalhar com POJOs é:

```java
PojoCodecProvider.builder()
        .automatic(true)
        .build();
```

O `PojoCodecProvider` permite ao driver fazer a conversão entre objetos Java e documentos BSON.

---

# 5. DAO

Agora vamos criar a classe responsável pelo acesso aos dados.

`AlunoDAO.java`:

```java
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

public class AlunoDAO {

    private final MongoCollection<Aluno> alunos;

    public AlunoDAO(MongoDB mongo) {

        alunos = mongo.getDatabase()
                .getCollection("alunos", Aluno.class);
    }

    // CREATE
    public void inserir(Aluno aluno) {
        alunos.insertOne(aluno);
    }

    // READ
    public void listar() {

        for (Aluno aluno : alunos.find()) {
            System.out.println(aluno);
        }
    }

    // READ por nome
    public Aluno buscarPorNome(String nome) {

        return alunos.find(
                eq("nome", nome)
        ).first();
    }

    // UPDATE
    public void atualizarIdade(
            String nome,
            int novaIdade) {

        alunos.updateOne(
                eq("nome", nome),
                new com.mongodb.client.model.Updates()
                        .set("idade", novaIdade)
        );
    }

    // DELETE
    public void excluir(String nome) {

        alunos.deleteOne(
                eq("nome", nome)
        );
    }
}
```

Há uma pequena melhoria possível no `update`, utilizando o método estático `set`.

Uma versão mais limpa é:

```java
import static com.mongodb.client.model.Updates.set;
```

e:

```java
alunos.updateOne(
        eq("nome", nome),
        set("idade", novaIdade)
);
```

Assim, o DAO completo fica:

```java
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class AlunoDAO {

    private final MongoCollection<Aluno> alunos;

    public AlunoDAO(MongoDB mongo) {

        alunos = mongo.getDatabase()
                .getCollection("alunos", Aluno.class);
    }

    // CREATE
    public void inserir(Aluno aluno) {
        alunos.insertOne(aluno);
    }

    // READ
    public void listar() {

        for (Aluno aluno : alunos.find()) {
            System.out.println(aluno);
        }
    }

    // READ por nome
    public Aluno buscarPorNome(String nome) {

        return alunos.find(
                eq("nome", nome)
        ).first();
    }

    // UPDATE
    public void atualizarIdade(
            String nome,
            int novaIdade) {

        alunos.updateOne(
                eq("nome", nome),
                set("idade", novaIdade)
        );
    }

    // DELETE
    public void excluir(String nome) {

        alunos.deleteOne(
                eq("nome", nome)
        );
    }
}
```

---

# 6. CREATE — Inserir

Para inserir um aluno:

```java
Aluno aluno = new Aluno(
        "João",
        20
);

dao.inserir(aluno);
```

Internamente:

```java
alunos.insertOne(aluno);
```

O driver transforma o objeto Java em um documento BSON.

---

# 7. READ — Consultar

## Listar todos

```java
dao.listar();
```

O DAO executa:

```java
for (Aluno aluno : alunos.find()) {
    System.out.println(aluno);
}
```

---

## Buscar um aluno

Podemos procurar pelo nome:

```java
Aluno aluno = dao.buscarPorNome("João");

System.out.println(aluno);
```

O filtro utilizado é:

```java
eq("nome", "João")
```

que corresponde aproximadamente à consulta MongoDB:

```javascript
db.alunos.findOne({
    nome: "João"
})
```

---

# 8. UPDATE — Atualizar

Para alterar a idade:

```java
dao.atualizarIdade(
        "João",
        21
);
```

O DAO executa:

```java
alunos.updateOne(
        eq("nome", "João"),
        set("idade", 21)
);
```

Equivalente, conceitualmente, a:

```javascript
db.alunos.updateOne(
    { nome: "João" },
    { $set: { idade: 21 } }
)
```

---

# 9. DELETE — Excluir

Para excluir um aluno:

```java
dao.excluir("João");
```

O DAO executa:

```java
alunos.deleteOne(
        eq("nome", "João")
);
```

Equivalente a:

```javascript
db.alunos.deleteOne({
    nome: "João"
})
```

---

# 10. Main — CRUD completo

Agora podemos juntar tudo em um exemplo.

`Main.java`:

```java
public class Main {

    public static void main(String[] args) {

        MongoDB mongo = new MongoDB();

        AlunoDAO dao = new AlunoDAO(mongo);

        // =========================================
        // CREATE
        // =========================================

        System.out.println("=== CREATE ===");

        dao.inserir(
                new Aluno("João", 20)
        );

        dao.inserir(
                new Aluno("Maria", 22)
        );

        dao.inserir(
                new Aluno("Pedro", 19)
        );


        // =========================================
        // READ
        // =========================================

        System.out.println("\n=== READ ===");

        dao.listar();


        // =========================================
        // READ - buscar por nome
        // =========================================

        System.out.println("\n=== BUSCAR ===");

        Aluno aluno =
                dao.buscarPorNome("Maria");

        System.out.println(aluno);


        // =========================================
        // UPDATE
        // =========================================

        System.out.println("\n=== UPDATE ===");

        dao.atualizarIdade(
                "Maria",
                23
        );

        dao.listar();


        // =========================================
        // DELETE
        // =========================================

        System.out.println("\n=== DELETE ===");

        dao.excluir("Pedro");

        dao.listar();


        // =========================================
        // ENCERRAR
        // =========================================

        mongo.close();
    }
}
```

---

# 11. Como os objetos Java são armazenados

Quando fazemos:

```java
Aluno aluno =
        new Aluno("João", 20);

dao.inserir(aluno);
```

temos um objeto Java:

```text
Aluno
 ├── nome = "João"
 └── idade = 20
```

O `PojoCodecProvider` permite que o driver transforme esse objeto em BSON.

No MongoDB, teremos algo semelhante a:

```json
{
    "_id": ObjectId("..."),
    "nome": "João",
    "idade": 20
}
```

O `_id` é criado pelo MongoDB quando não fornecemos um identificador.

---

## BSON → POJO

Quando executamos:

```java
Aluno aluno =
        dao.buscarPorNome("João");
```

o processo ocorre no sentido contrário:

```text
MongoDB
   ↓
BSON
   ↓
PojoCodecProvider
   ↓
Aluno
```

O resultado é um objeto Java:

```java
Aluno
```

---

# 12. CRUD completo

O exemplo implementa as quatro operações fundamentais:

| CRUD       | MongoDB                | Java                                   |
| ---------- | ---------------------- | -------------------------------------- |
| **Create** | `insertOne()`          | `dao.inserir()`                        |
| **Read**   | `find()` / `findOne()` | `dao.listar()` / `dao.buscarPorNome()` |
| **Update** | `updateOne()`          | `dao.atualizarIdade()`                 |
| **Delete** | `deleteOne()`          | `dao.excluir()`                        |

Fluxo geral:

```text
              Java
                │
                ▼
             POJO
             Aluno
                │
                ▼
              DAO
                │
                ▼
       MongoDB Java Driver
                │
                ▼
              BSON
                │
                ▼
            MongoDB
```

---

# 🎯 Exemplo completo em uma visão

```text
Main
 │
 │ new Aluno("João", 20)
 ▼
Aluno
 │
 ▼
AlunoDAO
 │
 ├── inserir()
 ├── listar()
 ├── buscarPorNome()
 ├── atualizarIdade()
 └── excluir()
 │
 ▼
MongoCollection<Aluno>
 │
 ▼
MongoDB
```

A principal ideia é perceber que **não precisamos manipular documentos BSON diretamente** para realizar o CRUD.

Em vez de trabalhar com:

```java
Document
```

podemos trabalhar diretamente com:

```java
Aluno
```

graças ao:

```java
PojoCodecProvider
```

Isso permite utilizar o MongoDB com uma abordagem orientada a objetos, mantendo o projeto simples e próximo do Java puro.

