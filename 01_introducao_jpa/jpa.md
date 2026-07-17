# JPA

## ✅ 1. Estrutura do Projeto

Você pode usar um projeto Java com **Maven** (mais fácil para gerenciar dependências).

### Exemplo de `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.exemplo</groupId>
    <artifactId>jpa-postgres</artifactId>
    <version>1.0</version>

    <dependencies>
        <!-- JPA -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- Hibernate (implementação JPA) -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.2.7.Final</version>
        </dependency>

        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.1</version>
        </dependency>

        <!-- JUnit opcional para testes -->
    </dependencies>
</project>
```

---

## 🗃️ 2. Tabela no PostgreSQL

Execute no seu banco PostgreSQL:

```sql
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);
```

---

## 📄 3. Classe Java (Entidade JPA)

```java
import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
```

---

## ⚙️ 4. Arquivo `persistence.xml`

Crie o arquivo dentro de:
`src/main/resources/META-INF/persistence.xml`

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             version="3.0">
    <persistence-unit name="meuPU">
        <class>Cliente</class>

        <properties>
            <!-- JDBC config -->
            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/seu_banco"/>
            <property name="jakarta.persistence.jdbc.user" value="seu_usuario"/>
            <property name="jakarta.persistence.jdbc.password" value="sua_senha"/>

            <!-- Hibernate config -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

> 🔐 Substitua `seu_banco`, `seu_usuario`, `sua_senha` pelas suas configurações.

---

## 🧪 5. Código para usar a JPA

```java
import jakarta.persistence.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        Cliente cliente = new Cliente();
        cliente.setNome("Maria da Silva");

        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();

        System.out.println("Cliente salvo com ID: " + cliente.getId());

        em.close();
        emf.close();
    }
}
```

---

## ✅ Resultado

Você verá no terminal algo como:

```sql
insert into clientes (nome) values (?)
```

E no banco, o novo cliente estará registrado na tabela.

---

Se você quer que o **JPA gere automaticamente o schema do banco de dados**, você pode configurar o `persistence.xml` com a propriedade correta para isso.


## ✅ Gerar o banco com JPA/Hibernate

A chave para isso está nesta propriedade:

```xml
<property name="hibernate.hbm2ddl.auto" value="create"/>
```

---

### 🧩 Opções para `hibernate.hbm2ddl.auto`

| Valor         | O que faz                                                      |
| ------------- | -------------------------------------------------------------- |
| `none`        | Não faz nada (padrão seguro)                                   |
| `validate`    | Valida as tabelas com base nas entidades (erro se faltar algo) |
| `update`      | Atualiza o schema do banco com base nas entidades (cuidado!)   |
| `create`      | Cria o schema do zero **toda vez que o app roda**              |
| `create-drop` | Cria o schema e **apaga** ao final da execução                 |

---

## 🛠️ Exemplo de `persistence.xml` para gerar o banco (PostgreSQL)

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             version="3.0">
    <persistence-unit name="meuPU">
        <class>Cliente</class>

        <properties>
            <!-- JDBC -->
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

> Substitua os valores de conexão com os corretos do seu PostgreSQL.

---

## 🧪 Resultado esperado

Ao rodar o seu app, o Hibernate:

* Vai criar as tabelas no banco **automaticamente** com base nas anotações das entidades (`@Entity`, `@Table`, `@Column` etc.).
* Vai logar os comandos SQL de criação no console se `show_sql = true`.

---

### 📁 Exemplo de entidade:

```java
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
}
```

Vai gerar algo como:

```sql
create table cliente (
    id serial primary key,
    nome varchar(255)
);
```

---

## ⚠️ Atenção

* `create` e `create-drop` **APAGAM** e **RECRIAM** as tabelas toda vez que o app roda.
* Use `update` se quiser manter os dados (mas ainda é perigoso para produção).
* Em produção, o ideal é gerar o schema uma vez (com `create`), exportar como SQL e aplicar manualmente no banco.

---

## 🧠 Dica Extra: Exportar o DDL para arquivo

Você também pode configurar para o Hibernate gerar o SQL DDL em um arquivo:

```xml
<property name="hibernate.hbm2ddl.auto" value="create"/>
<property name="hibernate.schema_export.directory" value="./"/>
<property name="hibernate.schema_export.filename" value="schema.sql"/>
```

Ou usar a linha de comando do Hibernate para exportar o schema.

---

## 🧠 Dica Final

* Use `hibernate.hbm2ddl.auto = update` no início para criar/atualizar tabelas automaticamente (ideal só para desenvolvimento).
* Use `validate` ou `none` em produção.
* Esse exemplo é ideal para projetos pequenos ou médios com PostgreSQL.
