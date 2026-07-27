
# Guia Rápido: Javalin com Java e Template Mustache
O **Javalin** é um framework web micro (microframework) focado na simplicidade, leveza e alta performance. Ele é construído sobre o servidor Jetty e brilha tanto na criação de APIs REST quanto na renderização de páginas no servidor (*Server-Side Rendering*) utilizando motores de template como o **Mustache**.
---
## 1. Configurando o Projeto (Maven)
Para usar o Javalin com suporte a JSON e renderização de páginas com o Mustache, adicione as seguintes dependências no seu arquivo de configuração.

### Dependências no `pom.xml````xml
<dependencies>
    <!-- Core do Javalin -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin</artifactId>
        <version>6.1.3</version>
    </dependency>

    <!-- Plugin de Renderização do Javalin (Obrigatório para usar templates) -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin-rendering</artifactId>
        <version>6.1.3</version>
    </dependency>

    <!-- Motor de Template Mustache -->
    <dependency>
        <groupId>com.github.spullara.mustache.java</groupId>
        <artifactId>compiler</artifactId>
        <version>0.9.10</version>
    </dependency>

    <!-- Logger obrigatório para o ciclo de vida do servidor Jetty -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.12</version>
    </dependency>

    <!-- Jackson para conversão automática de objetos para JSON (APIs) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.17.0</version>
    </dependency>
</dependencies>
```
---
## 2. O famoso "Hello World" com Mustache
Por padrão, o Javalin procura os arquivos de template dentro da pasta de recursos do sistema (`src/main/resources`). 
### Passo 1: Criar o arquivo HTML/MustacheCrie o arquivo no caminho: `src/main/resources/templates/index.mustache````html
<!DOCTYPE html>
<html>
<head>
    <title>Minha Aula</title>
</head>
<body>
    <h1>Olá, {{nome}}!</h1>
    <p>Seja bem-vindo à aula de desenvolvimento web.</p>
</body>
</html>
```
### Passo 2: Código Java para Renderizar a Página```java
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinMustache;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        // 1. Registra o Mustache como o renderizador oficial para extensões .mustache
        JavalinRenderer.register(new JavalinMustache(), ".mustache");

        // 2. Inicializa o servidor Javalin
        Javalin app = Javalin.create().start(8080);

        // 3. Rota que injeta dados no HTML e renderiza na tela
        app.get("/boas-vindas", ctx -> {
            // Cria os dados que serão substituídos nas tags {{...}} do HTML
            Map<String, Object> modelo = Map.of("nome", "Turma de Java");
            
            // Renderiza o arquivo localizado em src/main/resources/templates/index.mustache
            ctx.render("templates/index.mustache", modelo);
        });
    }
}
```
---
## 3. Manipulando Rotas, Parâmetros e Listas no Mustache
O Mustache permite renderizar não apenas variáveis simples, mas também iterar sobre listas (laços de repetição) de forma limpa.

### Arquivo de Template: `src/main/resources/templates/produtos.mustache````html
<!DOCTYPE html>
<html>
<head>
    <title>Loja Didática</title>
</head>
<body>
    <h2>Lista de Produtos Disponíveis:</h2>
    <ul>
        {{#produtos}}
        <li><strong>{{nome}}</strong> - R\$ {{preco}}</li>
        {{/produtos}}
        {{^produtos}}
        <li>Nenhum produto cadastrado no momento.</li>
        {{/produtos}}
    </ul>
</body>
</html>
```
### Código Java:```java
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinMustache;
import java.util.List;
import java.util.Map;

public class LojaApp {
    public static void main(String[] args) {
        JavalinRenderer.register(new JavalinMustache(), ".mustache");
        Javalin app = Javalin.create().start(8080);

        // Simulando uma lista de dados (podem vir de um banco ou do Redis)
        List<Produto> listaDeProdutos = List.of(
            new Produto("Teclado Mecânico", 250.0),
            new Produto("Mouse Gamer", 120.0),
            new Produto("Monitor 24'", 890.0)
        );

        app.get("/vitrine", ctx -> {
            // Passa a lista encapsulada em um mapa associado à tag {{#produtos}}
            Map<String, Object> modelo = Map.of("produtos", listaDeProdutos);
            ctx.render("templates/produtos.mustache", modelo);
        });
    }
}

// Record auxiliar para estruturar os dados do produto
record Produto(String nome, double preco) {}
```
---
## 4. Por que usar Javalin com Mustache para fins didáticos?

1.  **Separação clara de conceitos**: Os alunos entendem a diferença real entre dados (*Model*) e apresentação (*View*) de forma explícita, sem a injeção oculta do Spring.

2.  **Sintaxe baseada em tags simples**: O Mustache é conhecido como um sistema de templates *"logic-less"* (sem lógica interna). Ele não permite códigos Java ou lógicas complexas direto no HTML (como o JSP permitia), forçando o aluno a preparar e tratar todos os dados corretamente na camada do controlador Java antes de renderizar.

3.  **Configuração Instantânea**: O servidor embutido inicia em milissegundos e as páginas modificadas são fáceis de testar atualizando o navegador.

