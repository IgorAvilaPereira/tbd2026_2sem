# Aula — Aggregation no MongoDB

## 1. O que é Aggregation?

No MongoDB, **Aggregation** é o mecanismo utilizado para:

* filtrar documentos;
* agrupar dados;
* calcular valores;
* ordenar resultados;
* selecionar campos;
* transformar documentos;
* realizar estatísticas;
* combinar informações de diferentes coleções.

A ideia principal é trabalhar com um **pipeline**.

```text
Documentos
    ↓
$match
    ↓
$group
    ↓
$sort
    ↓
$project
    ↓
Resultado
```

Cada etapa recebe o resultado da etapa anterior e produz um novo resultado.

---

# 2. Nossa coleção de exemplo

Vamos imaginar uma coleção chamada `vendas`.

```javascript
db.vendas.insertMany([
    {
        produto: "Notebook",
        categoria: "Informática",
        quantidade: 2,
        preco: 3500,
        cidade: "Rio Grande"
    },
    {
        produto: "Mouse",
        categoria: "Informática",
        quantidade: 10,
        preco: 80,
        cidade: "Rio Grande"
    },
    {
        produto: "Teclado",
        categoria: "Informática",
        quantidade: 5,
        preco: 150,
        cidade: "Pelotas"
    },
    {
        produto: "Monitor",
        categoria: "Informática",
        quantidade: 3,
        preco: 1200,
        cidade: "Pelotas"
    },
    {
        produto: "Cadeira",
        categoria: "Móveis",
        quantidade: 4,
        preco: 900,
        cidade: "Rio Grande"
    },
    {
        produto: "Mesa",
        categoria: "Móveis",
        quantidade: 2,
        preco: 1500,
        cidade: "Pelotas"
    }
])
```

---

# 3. Estrutura básica

A estrutura de uma agregação é:

```javascript
db.vendas.aggregate([
    {
        $match: {
            cidade: "Rio Grande"
        }
    }
])
```

O `aggregate()` recebe um **array de etapas**.

Cada etapa começa com um operador:

```javascript
$match
$group
$sort
$project
$limit
$skip
$unwind
$lookup
...
```

---

# 4. `$match`

O `$match` funciona de maneira semelhante ao `WHERE` do SQL.

### SQL

```sql
SELECT *
FROM vendas
WHERE cidade = 'Rio Grande';
```

### MongoDB

```javascript
db.vendas.aggregate([
    {
        $match: {
            cidade: "Rio Grande"
        }
    }
])
```

Resultado:

```text
Notebook
Mouse
Cadeira
```

---

# 5. `$match` com valores numéricos

Podemos procurar vendas com quantidade maior que 3.

```javascript
db.vendas.aggregate([
    {
        $match: {
            quantidade: {
                $gt: 3
            }
        }
    }
])
```

`$gt` significa:

```text
greater than
maior que
```

Outros operadores:

```text
$gt   maior que
$gte  maior ou igual
$lt   menor que
$lte  menor ou igual
$eq   igual
$ne   diferente
$in   pertence a uma lista
$nin  não pertence a uma lista
```

Exemplo:

```javascript
db.vendas.aggregate([
    {
        $match: {
            preco: {
                $gte: 1000
            }
        }
    }
])
```

---

# 6. `$group`

Agora começa uma das partes mais importantes.

O `$group` permite **agrupar documentos**.

É semelhante ao:

```sql
GROUP BY
```

Por exemplo:

> Quanto foi vendido em cada cidade?

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",
            totalVendas: {
                $sum: 1
            }
        }
    }
])
```

Resultado aproximado:

```javascript
[
    {
        "_id": "Rio Grande",
        "totalVendas": 3
    },
    {
        "_id": "Pelotas",
        "totalVendas": 3
    }
]
```

---

# 7. Entendendo o `_id` do `$group`

Esta parte é fundamental:

```javascript
_id: "$cidade"
```

significa:

> agrupe os documentos pelo campo `cidade`.

Por exemplo:

```text
Rio Grande
Rio Grande
Rio Grande
Pelotas
Pelotas
Pelotas
```

vira:

```text
Rio Grande → grupo 1
Pelotas    → grupo 2
```

No `$group`, o `_id` representa **a chave do agrupamento**.

---

# 8. `$sum`

Podemos somar valores.

Por exemplo:

> Quantas unidades foram vendidas em cada cidade?

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

Resultado:

```text
Rio Grande → 16
Pelotas    → 10
```

Porque:

```text
Rio Grande:
2 + 10 + 4 = 16

Pelotas:
5 + 3 + 2 = 10
```

---

# 9. Calculando faturamento

Podemos combinar operadores.

Queremos:

```text
quantidade × preço
```

Podemos criar esse cálculo utilizando `$multiply`.

```javascript
db.vendas.aggregate([
    {
        $project: {
            produto: 1,
            quantidade: 1,
            preco: 1,
            faturamento: {
                $multiply: [
                    "$quantidade",
                    "$preco"
                ]
            }
        }
    }
])
```

Resultado:

```text
Notebook → 7000
Mouse    → 800
Teclado  → 750
Monitor  → 3600
Cadeira  → 3600
Mesa     → 3000
```

---

# 10. `$project`

O `$project` serve para **selecionar e transformar campos**.

Exemplo:

```javascript
db.vendas.aggregate([
    {
        $project: {
            produto: 1,
            cidade: 1
        }
    }
])
```

Resultado:

```text
Notebook    Rio Grande
Mouse       Rio Grande
Teclado     Pelotas
...
```

O:

```javascript
produto: 1
```

significa:

> mantenha o campo produto.

---

# 11. Criando campos com `$project`

O `$project` não serve apenas para selecionar.

Podemos criar novos campos.

```javascript
db.vendas.aggregate([
    {
        $project: {
            produto: 1,
            faturamento: {
                $multiply: [
                    "$quantidade",
                    "$preco"
                ]
            }
        }
    }
])
```

---

# 12. `$group` + `$sum`

Agora vamos responder:

> Qual o faturamento total?

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: null,
            faturamentoTotal: {
                $sum: {
                    $multiply: [
                        "$quantidade",
                        "$preco"
                    ]
                }
            }
        }
    }
])
```

O resultado será algo como:

```javascript
{
    "_id": null,
    "faturamentoTotal": 18750
}
```

Quando usamos:

```javascript
_id: null
```

não estamos criando grupos diferentes.

Estamos dizendo:

> coloque todos os documentos no mesmo grupo.

---

# 13. `$avg`

Podemos calcular médias.

Por exemplo:

> Qual o preço médio dos produtos?

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: null,
            precoMedio: {
                $avg: "$preco"
            }
        }
    }
])
```

---

# 14. Média por cidade

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",
            precoMedio: {
                $avg: "$preco"
            }
        }
    }
])
```

Resultado conceitual:

```text
Rio Grande → preço médio
Pelotas    → preço médio
```

---

# 15. `$max`

Maior valor.

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: null,
            maiorPreco: {
                $max: "$preco"
            }
        }
    }
])
```

---

# 16. `$min`

Menor valor.

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: null,
            menorPreco: {
                $min: "$preco"
            }
        }
    }
])
```

---

# 17. Vários operadores juntos

Podemos fazer:

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

Resultado:

```text
Rio Grande
    quantidadeTotal
    precoMedio
    maiorPreco
    menorPreco

Pelotas
    quantidadeTotal
    precoMedio
    maiorPreco
    menorPreco
```

---

# 18. `$sort`

Ordenação.

```javascript
db.vendas.aggregate([
    {
        $sort: {
            preco: 1
        }
    }
])
```

`1` significa crescente.

```javascript
-1
```

significa decrescente.

Então:

```javascript
db.vendas.aggregate([
    {
        $sort: {
            preco: -1
        }
    }
])
```

mostra os produtos do mais caro para o mais barato.

---

# 19. `$limit`

Limitar resultados.

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

Isso significa:

> Mostre os 3 produtos mais caros.

A ordem das etapas importa:

```text
$sort
   ↓
$limit
```

é diferente de:

```text
$limit
   ↓
$sort
```

---

# 20. Pipeline completo

Agora podemos fazer algo mais interessante:

> Quais são as cidades que possuem maior faturamento?

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",

            faturamento: {
                $sum: {
                    $multiply: [
                        "$quantidade",
                        "$preco"
                    ]
                }
            }
        }
    },

    {
        $sort: {
            faturamento: -1
        }
    }
])
```

Pipeline:

```text
DOCUMENTOS
     ↓
$group
     ↓
calcula faturamento
     ↓
$sort
     ↓
ordena pelo faturamento
```

---

# 21. `$match` + `$group`

Agora:

> Qual o faturamento dos produtos de Informática?

```javascript
db.vendas.aggregate([
    {
        $match: {
            categoria: "Informática"
        }
    },

    {
        $group: {
            _id: null,
            faturamento: {
                $sum: {
                    $multiply: [
                        "$quantidade",
                        "$preco"
                    ]
                }
            }
        }
    }
])
```

Aqui temos:

```text
$match
   ↓
filtra Informática
   ↓
$group
   ↓
calcula faturamento
```

---

# 22. `$match` + `$group` + `$sort`

Um exemplo bastante comum:

> Liste as cidades com pelo menos 10 unidades vendidas, da maior para a menor quantidade.

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",

            quantidade: {
                $sum: "$quantidade"
            }
        }
    },

    {
        $match: {
            quantidade: {
                $gte: 10
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

Observe algo importante:

O `$match` pode aparecer **depois do `$group`**.

Antes do `$group`, ele filtra documentos.

Depois do `$group`, ele filtra os grupos resultantes.

---

# 23. `$count`

Podemos contar documentos.

```javascript
db.vendas.aggregate([
    {
        $count: "total"
    }
])
```

Resultado:

```javascript
{
    "total": 6
}
```

---

# 24. `$count` por categoria

Para contar vendas por categoria:

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$categoria",
            quantidade: {
                $sum: 1
            }
        }
    }
])
```

Resultado:

```text
Informática → 4
Móveis      → 2
```

---

# 25. `$addFields`

Podemos adicionar um campo sem eliminar os demais.

```javascript
db.vendas.aggregate([
    {
        $addFields: {
            faturamento: {
                $multiply: [
                    "$quantidade",
                    "$preco"
                ]
            }
        }
    }
])
```

Agora cada documento terá:

```javascript
{
    produto: "Notebook",
    quantidade: 2,
    preco: 3500,
    cidade: "Rio Grande",
    faturamento: 7000
}
```

---

# 26. `$unwind`

Agora vamos imaginar uma coleção de alunos:

```javascript
{
    nome: "João",
    cursos: [
        "MongoDB",
        "Java",
        "PostgreSQL"
    ]
}
```

O `$unwind` transforma o array em documentos separados.

```javascript
db.alunos.aggregate([
    {
        $unwind: "$cursos"
    }
])
```

Antes:

```text
João
    MongoDB
    Java
    PostgreSQL
```

Depois:

```text
João → MongoDB
João → Java
João → PostgreSQL
```

Isso é muito útil para trabalhar com arrays.

---

# 27. Exemplo com notas

Imagine:

```javascript
{
    nome: "João",
    notas: [8, 7, 10]
}
```

Podemos calcular a média:

```javascript
db.alunos.aggregate([
    {
        $unwind: "$notas"
    },

    {
        $group: {
            _id: "$nome",

            media: {
                $avg: "$notas"
            }
        }
    }
])
```

Resultado:

```text
João → 8.33
```

---

# 28. `$lookup`

Um dos operadores mais importantes.

O `$lookup` permite combinar informações de duas coleções.

É parecido com:

```sql
JOIN
```

Imagine:

### clientes

```javascript
{
    _id: 1,
    nome: "João"
}
```

### pedidos

```javascript
{
    cliente_id: 1,
    produto: "Notebook"
}
```

Podemos fazer:

```javascript
db.pedidos.aggregate([
    {
        $lookup: {
            from: "clientes",
            localField: "cliente_id",
            foreignField: "_id",
            as: "cliente"
        }
    }
])
```

Resultado:

```javascript
{
    cliente_id: 1,
    produto: "Notebook",

    cliente: [
        {
            _id: 1,
            nome: "João"
        }
    ]
}
```

---

# 29. `$lookup` + `$unwind`

Como o `$lookup` cria um array, podemos utilizar `$unwind`.

```javascript
db.pedidos.aggregate([
    {
        $lookup: {
            from: "clientes",
            localField: "cliente_id",
            foreignField: "_id",
            as: "cliente"
        }
    },

    {
        $unwind: "$cliente"
    }
])
```

Agora podemos acessar:

```javascript
"$cliente.nome"
```

---

# 30. Exemplo mais realista

Imagine:

### clientes

```javascript
[
    {
        _id: 1,
        nome: "João",
        cidade: "Rio Grande"
    },
    {
        _id: 2,
        nome: "Maria",
        cidade: "Pelotas"
    }
]
```

### pedidos

```javascript
[
    {
        cliente_id: 1,
        valor: 500
    },
    {
        cliente_id: 1,
        valor: 800
    },
    {
        cliente_id: 2,
        valor: 1000
    }
]
```

Queremos:

> Quanto cada cliente gastou?

```javascript
db.pedidos.aggregate([
    {
        $group: {
            _id: "$cliente_id",

            total: {
                $sum: "$valor"
            }
        }
    },

    {
        $lookup: {
            from: "clientes",
            localField: "_id",
            foreignField: "_id",
            as: "cliente"
        }
    },

    {
        $unwind: "$cliente"
    },

    {
        $project: {
            _id: 0,
            cliente: "$cliente.nome",
            total: 1
        }
    }
])
```

Resultado:

```text
João  → 1300
Maria → 1000
```

---

# 31. `$facet`

O `$facet` permite executar vários pipelines diferentes sobre os mesmos documentos.

Por exemplo:

```javascript
db.vendas.aggregate([
    {
        $facet: {

            produtosCaros: [
                {
                    $sort: {
                        preco: -1
                    }
                },
                {
                    $limit: 3
                }
            ],

            cidades: [
                {
                    $group: {
                        _id: "$cidade"
                    }
                }
            ],

            totalProdutos: [
                {
                    $count: "total"
                }
            ]
        }
    }
])
```

Isso pode produzir algo semelhante a:

```javascript
{
    produtosCaros: [...],

    cidades: [...],

    totalProdutos: [
        {
            total: 6
        }
    ]
}
```

É muito interessante para construir **dashboards e relatórios**.

---

# 32. `$bucket`

Podemos dividir valores em faixas.

Por exemplo, queremos classificar produtos por preço:

```text
0–500
500–1000
1000–2000
2000+
```

Podemos usar:

```javascript
db.vendas.aggregate([
    {
        $bucket: {
            groupBy: "$preco",

            boundaries: [
                0,
                500,
                1000,
                2000,
                10000
            ],

            default: "Outros",

            output: {
                quantidade: {
                    $sum: 1
                }
            }
        }
    }
])
```

Isso é útil para:

* relatórios;
* estatísticas;
* análise de preços;
* distribuição de valores.

---

# 33. Comparação com SQL

Uma forma interessante de ensinar MongoDB é mostrar a equivalência.

### SQL

```sql
SELECT cidade, SUM(quantidade)
FROM vendas
GROUP BY cidade;
```

### MongoDB

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",
            quantidade: {
                $sum: "$quantidade"
            }
        }
    }
])
```

---

### SQL

```sql
SELECT *
FROM vendas
WHERE cidade = 'Rio Grande';
```

### MongoDB

```javascript
db.vendas.aggregate([
    {
        $match: {
            cidade: "Rio Grande"
        }
    }
])
```

---

### SQL

```sql
SELECT *
FROM vendas
ORDER BY preco DESC
LIMIT 3;
```

### MongoDB

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

---

### SQL

```sql
SELECT cidade, AVG(preco)
FROM vendas
GROUP BY cidade;
```

### MongoDB

```javascript
db.vendas.aggregate([
    {
        $group: {
            _id: "$cidade",
            media: {
                $avg: "$preco"
            }
        }
    }
])
```

---

# 34. Um exemplo completo

Agora podemos montar um relatório:

> Para cada cidade, mostrar:
>
> * quantidade de produtos vendidos;
> * faturamento;
> * preço médio;
> * maior venda;
> * ordenar pelo faturamento.

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

Esse é um bom exemplo para mostrar aos alunos porque reúne:

```text
$group
   ↓
$sum
   ↓
$multiply
   ↓
$avg
   ↓
$max
   ↓
$sort
   ↓
$project
```

---

# 35. Regra mental para o aluno

Uma maneira simples de memorizar Aggregation:

```text
$match
    ↓
"Quais documentos eu quero?"

$group
    ↓
"Como quero agrupá-los?"

$sum / $avg / $min / $max
    ↓
"O que quero calcular?"

$sort
    ↓
"Como quero ordenar?"

$limit
    ↓
"Quantos quero?"

$project
    ↓
"Quais campos quero mostrar?"
```

E:

```text
$unwind
    ↓
"Quero transformar elementos de um array em documentos."

$lookup
    ↓
"Quero buscar informações em outra coleção."
```

---

# 36. Exercícios para os alunos

### Exercício 1

Utilizando a coleção `vendas`, mostre somente os produtos da cidade de `Pelotas`.

---

### Exercício 2

Mostre somente os produtos com preço superior a `1000`.

---

### Exercício 3

Calcule o preço médio dos produtos.

---

### Exercício 4

Calcule o preço médio por cidade.

---

### Exercício 5

Calcule a quantidade total de produtos vendidos por cidade.

---

### Exercício 6

Calcule o faturamento de cada produto.

---

### Exercício 7

Calcule o faturamento total.

---

### Exercício 8

Mostre as cidades ordenadas pelo faturamento, da maior para a menor.

---

### Exercício 9

Mostre os três produtos mais caros.

---

### Exercício 10

Mostre somente as cidades cujo faturamento seja superior a `5000`.

---

### Exercício 11

Crie uma agregação que apresente:

```text
cidade
quantidade vendida
faturamento
preço médio
maior preço
menor preço
```

---

### Exercício 12 — desafio

Crie duas coleções:

```text
clientes
pedidos
```

Utilize:

```text
$lookup
$unwind
$group
$sort
$project
```

para gerar um relatório:

```text
Cliente | Quantidade de pedidos | Total gasto
```

---

## Resumo final

Os operadores mais importantes para começar são:

| Operador     | Função                        |
| ------------ | ----------------------------- |
| `$match`     | Filtrar                       |
| `$group`     | Agrupar                       |
| `$sum`       | Somar                         |
| `$avg`       | Média                         |
| `$min`       | Menor valor                   |
| `$max`       | Maior valor                   |
| `$sort`      | Ordenar                       |
| `$limit`     | Limitar                       |
| `$project`   | Selecionar/transformar campos |
| `$addFields` | Adicionar campos              |
| `$unwind`    | Desmontar arrays              |
| `$lookup`    | Relacionar coleções           |
| `$count`     | Contar                        |
| `$facet`     | Executar vários pipelines     |
| `$bucket`    | Criar faixas                  |

**A ideia central da Aggregation é:** o MongoDB recebe documentos, passa esses documentos por uma sequência de transformações e, ao final, produz um novo conjunto de resultados. Cada etapa do pipeline faz uma operação específica, e as etapas podem ser combinadas para produzir consultas bastante sofisticadas.
