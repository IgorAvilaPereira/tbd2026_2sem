
# Aula: Apache Cassandra NoSQL com Exemplos em Java

O **Apache Cassandra** é um banco de dados NoSQL distribuído e orientado a colunas largas (*wide-column store*), criado para oferecer alta disponibilidade, escalabilidade linear e altíssima performance de escrita sem ponto único de falha. Ele é ideal para sistemas que lidam com volumes massivos de dados em tempo real, como IoT, mensageria e telemetria.

---

## 1. Arquitetura e Conceitos Fundamentais

Diferente de bancos relacionais tradicionais, o Cassandra funciona em uma topologia de anel descentralizada onde todos os servidores (nós) têm o mesmo papel.

* **Cluster e Nós:** O conjunto de servidores que armazenam os dados forma o cluster, e cada servidor individual é chamado de nó. Se um nó cair, os outros continuam atendendo requisições normalmente.
* **Keyspace:** É o equivalente ao "Database" ou "Schema" no mundo SQL. Nele você define o fator de replicação (quantas cópias dos dados existirão em nós diferentes).
* **Tabela (Column Family):** Armazena linhas identificadas por uma chave primária. Cada linha pode ter um conjunto flexível de colunas.
* **Chave Primária (Primary Key):** Define onde o dado será gravado fisicamente no cluster e como ele será ordenado. É dividida em **Partition Key** (escolhe o servidor) e **Clustering Columns** (ordena os dados dentro daquela partição).
* **CQL (Cassandra Query Language):** É a linguagem de consulta do Cassandra. Sua sintaxe lembra muito o SQL padrão, mas não permite `JOINs`, subconsultas ou filtros em colunas que não façam parte da chave primária.

---

## 2. Modelagem de Dados (A Regra de Ouro)

Em bancos relacionais, modelamos primeiro as entidades e depois pensamos nas consultas. No Cassandra, fazemos o caminho inverso.

* **Modelagem orientada a consultas:** Você cria uma tabela específica para cada consulta que a sua aplicação precisa realizar.
* **Desnormalização intencional:** Duplicar dados em várias tabelas é uma prática recomendada e esperada para evitar leituras lentas em múltiplos nós.
* **Escrita ultra rápida:** O Cassandra grava dados em memória (*Memtable*) e em um log sequencial em disco (*CommitLog*) simultaneamente, sem precisar ler dados antigos antes de atualizar ou inserir.

---

## 3. Preparando o Ambiente (CQL)

Antes de escrever código Java, crie a estrutura básica no banco usando o terminal `cqlsh` (ferramenta de linha de comando do Cassandra).

```sql
-- 1. Criando o Keyspace para ambiente local (1 réplica)
CREATE KEYSPACE IF NOT EXISTS aula_cassandra 
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

-- 2. Selecionando o Keyspace
USE aula_cassandra;

-- 3. Criando uma tabela de logs de acesso por usuário
-- user_id é a Partition Key (agrupa os dados no mesmo nó)
-- data_acesso é a Clustering Column (ordena do mais recente para o mais antigo)
CREATE TABLE IF NOT EXISTS logs_acesso (
    user_id uuid,
    data_acesso timestamp,
    acao text,
    ip text,
    PRIMARY KEY ((user_id), data_acesso)
) WITH CLUSTERING ORDER BY (data_acesso DESC);
```

---

## 4. Conectando com Java (DataStax Driver)

Para integrar o Java ao Cassandra, utilizamos o driver oficial mantido pela DataStax (versão 4.x).

### Dependência Maven (`pom.xml`)

Adicione o driver principal ao seu projeto Maven:

```xml
<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>java-driver-core</artifactId>
    <version>4.18.1</version>
</dependency>
```

### Código Completo: Conexão, Inserção e Consulta

O objeto `CqlSession` é *thread-safe* e pesado para ser criado. Em aplicações reais, você deve instanciá-lo uma única vez (Singleton ou Bean no Spring) e reutilizá-lo durante todo o ciclo de vida do sistema.

```java
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.UUID;

public class AulaCassandraJava {

    public static void main(String[] args) {
        // 1. Abrindo a sessão com o cluster local
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("aula_cassandra")
                .build()) {

            System.out.println("Conectado ao Cassandra com sucesso!");

            UUID usuarioId = UUID.randomUUID();

            // 2. Inserindo dados usando PreparedStatement (evita SQL Injection e melhora performance)
            PreparedStatement insertStmt = session.prepare(
                "INSERT INTO logs_acesso (user_id, data_acesso, acao, ip) VALUES (?, ?, ?, ?)"
            );

            BoundStatement boundInsert1 = insertStmt.bind(
                usuarioId, Instant.now().minusSeconds(60), "LOGIN", "192.168.0.10"
            );
            BoundStatement boundInsert2 = insertStmt.bind(
                usuarioId, Instant.now(), "COMPRA_FINALIZADA", "192.168.0.10"
            );

            session.execute(boundInsert1);
            session.execute(boundInsert2);
            System.out.println("Logs gravados!");

            // 3. Consultando os dados pela Partition Key
            PreparedStatement selectStmt = session.prepare(
                "SELECT data_acesso, acao, ip FROM logs_acesso WHERE user_id = ?"
            );

            ResultSet rs = session.execute(selectStmt.bind(usuarioId));

            System.out.println("\nHistórico do usuário " + usuarioId + ":");
            for (Row row : rs) {
                Instant data = row.getInstant("data_acesso");
                String acao = row.getString("acao");
                String ip = row.getString("ip");

                System.out.printf("Data: %s | Ação: %s | IP: %s%n", data, acao, ip);
            }
        }
    }
}
```

---

## 5. Boas Práticas Essenciais

* **Sempre use `PreparedStatement`:** Ele compila a query no servidor do Cassandra uma única vez e envia apenas os parâmetros nas execuções seguintes, economizando rede e CPU.
* **Evite `ALLOW FILTERING` em produção:** Se o Cassandra pedir essa cláusula em uma consulta `SELECT`, significa que sua tabela foi mal modelada para aquela busca e o banco terá que varrer todos os nós do cluster.
* **Use `Instant` para datas:** O tipo `timestamp` do CQL mapeia diretamente para a classe `java.time.Instant` do Java moderno.

## Referências

* [Guia de Cassandra com Java no Baeldung](https://baeldung.com)

