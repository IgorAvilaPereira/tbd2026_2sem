# Mongo Aggregation

## 1️⃣ Listar álbuns com maior duração total

Agrupa músicas por álbum e calcula a **duração total e média das músicas**.

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$album",
      totalDuracao: { $sum: "$duracao" },
      mediaDuracao: { $avg: "$duracao" },
      totalMusicas: { $sum: 1 }
    }
  },
  { $sort: { totalDuracao: -1 } },
  { $limit: 10 }
])
```

💡 Útil para descobrir **quais álbuns têm mais conteúdo**.

---

## 2️⃣ Distribuição de músicas por faixa de duração

Cria “faixas” de duração usando `$bucket`:

```javascript
db.musicas.aggregate([
  {
    $bucket: {
      groupBy: "$duracao",
      boundaries: [0, 120, 180, 240, 300, 600],
      default: "600+",
      output: {
        totalMusicas: { $sum: 1 },
        mediaDuracao: { $avg: "$duracao" }
      }
    }
  }
])
```

Resultado esperado:

```json
[
  { "_id": 0, "totalMusicas": 5, "mediaDuracao": 100 },
  { "_id": 120, "totalMusicas": 12, "mediaDuracao": 150 },
  ...
]
```

Isso é ótimo para **análises de duração de músicas populares**.

---

## 3️⃣ Procurar músicas com palavras específicas no título

```javascript
db.musicas.aggregate([
  { $match: { titulo: { $regex: "amor", $options: "i" } } },
  { $project: { titulo: 1, artista: 1, duracao: 1 } }
])
```

💡 Aqui usamos `$regex` para achar títulos que contenham “amor” (case-insensitive).

---

## 4️⃣ Média de duração de músicas por artista

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$artista",
      mediaDuracao: { $avg: "$duracao" },
      totalMusicas: { $sum: 1 }
    }
  },
  { $sort: { mediaDuracao: -1 } },
  { $limit: 5 }
])
```

✅ Útil para descobrir **artistas cujas músicas tendem a ser mais longas**.

---

## 5️⃣ Combinar `$match` e `$group` — Rock com mais de 4 minutos

```javascript
db.musicas.aggregate([
  { $match: { genero: "Rock", duracao: { $gt: 240 } } },
  { $group: {
      _id: "$artista",
      totalMusicas: { $sum: 1 },
      duracaoMedia: { $avg: "$duracao" }
  }},
  { $sort: { totalMusicas: -1 } }
])
```

Isso filtra músicas longas de um gênero específico e depois agrega por artista.

---

## 6️⃣ Exemplo de `$facet` — múltiplas estatísticas ao mesmo tempo

```javascript
db.musicas.aggregate([
  {
    $facet: {
      topGeneros: [
        { $group: { _id: "$genero", total: { $sum: 1 } } },
        { $sort: { total: -1 } },
        { $limit: 3 }
      ],
      duracaoEstatisticas: [
        { $group: {
            _id: null,
            duracaoMin: { $min: "$duracao" },
            duracaoMax: { $max: "$duracao" },
            duracaoMedia: { $avg: "$duracao" }
        }}
      ]
    }
  }
])
```

💡 Com `$facet` você consegue **retornar várias métricas diferentes em um mesmo pipeline**.

---

## 🎧 1. Contar músicas por gênero

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$genero",
      totalMusicas: { $sum: 1 },
      duracaoTotal: { $sum: "$duracao" },
      duracaoMedia: { $avg: "$duracao" }
    }
  },
  { $sort: { totalMusicas: -1 } }
])
```

📤 **Resultado esperado:**

```json
[
  { "_id": "Pop", "totalMusicas": 42, "duracaoTotal": 9800, "duracaoMedia": 233.33 },
  { "_id": "Rock", "totalMusicas": 35, "duracaoTotal": 8700, "duracaoMedia": 248.57 }
]
```

---

## 📅 2. Número de músicas por ano de lançamento

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$ano",
      totalMusicas: { $sum: 1 }
    }
  },
  { $sort: { _id: 1 } }
])
```

📤 **Resultado:**

```json
[
  { "_id": 1971, "totalMusicas": 3 },
  { "_id": 1990, "totalMusicas": 12 },
  { "_id": 2020, "totalMusicas": 25 }
]
```

---

## 🧑‍🎤 3. Top 5 artistas com mais músicas cadastradas

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$artista",
      totalMusicas: { $sum: 1 },
      duracaoTotal: { $sum: "$duracao" }
    }
  },
  { $sort: { totalMusicas: -1 } },
  { $limit: 5 }
])
```

📤 **Resultado:**

```json
[
  { "_id": "Anitta", "totalMusicas": 45, "duracaoTotal": 10200 },
  { "_id": "Caetano Veloso", "totalMusicas": 33, "duracaoTotal": 7800 },
  ...
]
```

---

## 🪄 4. Músicas com mais de 5 minutos (300s) e agrupadas por gênero

```javascript
db.musicas.aggregate([
  { $match: { duracao: { $gt: 300 } } },
  {
    $group: {
      _id: "$genero",
      musicasLongas: { $sum: 1 },
      mediaDuracao: { $avg: "$duracao" }
    }
  },
  { $sort: { musicasLongas: -1 } }
])
```

---


## 5. Contar músicas por artista e gênero

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: { artista: "$artista", genero: "$genero" },
      totalMusicas: { $sum: 1 },
      duracaoTotal: { $sum: "$duracao" }
    }
  },
  { $sort: { "totalMusicas": -1 } }
])
```

💡 Isso mostra **quantas músicas cada artista tem por gênero** e a duração total dessas músicas.

---

## 6. Top 3 músicas mais longas por artista

```javascript
db.musicas.aggregate([
  { $sort: { duracao: -1 } },
  {
    $group: {
      _id: "$artista",
      topMusicas: { $push: { titulo: "$titulo", duracao: "$duracao" } }
    }
  },
  {
    $project: {
      topMusicas: { $slice: ["$topMusicas", 3] }
    }
  }
])
```

✅ Mostra as **3 músicas mais longas de cada artista**.

---

## 7. Músicas lançadas por década

```javascript
db.musicas.aggregate([
  {
    $project: {
      titulo: 1,
      artista: 1,
      ano: 1,
      decada: { $multiply: [ { $floor: { $divide: ["$ano", 10] } }, 10 ] }
    }
  },
  {
    $group: {
      _id: "$decada",
      totalMusicas: { $sum: 1 }
    }
  },
  { $sort: { "_id": 1 } }
])
```

💡 Permite **analisar tendências musicais por década**.

---

## 8. Artistas com mais de 10 músicas cadastradas

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$artista",
      totalMusicas: { $sum: 1 }
    }
  },
  { $match: { totalMusicas: { $gt: 10 } } },
  { $sort: { totalMusicas: -1 } }
])
```

✅ Útil para identificar **artistas mais produtivos no banco de dados**.

---

## 9. Álbuns com música mais longa

```javascript
db.musicas.aggregate([
  {
    $group: {
      _id: "$album",
      musicaMaisLonga: { $max: "$duracao" },
      totalMusicas: { $sum: 1 }
    }
  },
  { $sort: { musicaMaisLonga: -1 } }
])
```

💡 Mostra quais **álbuns têm a música mais longa**.

---

## 10. Estatísticas completas usando `$facet`

```javascript
db.musicas.aggregate([
  {
    $facet: {
      duracaoStats: [
        { $group: {
            _id: null,
            duracaoMin: { $min: "$duracao" },
            duracaoMax: { $max: "$duracao" },
            duracaoMedia: { $avg: "$duracao" }
        }}
      ],
      topGeneros: [
        { $group: { _id: "$genero", totalMusicas: { $sum: 1 } } },
        { $sort: { totalMusicas: -1 } },
        { $limit: 5 }
      ],
      topArtistas: [
        { $group: { _id: "$artista", totalMusicas: { $sum: 1 } } },
        { $sort: { totalMusicas: -1 } },
        { $limit: 5 }
      ]
    }
  }
])
```

✅ Retorna **estatísticas de duração, top gêneros e top artistas em um único pipeline**.

---

## 11. Encontrar artistas com músicas acima de 5 minutos (300s) e agrupar por gênero

```javascript
db.musicas.aggregate([
  { $match: { duracao: { $gt: 300 } } },
  {
    $group: {
      _id: "$genero",
      totalMusicasLongas: { $sum: 1 },
      mediaDuracaoLongas: { $avg: "$duracao" }
    }
  },
  { $sort: { totalMusicasLongas: -1 } }
])
```

💡 Útil para playlists de músicas longas ou análise de gêneros com músicas extensas.

---

## lookup

### 🎼 Coleções de exemplo:

#### `musicas`

```json
[
  { "_id": 1, "titulo": "Imagine", "artista_id": 101, "ano": 1971 },
  { "_id": 2, "titulo": "Hey Jude", "artista_id": 102, "ano": 1968 },
  { "_id": 3, "titulo": "Let It Be", "artista_id": 102, "ano": 1970 }
]
```

#### `artistas`

```json
[
  { "_id": 101, "nome": "John Lennon", "pais": "Reino Unido" },
  { "_id": 102, "nome": "The Beatles", "pais": "Reino Unido" }
]
```

---

## 🧩 1. **Exemplo básico de `$lookup`**

Juntando as músicas com os dados dos artistas:

```javascript
db.musicas.aggregate([
  {
    $lookup: {
      from: "artistas",          // coleção para unir
      localField: "artista_id",  // campo na coleção "musicas"
      foreignField: "_id",       // campo correspondente na coleção "artistas"
      as: "artista"              // nome do campo resultante
    }
  }
])
```

🟡 **Resultado:**

```json
[
  {
    "_id": 1,
    "titulo": "Imagine",
    "artista_id": 101,
    "ano": 1971,
    "artista": [
      { "_id": 101, "nome": "John Lennon", "pais": "Reino Unido" }
    ]
  },
  {
    "_id": 2,
    "titulo": "Hey Jude",
    "artista_id": 102,
    "ano": 1968,
    "artista": [
      { "_id": 102, "nome": "The Beatles", "pais": "Reino Unido" }
    ]
  }
]
```

---

## 🧾 2. **Usando `$unwind` para simplificar**

Como `artista` vem como um **array**, você pode usar `$unwind`:

```javascript
db.musicas.aggregate([
  {
    $lookup: {
      from: "artistas",
      localField: "artista_id",
      foreignField: "_id",
      as: "artista"
    }
  },
  { $unwind: "$artista" }
])
```

🟢 **Resultado:**

```json
{
  "_id": 1,
  "titulo": "Imagine",
  "ano": 1971,
  "artista": {
    "_id": 101,
    "nome": "John Lennon",
    "pais": "Reino Unido"
  }
}
```

---

## 🔍 3. **Com filtros adicionais**

Por exemplo, buscar apenas músicas **depois de 1969** e juntar com o artista:

```javascript
db.musicas.aggregate([
  { $match: { ano: { $gt: 1969 } } },
  {
    $lookup: {
      from: "artistas",
      localField: "artista_id",
      foreignField: "_id",
      as: "artista"
    }
  },
  { $unwind: "$artista" }
])
```

---

## 🪄 4. **Usando `$lookup` com pipeline avançado**

Você também pode usar um **sub-pipeline** dentro do `$lookup` para filtrar artistas:

```javascript
db.musicas.aggregate([
  {
    $lookup: {
      from: "artistas",
      let: { id_artista: "$artista_id" },
      pipeline: [
        { $match: { $expr: { $eq: ["$_id", "$$id_artista"] } } },
        { $project: { nome: 1, pais: 1, _id: 0 } }
      ],
      as: "artista"
    }
  },
  { $unwind: "$artista" }
])
```

🔸 Isso é útil quando você quer controlar **exatamente quais campos retornar** da coleção relacionada.

---

## 🌍 5. **Exemplo com múltiplos lookups**

Se você tiver também uma coleção `generos`:

```json
[
  { "_id": 1, "nome": "Rock" },
  { "_id": 2, "nome": "Pop" }
]
```

E sua música tiver `genero_id`, você pode juntar **artista + gênero**:

```javascript
db.musicas.aggregate([
  {
    $lookup: {
      from: "artistas",
      localField: "artista_id",
      foreignField: "_id",
      as: "artista"
    }
  },
  { $unwind: "$artista" },
  {
    $lookup: {
      from: "generos",
      localField: "genero_id",
      foreignField: "_id",
      as: "genero"
    }
  },
  { $unwind: "$genero" }
])
```

---

## 💡 Exemplo de código Java usando `aggregate`

Se você estiver usando Java com o **driver oficial do MongoDB**, pode montar assim:

```java
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import java.util.Arrays;

AggregateIterable<Document> result = colecao.aggregate(Arrays.asList(
    new Document("$group", new Document("_id", "$genero")
        .append("totalMusicas", new Document("$sum", 1))
        .append("duracaoMedia", new Document("$avg", "$duracao"))
    ),
    new Document("$sort", new Document("totalMusicas", -1))
));

for (Document doc : result) {
    System.out.println(doc.toJson());
}
```
