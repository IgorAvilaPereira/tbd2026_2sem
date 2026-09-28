## Aggregation com Java Driver

Imagine a coleção `vendas` com documentos como:

```json
{
    "produto": "Notebook",
    "categoria": "Informática",
    "quantidade": 2,
    "preco": 3500,
    "cidade": "Rio Grande"
}
```

### 1. Dependência Maven

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.6.0</version>
</dependency>
```

### 2. Conexão

```java
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

public class Main {

    public static void main(String[] args) {

        try (MongoClient mongoClient =
                     MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("loja");

            MongoCollection<Document> vendas =
                    db.getCollection("vendas");

            // aggregation aqui
        }
    }
}
```

---

# 3. Primeiro exemplo: `$match`

No MongoDB Shell:

```javascript
db.vendas.aggregate([
    {
        $match: {
            cidade: "Rio Grande"
        }
    }
])
```

No Java:

```java
import static com.mongodb.client.model.Aggregates.*;

var resultado = vendas.aggregate(
        List.of(
                match(
                        Filters.eq("cidade", "Rio Grande")
                )
        )
);

for (Document doc : resultado) {
    System.out.println(doc.toJson());
}
```

Precisamos dos imports:

```java
import com.mongodb.client.model.Filters;

import java.util.List;
```

A ideia é:

```text
MongoDB                         Java

$match                  →      match()
cidade: "Rio Grande"    →      Filters.eq(...)
```

---

# 4. `$group` + `$sum`

Na aula, temos:

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",
            quantidadeTotal: {
                $sum: "$quantidade"
            }
        }
    }
])
```

No Java Driver:

```java
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;

var resultado = vendas.aggregate(
        List.of(
                group(
                        "$cidade",
                        sum("quantidadeTotal", "$quantidade")
                )
        )
);
```

Completo:

```java
for (Document doc : resultado) {
    System.out.println(doc.toJson());
}
```

Resultado:

```json
{
    "_id": "Rio Grande",
    "quantidadeTotal": 16
}
```

e:

```json
{
    "_id": "Pelotas",
    "quantidadeTotal": 10
}
```

---

# 5. `$group` com vários cálculos

Aqui começa a ficar interessante.

Queremos:

```text
cidade
quantidade total
preço médio
maior preço
menor preço
```

No MongoDB:

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",

            quantidadeTotal: {
                $sum: "$quantidade"
            },

            precoMedio: {
                $avg: "$preco"
            },

            maiorPreco: {
                $max: "$preco"
            },

            menorPreco: {
                $min: "$preco"
            }
        }
    }
])
```

No Java:

```java
var resultado = vendas.aggregate(
        List.of(
                group(
                        "$cidade",

                        sum(
                                "quantidadeTotal",
                                "$quantidade"
                        ),

                        avg(
                                "precoMedio",
                                "$preco"
                        ),

                        max(
                                "maiorPreco",
                                "$preco"
                        ),

                        min(
                                "menorPreco",
                                "$preco"
                        )
                )
        )
);
```

---

# 6. `$sort`

No MongoDB:

```javascript
db.vendas.aggregate([
    {
        $sort: {
            preco: -1
        }
    }
])
```

No Java:

```java
import static com.mongodb.client.model.Sorts.*;

var resultado = vendas.aggregate(
        List.of(
                sort(descending("preco"))
        )
);
```

Para crescente:

```java
sort(ascending("preco"))
```

Para decrescente:

```java
sort(descending("preco"))
```

---

# 7. `$limit`

MongoDB:

```javascript
db.vendas.aggregate([
    {
        $sort: {
            preco: -1
        }
    },
    {
        $limit: 3
    }
])
```

Java:

```java
var resultado = vendas.aggregate(
        List.of(
                sort(descending("preco")),
                limit(3)
        )
);
```

Isso produz:

```text
1º produto mais caro
2º produto mais caro
3º produto mais caro
```

---

# 8. `$match` + `$group` + `$sort`

Agora um pipeline mais próximo de uma consulta real.

> Quantidade vendida por cidade, considerando somente produtos de Informática, ordenada da maior para a menor quantidade.

MongoDB:

```javascript
db.vendas.aggregate([
    {
        $match: {
            categoria: "Informática"
        }
    },
    {
        $group: {
            _id: "$cidade",
            quantidade: {
                $sum: "$quantidade"
            }
        }
    },
    {
        $sort: {
            quantidade: -1
        }
    }
])
```

Java:

```java
var resultado = vendas.aggregate(
        List.of(

                match(
                        Filters.eq(
                                "categoria",
                                "Informática"
                        )
                ),

                group(
                        "$cidade",
                        sum(
                                "quantidade",
                                "$quantidade"
                        )
                ),

                sort(
                        descending("quantidade")
                )
        )
);
```

---

# 9. Calculando `quantidade × preco`

Aqui existe uma diferença importante.

Na aula, o MongoDB usa:

```javascript
$multiply: [
    "$quantidade",
    "$preco"
]
```

No Java Driver podemos utilizar `Projections.computed()`:

```java
import static com.mongodb.client.model.Projections.*;

var resultado = vendas.aggregate(
        List.of(
                project(
                        fields(
                                include("produto"),
                                include("quantidade"),
                                include("preco"),

                                computed(
                                        "faturamento",
                                        new Document(
                                                "$multiply",
                                                List.of(
                                                        "$quantidade",
                                                        "$preco"
                                                )
                                        )
                                )
                        )
                )
        )
);
```

Um documento:

```json
{
    "produto": "Notebook",
    "quantidade": 2,
    "preco": 3500
}
```

vira:

```json
{
    "produto": "Notebook",
    "quantidade": 2,
    "preco": 3500,
    "faturamento": 7000
}
```

---

# 10. O exemplo mais interessante: relatório completo

Vamos transformar o exemplo completo da sua aula em Java.

Queremos:

```text
cidade
quantidade vendida
faturamento
preço médio
maior preço

ordenado pelo faturamento
```

No MongoDB:

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",

            quantidadeProdutos: {
                $sum: "$quantidade"
            },

            faturamento: {
                $sum: {
                    $multiply: [
                        "$quantidade",
                        "$preco"
                    ]
                }
            },

            precoMedio: {
                $avg: "$preco"
            },

            maiorPreco: {
                $max: "$preco"
            }
        }
    },

    {
        $sort: {
            faturamento: -1
        }
    },

    {
        $project: {
            _id: 0,
            cidade: "$_id",
            quantidadeProdutos: 1,
            faturamento: 1,
            precoMedio: 1,
            maiorPreco: 1
        }
    }
])
```

Em Java:

```java
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;

public class Main {

    public static void main(String[] args) {

        try (MongoClient client =
                     MongoClients.create(
                             "mongodb://localhost:27017")) {

            MongoDatabase db =
                    client.getDatabase("loja");

            MongoCollection<Document> vendas =
                    db.getCollection("vendas");

            var pipeline = List.of(

                    group(
                            "$cidade",

                            sum(
                                    "quantidadeProdutos",
                                    "$quantidade"
                            ),

                            sum(
                                    "faturamento",
                                    new Document(
                                            "$multiply",
                                            List.of(
                                                    "$quantidade",
                                                    "$preco"
                                            )
                                    )
                            ),

                            avg(
                                    "precoMedio",
                                    "$preco"
                            ),

                            max(
                                    "maiorPreco",
                                    "$preco"
                            )
                    ),

                    sort(
                            descending("faturamento")
                    ),

                    project(
                            fields(
                                    excludeId(),

                                    computed(
                                            "cidade",
                                            "$_id"
                                    ),

                                    include(
                                            "quantidadeProdutos"
                                    ),

                                    include(
                                            "faturamento"
                                    ),

                                    include(
                                            "precoMedio"
                                    ),

                                    include(
                                            "maiorPreco"
                                    )
                            )
                    )
            );

            var resultado =
                    vendas.aggregate(pipeline);

            for (Document doc : resultado) {
                System.out.println(
                        doc.toJson()
                );
            }
        }
    }
}
```

## 11. Uma forma didática de explicar para os alunos

Eu usaria esta correspondência:

| MongoDB       | Java Driver              |
| ------------- | ------------------------ |
| `aggregate()` | `collection.aggregate()` |
| `$match`      | `match()`                |
| `$group`      | `group()`                |
| `$sum`        | `sum()`                  |
| `$avg`        | `avg()`                  |
| `$min`        | `min()`                  |
| `$max`        | `max()`                  |
| `$sort`       | `sort()`                 |
| `$limit`      | `limit()`                |
| `$project`    | `project()`              |
| `$unwind`     | `unwind()`               |
| `$lookup`     | `lookup()`               |
| `$count`      | `count()`                |

O ponto principal para o aluno é perceber que **não existe uma nova lógica de consulta**. O Java Driver está simplesmente construindo o mesmo pipeline que seria escrito no MongoDB Shell.

Por exemplo:

```javascript
$group: {
    _id: "$cidade",
    total: {
        $sum: "$quantidade"
    }
}
```

vira:

```java
group(
    "$cidade",
    sum(
        "total",
        "$quantidade"
    )
)
```

Ou seja:

```text
MongoDB Shell
      ↓
Aggregation Pipeline
      ↓
Java Driver
      ↓
Aggregation Pipeline
```

Isso combina diretamente com a ideia central do material: cada estágio recebe o resultado do estágio anterior e produz o resultado para o próximo estágio. 
