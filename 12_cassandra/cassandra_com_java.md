# Como usar Apache Cassandra com Java

## 🧰 O que você precisa

### ✅ Requisitos:

1. **Java (JDK 11 ou superior)**
2. **Maven ou Gradle** (vamos usar Maven neste exemplo)
3. **Cassandra em execução** (pode ser via Docker)
4. **Driver oficial do Cassandra para Java**:

   * Fornecido pela [DataStax](https://docs.datastax.com/en/developer/java-driver/)

---

## 📦 1. Dependência Maven (DataStax Java Driver)

Adicione ao `pom.xml`:

```xml
<dependency>
  <groupId>com.datastax.oss</groupId>
  <artifactId>java-driver-core</artifactId>
  <version>4.17.0</version> <!-- Ou a versão mais recente -->
</dependency>
```

---

## 🚀 2. Conectando ao Cassandra

### Classe de conexão básica:

```java
import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class ConexaoCassandra {

    public static CqlSession conectar() {
        return CqlSession.builder()
            .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
            .withLocalDatacenter("datacenter1") // default do Cassandra local
            .build();
    }
}
```

---

## 🧪 3. Criando e usando tabelas

### Exemplo: Inserir e buscar dados da tabela `usuarios`

```java
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.util.UUID;

public class UsuarioRepository {

    private final CqlSession session;

    public UsuarioRepository(CqlSession session) {
        this.session = session;
    }

    public void inserirUsuario(UUID id, String nome, String email) {
        String query = "INSERT INTO mensageria.usuarios (id, nome, email, data_cadastro) " +
                       "VALUES (?, ?, ?, toTimestamp(now()))";

        PreparedStatement ps = session.prepare(query);
        session.execute(ps.bind(id, nome, email));
    }

    public void listarUsuarios() {
        ResultSet rs = session.execute("SELECT * FROM mensageria.usuarios");

        for (Row row : rs) {
            System.out.println("ID: " + row.getUuid("id"));
            System.out.println("Nome: " + row.getString("nome"));
            System.out.println("Email: " + row.getString("email"));
            System.out.println("Data: " + row.getInstant("data_cadastro"));
            System.out.println("---");
        }
    }
}
```

---

## 🧪 4. Programa principal

```java
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try (CqlSession session = ConexaoCassandra.conectar()) {
            UsuarioRepository repo = new UsuarioRepository(session);

            UUID id = UUID.randomUUID();
            repo.inserirUsuario(id, "João", "joao@email.com");

            System.out.println("Usuários cadastrados:");
            repo.listarUsuarios();
        }
    }
}
```

---

## 🧼 Encerrando a conexão

O `CqlSession` deve ser fechado corretamente (como usamos `try-with-resources`, isso já acontece).

---

## 🛠️ Extras que você pode explorar

* ⚙️ **Spring Boot com Cassandra** (para apps maiores)
* 📄 **Mapper API** (para usar classes anotadas, estilo JPA)

---

## 📚 Dicas úteis

* Cassandra **não usa auto-incremento** — use `UUID.randomUUID()` como IDs.
* Evite `SELECT *` em produção — Cassandra é projetado para consultas **bem específicas**.
* Planeje suas **queries antes de modelar** a tabela.

