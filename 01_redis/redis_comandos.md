# Guia de Comandos Essenciais do Redis

O Redis é um banco de dados chave-valor. A maioria dos comandos segue a estrutura: `COMANDO chave [argumentos]`.

---

## 1. Comandos Gerais (Gerenciamento de Chaves)

Aplicam-se a qualquer tipo de dado armazenado.

*   `KEYS *` - Lista todas as chaves cadastradas (evite usar em produção).
*   `SCAN 0` - Alternativa segura ao `KEYS` para listar chaves de forma paginada.
*   `EXISTS chave` - Verifica se uma chave existe (retorna 1 se sim, 0 se não).
*   `DEL chave` - Deleta uma ou mais chaves.
*   `EXPIRE chave 60` - Define um tempo de vida (TTL) para a chave em segundos (60s neste exemplo).
*   `TTL chave` - Retorna o tempo restante de vida da chave (-1 significa que não expira, -2 significa que não existe).
*   `FLUSHDB` - Apaga todos os dados do banco de dados atual.

---

## 2. Strings

O tipo de dado mais simples. Pode armazenar texto, inteiros ou dados binários (como imagens codificadas) de até 512MB.

*   `SET chave "valor"` - Salva ou atualiza uma chave com o valor informado.
*   `SETEX chave 60 "valor"` - Cria a chave com valor e define o TTL de 60 segundos em uma única operação.
*   `GET chave` - Recuperar o valor da chave.
*   `MSET c1 "v1" c2 "v2"` - Cria múltiplas chaves e valores simultaneamente.
*   `MGET c1 c2` - Recupera os valores de múltiplas chaves.
*   `INCR chave` - Incrementa em 1 um valor numérico.
*   `DECRBY chave 5` - Decrementa o valor numérico pela quantidade informada (5 neste exemplo).

---

## 3. Hashes

Ideal para representar objetos com campos e valores (como um perfil de usuário).

*   `HSET usuario:1 nome "Ana" idade 30` - Cria ou atualiza campos dentro do hash.
*   `HGET usuario:1 nome` - Retorna o valor de um campo específico.
*   `HGETALL usuario:1` - Retorna todos os campos e valores do hash.
*   `HDEL usuario:1 idade` - Deleta um campo de dentro do hash.
*   `HEXISTS usuario:1 email` - Verifica se um campo existe no hash.

---

## 4. Lists (Listas)

Coleções de strings ordenadas pela ordem de inserção. Permitem duplicatas. Excelente para criar filas (FIFO) ou pilhas (LIFO).

*   `LPUSH minha_lista "item1"` - Insere o item no início (esquerda) da lista.
*   `RPUSH minha_lista "item2"` - Insere o item no final (direita) da lista.
*   `LPOP minha_lista` - Remove e retorna o primeiro item (esquerda).
*   `RPOP minha_lista` - Remove e retorna o último item (direita).
*   `LRANGE minha_lista 0 -1` - Retorna todos os itens da lista (0 é o primeiro, -1 é o último).
*   `LLEN minha_lista` - Retorna o tamanho atual da lista.

---

## 5. Sets (Conjuntos)

Coleções de strings não ordenadas e que **não permitem elementos duplicados**.

*   `SADD meu_set "valor1" "valor2"` - Adiciona membros ao conjunto.
*   `SMEMBERS meu_set` - Retorna todos os membros do conjunto.
*   `SISMEMBER meu_set "valor1"` - Verifica se o elemento pertence ao conjunto.
*   `SREM meu_set "valor1"` - Remove um membro do conjunto.
*   `SINTER setA setB` - Retorna a interseção (elementos comuns) entre dois conjuntos.

---

## 6. Sorted Sets (ZSets)

Semelhante aos Sets, mas cada elemento possui um **score** (pontuação numérica) associado. Os dados são mantidos ordenados automaticamente pelo score. Ideal para rankings/líderes.

*   `ZADD ranking 100 "jogador1" 150 "jogador2"` - Adiciona membros com suas respectivas pontuações.
*   `ZRANGE ranking 0 -1 WITHSCORES` - Retorna os membros ordenados do menor para o maior score.
*   `ZREVRANGE ranking 0 -1 WITHSCORES` - Retorna os membros ordenados do maior para o menor score (formato de pódio).
*   `ZINCRBY ranking 50 "jogador1"` - Incrementa o score do membro em 50 pontos.
*   `ZREM ranking "jogador1"` - Remove o membro do Sorted Set.
