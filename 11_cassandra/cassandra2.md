
# **Aula: Introdução ao Apache Cassandra**

Imagine que temos um sistema que recebe milhões de informações por segundo. Pode ser uma rede social, um sistema de monitoramento de sensores ou uma plataforma de streaming.

Se utilizássemos um banco de dados relacional tradicional, como PostgreSQL, poderíamos enfrentar dificuldades para distribuir toda essa carga entre vários servidores.

É justamente nesse cenário que entra o Apache Cassandra.

O Cassandra é um banco de dados NoSQL distribuído. Isso significa que os dados não ficam necessariamente armazenados em um único servidor. Eles são distribuídos entre várias máquinas, chamadas de nós.

Uma característica muito importante é que não existe um servidor principal, ou seja, um servidor master. Todos os nós possuem responsabilidades equivalentes.

Essa arquitetura é chamada de Peer-to-Peer.

## **1. Arquitetura distribuída**

Imagine um cluster formado por três servidores:

- Nó 1;
- Nó 2;
- Nó 3.

Os dados são distribuídos entre esses servidores e podem ser replicados.

Por exemplo, quando uma informação é armazenada, o Cassandra pode manter cópias dessa informação em dois ou três nós diferentes.

Assim, se um servidor apresentar problemas, os outros podem continuar atendendo às requisições.

Essa característica é chamada de tolerância a falhas.

## **2. Escalabilidade horizontal**

Aqui temos uma diferença importante em relação à arquitetura tradicional de bancos de dados.

Quando precisamos aumentar a capacidade de um banco de dados, podemos melhorar o hardware do servidor, colocando mais memória, processadores e armazenamento.

Isso é chamado de escalabilidade vertical.

No Cassandra, podemos simplesmente adicionar novos servidores ao cluster.

Isso é chamado de escalabilidade horizontal.

Quanto mais nós adicionamos, maior pode ser a capacidade de armazenamento e processamento do ambiente.

## **3. Organização dos dados**

Agora vamos entender como os dados são organizados.

No Cassandra, temos três conceitos importantes:

**Keyspace:** é semelhante a um banco de dados no PostgreSQL. É onde definimos configurações como a estratégia de replicação.

**Tabela:** organiza os dados em linhas e colunas, de maneira semelhante às tabelas relacionais.

**Partição:** representa o conjunto de dados identificado por uma chave de partição. Essa chave determina como os dados serão distribuídos entre os nós.

Um detalhe importante: no Cassandra, a escolha da chave primária é fundamental para o desempenho das consultas e para a distribuição dos dados.

## **4. Conhecendo a CQL**

O Cassandra utiliza uma linguagem chamada Cassandra Query Language, ou simplesmente CQL.

Sua sintaxe lembra bastante o SQL.

Por exemplo, podemos criar um keyspace:

```
CREATE KEYSPACE loja
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 3
};
```

Aqui estamos criando um keyspace chamado `loja`.

O fator de replicação igual a três significa que o Cassandra deverá manter três cópias de cada partição, conforme a estratégia de replicação e a disponibilidade do cluster.

Agora podemos selecionar esse keyspace:

```
USE loja;
```

E criar uma tabela:

```
CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome TEXT,
    email TEXT,
    cidade TEXT
);
```

Observe que utilizamos UUID como identificador do cliente.

Podemos inserir registros:

```
INSERT INTO cliente (
    id, nome, email, cidade
)
VALUES (
    uuid(),
    'João',
    'joao@email.com',
    'Rio Grande'
);
```

E consultar os dados:

```
SELECT * FROM cliente;
```

Até aqui, a experiência é bastante parecida com PostgreSQL.

Mas existe uma diferença muito importante: o Cassandra não foi projetado para realizar consultas relacionais complexas.

Por exemplo, não podemos simplesmente utilizar JOINs entre tabelas como fazemos no PostgreSQL.

Por isso, a modelagem dos dados é diferente.

## **5. Consistência ajustável**

Outro conceito fundamental é a consistência ajustável.

Imagine que temos três réplicas de uma determinada informação.

Podemos configurar quantas réplicas precisam responder para que uma operação de leitura ou escrita seja considerada bem-sucedida.

Por exemplo:

```
CONSISTENCY QUORUM;
```

Nesse caso, com fator de replicação três, precisamos de respostas de pelo menos duas réplicas.

Isso permite equilibrar disponibilidade, consistência e latência de acordo com as necessidades da aplicação.

## **6. Quando utilizar Cassandra?**

O Cassandra é especialmente interessante para:

- Sistemas de Internet das Coisas (IoT).
- Monitoramento de aplicações.
- Armazenamento de logs.
- Redes sociais.
- Aplicações distribuídas globalmente.
- Sistemas que recebem grandes volumes de escrita.

Por outro lado, não é a melhor escolha quando precisamos de muitas operações relacionais, JOINs complexos ou transações tradicionais envolvendo várias tabelas.

**Para guardar a ideia principal da aula:**

O PostgreSQL é muito forte em consultas relacionais, integridade referencial e transações. Já o Cassandra é especialmente projetado para distribuir grandes volumes de dados entre vários servidores, mantendo alta disponibilidade e escalabilidade.

Essa é a principal diferença de filosofia entre os dois bancos de dados.