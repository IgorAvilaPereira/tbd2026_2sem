## 🧩 CENÁRIO DO EXEMPLO

Vamos modelar duas entidades:

* `Cliente`: possui vários `Pedidos`
* `Pedido`: pertence a um `Cliente`

### Relacionamento:

**1 Cliente → N Pedidos**

---

## 📦 ESTRUTURA DO PROJETO (MAVEN)

### `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.exemplo</groupId>
    <artifactId>jpa-postgres-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- JPA API -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- Hibernate como implementação JPA -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.2.7.Final</version>
        </dependency>

        <!-- Driver PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.1</version>
        </dependency>
    </dependencies>
</project>
```

---

## 📄 `persistence.xml`

Crie este arquivo em:

📁 `src/main/resources/META-INF/persistence.xml`

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             version="3.0">
    <persistence-unit name="meuPU">
        <class>com.exemplo.Cliente</class>
        <class>com.exemplo.Pedido</class>

        <properties>
            <!-- Conexão PostgreSQL -->
            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/seu_banco"/>
            <property name="jakarta.persistence.jdbc.user" value="seu_usuario"/>
            <property name="jakarta.persistence.jdbc.password" value="sua_senha"/>

            <!-- Hibernate -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="create"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

> 🔐 Substitua `seu_banco`, `seu_usuario` e `sua_senha` pelos dados do seu PostgreSQL.

---

## 📁 CLASSES JPA

### `Cliente.java`

```java
package com.exemplo;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}
```

---

### `Pedido.java`

```java
package com.exemplo;

import jakarta.persistence.*;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
```

---

## ▶️ CLASSE PRINCIPAL: `Main.java`

```java
package com.exemplo;

import jakarta.persistence.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        // Criar cliente e pedidos
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos");

        Pedido pedido1 = new Pedido();
        pedido1.setDescricao("Notebook");
        pedido1.setCliente(cliente);

        Pedido pedido2 = new Pedido();
        pedido2.setDescricao("Impressora");
        pedido2.setCliente(cliente);

        cliente.setPedidos(Arrays.asList(pedido1, pedido2));

        // Persistência
        em.getTransaction().begin();
        em.persist(cliente); // Isso salva o cliente e os pedidos por causa do cascade
        em.getTransaction().commit();

        System.out.println("Cliente e pedidos salvos com sucesso!");

        em.close();
        emf.close();
    }
}
```

---

## 🧪 EXECUTANDO

1. Crie um banco vazio no PostgreSQL:

   ```sql
   CREATE DATABASE jpa_exemplo;
   ```

2. Atualize o `persistence.xml` com os dados desse banco.

3. Compile e execute o projeto (pode usar IntelliJ, Eclipse, VS Code ou CLI com Maven).

---

## 🗃️ O QUE SERÁ CRIADO NO BANCO?

O JPA + Hibernate com `hibernate.hbm2ddl.auto = create` vai criar:

* Tabela `cliente`
* Tabela `pedido`
* Chave estrangeira `cliente_id` na tabela `pedido`

---

## ✅ PRONTO PARA USAR

Esse projeto já está apto para ser expandido com:

* Consultas (`JPQL`, `Criteria`, ou SQL nativo)
* Stored Procedures (com `@NamedStoredProcedureQuery`)
* Integração REST (Spring Boot)
* Interface gráfica (JavaFX ou Swing)

