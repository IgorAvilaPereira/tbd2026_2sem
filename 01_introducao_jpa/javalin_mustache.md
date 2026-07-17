# Javalin / Mustache

## ✅ O que vamos fazer:

* Usar o **microframework Javalin** para rodar um servidor web em Java.
* Integrar com o **template engine Mustache**.
* Criar uma página HTML dinâmica renderizada com dados Java.

---

## 📦 1. `pom.xml` com dependências

Se estiver usando Maven:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.exemplo</groupId>
  <artifactId>javalin-mustache-demo</artifactId>
  <version>1.0-SNAPSHOT</version>

  <dependencies>
    <!-- Javalin -->
    <dependency>
      <groupId>io.javalin</groupId>
      <artifactId>javalin</artifactId>
      <version>6.1.3</version>
    </dependency>

    <!-- Mustache template engine -->
    <dependency>
      <groupId>com.github.spullara.mustache.java</groupId>
      <artifactId>compiler</artifactId>
      <version>0.9.10</version>
    </dependency>
  </dependencies>
</project>
```

---

## 📁 2. Estrutura de diretórios esperada

```
src/
 └── main/
     ├── java/
     │    └── App.java
     └── resources/
          └── templates/
              └── hello.mustache
```

---

## ✨ 3. Template Mustache (`hello.mustache`)

📄 `src/main/resources/templates/hello.mustache`

```html
<!DOCTYPE html>
<html>
<head>
    <title>Hello Mustache</title>
</head>
<body>
    <h1>Olá, {{nome}}!</h1>
</body>
</html>
```

---

## ☕ 4. Código Java com Javalin + Mustache

📄 `src/main/java/App.java`

```java
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinMustache;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        // Configura o diretório dos templates
        JavalinMustache.configure(config -> {
            config.setDirectory("templates");
        });

        Javalin app = Javalin.create(config -> {
            config.fileRenderer(new JavalinMustache());
        }).start(7000);

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("nome", "Maria");

            ctx.render("hello.mustache", model);
        });
    }
}
```

---

## ▶️ 5. Executando

1. Compile e execute a aplicação.
2. Acesse: [http://localhost:7000](http://localhost:7000)
3. Você verá:
   **"Olá, Maria!"** — vindo do template `hello.mustache`.

---

## 🧠 Dica: Tornando dinâmico

Você pode deixar a rota dinâmica, como:

```java
app.get("/hello/{nome}", ctx -> {
    Map<String, Object> model = new HashMap<>();
    model.put("nome", ctx.pathParam("nome"));
    ctx.render("hello.mustache", model);
});
```

➡️ Exemplo: acessar `http://localhost:7000/hello/João`
Mostrará: **"Olá, João!"**

---

## ✅ Conclusão

Com **Javalin + Mustache**, você cria:

* Servidores HTTP leves e rápidos
* Templates dinâmicos com HTML + dados Java
* Ótimo para microsserviços com interface web simples ou sistemas leves
* [Exemplo Javalin/Mustache/JPA](https://github.com/IgorAvilaPereira/JavalinMustacheJPA)

## OFF-TOPIC: Javalin + JSON

### ✅ Exemplo básico: Retornando JSON com Javalin

```java
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/cliente", ctx -> {
            Cliente cliente = new Cliente(1L, "Maria");
            ctx.json(cliente);
        });
    }
}

class Cliente {
    public Long id;
    public String nome;

    public Cliente(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
```

---

### ▶️ Resultado (GET `/cliente`):

```json
{
  "id": 1,
  "nome": "Maria"
}
```

---

### 🔁 Exemplo: Retornando lista de objetos

```java
import java.util.List;

app.get("/clientes", ctx -> {
    List<Cliente> clientes = List.of(
        new Cliente(1L, "Maria"),
        new Cliente(2L, "João")
    );
    ctx.json(clientes);
});
```

---

### ✅ Por padrão, Javalin usa **Jackson** para serializar JSON

Você pode enviar objetos simples, listas, mapas, DTOs etc.

---

### ⛔ Dica: Evite campos `private` sem getter/setter

Jackson (usado internamente) exige:

* Campos públicos **ou**
* Getters públicos **ou**
* Anotações como `@JsonProperty` (caso use campos privados)

---

### ✅ Retornando JSON com status HTTP

```java
ctx.status(201).json(new Cliente(3L, "Ana"));
```

---

### ✅ Recebendo JSON do corpo da requisição (POST)

```java
app.post("/clientes", ctx -> {
    Cliente novo = ctx.bodyAsClass(Cliente.class);
    System.out.println("Recebido: " + novo.nome);
    ctx.status(201).json(novo);
});
```

➡️ Envie via POST um JSON como:

```json
{
  "id": 10,
  "nome": "Carlos"
}
```

---

### 🧠 Conclusão

Javalin facilita muito o uso de JSON:

* `ctx.json(obj)` → retorna JSON
* `ctx.bodyAsClass(MyClass.class)` → lê JSON do corpo
* Compatível com listas, mapas, DTOs, etc.
* Sem configuração extra (usa Jackson por padrão)
