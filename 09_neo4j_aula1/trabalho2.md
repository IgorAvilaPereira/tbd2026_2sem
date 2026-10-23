# Trabalho – Rede Social Acadêmica com Java Vanilla, Neo4j e Cassandra (5 pontos)

## 1. Objetivo

Desenvolver uma aplicação de console utilizando Java Vanilla que implemente funcionalidades básicas de uma rede social acadêmica, integrando os bancos de dados Neo4j e Apache Cassandra por meio de seus respectivos drivers oficiais para Java.

O trabalho tem como objetivo exercitar o acesso a diferentes modelos de bancos de dados NoSQL: banco de dados orientado a grafos e banco de dados distribuído orientado a famílias de colunas.

## 2. Tema e escopo

A aplicação deverá implementar uma **rede social acadêmica**, na qual estudantes podem estabelecer amizades e publicar mensagens.

Os dados deverão ser distribuídos entre os dois bancos de dados conforme suas características:

- **Neo4j:** armazenar estudantes e seus relacionamentos de amizade.
- **Apache Cassandra:** armazenar as publicações dos estudantes, incluindo conteúdo, data e hora e identificação do autor.

A aplicação deverá permitir cadastrar estudantes, estabelecer amizades, publicar mensagens, consultar relacionamentos e visualizar publicações, combinando informações provenientes dos dois bancos de dados.

O sistema deverá ser implementado como uma aplicação de console, sem necessidade de interface gráfica ou aplicação web.

## 3. Requisitos funcionais

### 3.1. Cadastrar estudante e estabelecer amizade — 1,0 ponto

Implementar a funcionalidade **Cadastrar estudante e estabelecer amizade**, que deverá:

1. Solicitar o identificador, o nome e o curso de um estudante.
2. Cadastrar o estudante no Neo4j.
3. Solicitar o identificador de outro estudante já cadastrado.
4. Criar um relacionamento de amizade entre os dois estudantes.

O sistema deverá impedir o cadastro de identificadores duplicados e a criação de amizades entre um estudante e ele próprio.

Os estudantes deverão ser representados por nós com o rótulo `Estudante`, e as amizades deverão ser representadas por relacionamentos do tipo `AMIGO_DE`.

A amizade deverá ser considerada mútua, sem duplicação desnecessária de relacionamentos em sentidos opostos.

### 3.2. Publicar mensagem — 1,0 ponto

Implementar a funcionalidade **Publicar mensagem**, que deverá:

1. Solicitar o identificador do estudante autor.
2. Verificar se o estudante está cadastrado no Neo4j.
3. Solicitar o conteúdo textual da publicação.
4. Gerar um identificador único para a publicação.
5. Registrar automaticamente a data e a hora da publicação.
6. Armazenar a publicação no Cassandra.

Cada publicação deverá conter, no mínimo:

- Identificador da publicação.
- Identificador do estudante autor.
- Conteúdo textual.
- Data e hora da publicação.

Somente estudantes cadastrados no Neo4j poderão publicar mensagens. A publicação deverá ser armazenada efetivamente no Cassandra, utilizando o driver oficial para Java.

### 3.3. Consultar amigos — 1,0 ponto

Implementar a funcionalidade **Consultar amigos**, que deverá:

1. Solicitar o identificador de um estudante.
2. Consultar o Neo4j utilizando Cypher e o driver oficial para Java.
3. Listar os estudantes relacionados por amizade ao estudante informado, apresentando identificador, nome e curso.
4. Informar quando o estudante não possuir amigos cadastrados.

A consulta deverá considerar a natureza mútua da amizade, permitindo recuperar os amigos independentemente do sentido em que o relacionamento tenha sido armazenado.

### 3.4. Visualizar publicações dos amigos — 1,0 ponto

Implementar a funcionalidade **Visualizar publicações dos amigos**, que deverá:

1. Solicitar o identificador de um estudante.
2. Consultar o Neo4j para identificar seus amigos.
3. Consultar o Cassandra para recuperar as publicações desses amigos.
4. Apresentar as publicações em ordem cronológica decrescente, da mais recente para a mais antiga.
5. Exibir, para cada publicação, o nome do autor, o conteúdo e a data e hora.

Essa funcionalidade deverá integrar os resultados dos dois bancos de dados em uma única operação da aplicação Java.

A identificação dos amigos deverá ser obtida no Neo4j, enquanto os dados das publicações deverão ser recuperados do Cassandra. Não será permitido manter uma cópia das publicações em memória como substituição à persistência no Cassandra.

Caso não existam amigos ou publicações para apresentar, o sistema deverá informar adequadamente essa situação.

### 3.5. Consultar relatório de atividade da rede — 1,0 ponto

Implementar a funcionalidade **Relatório de atividade da rede**, que deverá:

1. Solicitar o identificador de um estudante.
2. Consultar o Neo4j para recuperar o nome do estudante e a quantidade de amigos.
3. Consultar o Cassandra para recuperar a quantidade de publicações do estudante.
4. Identificar a publicação mais recente do estudante, quando existir.
5. Apresentar um relatório consolidado contendo o nome do estudante, a quantidade de amigos, a quantidade de publicações e os dados da publicação mais recente.

Essa funcionalidade deverá utilizar os dois bancos de dados e demonstrar a integração dos resultados na aplicação Java.

O sistema deverá apresentar zero publicações quando o estudante ainda não tiver publicado mensagens e informar quando não houver publicação recente a exibir.

## 4. Menu da aplicação

A aplicação deverá disponibilizar um menu textual semelhante ao seguinte:

```
===== REDE SOCIAL ACADÊMICA =====

1. Cadastrar estudante e estabelecer amizade
2. Publicar mensagem
3. Consultar amigos
4. Visualizar publicações dos amigos
5. Consultar relatório de atividade da rede
0. Encerrar aplicação

Escolha uma opção:
```

Cada opção deverá executar a funcionalidade correspondente. Após a conclusão de uma operação, o menu deverá ser apresentado novamente, permitindo realizar outras operações até que o usuário escolha encerrar a aplicação.

## 5. Requisitos técnicos obrigatórios

A aplicação deverá atender aos seguintes requisitos:

- Utilizar Java Vanilla, sem frameworks de aplicação como Spring Boot ou Quarkus.
- Utilizar o driver oficial do Neo4j para Java.
- Utilizar o driver oficial do Apache Cassandra para Java.
- Utilizar Cypher para consultar e manipular dados no Neo4j.
- Utilizar CQL para consultar e manipular dados no Cassandra.
- Armazenar estudantes e amizades no Neo4j.
- Armazenar publicações no Cassandra.
- Utilizar os bancos de dados efetivamente, sem substituir a persistência por arquivos ou estruturas em memória.
- Utilizar consultas parametrizadas sempre que suportadas pelos drivers.
- Implementar tratamento básico de erros, incluindo falhas de conexão, dados inválidos e consultas sem resultados.
- Encerrar adequadamente sessões, conexões e demais recursos utilizados pelos drivers.
- Organizar o código em classes e métodos com responsabilidades bem definidas.

Poderá ser utilizado Maven para gerenciamento das dependências do projeto.

## 6. Modelo de dados sugerido

### 6.1. Neo4j

Cada estudante deverá ser representado por um nó com o rótulo `Estudante` e as seguintes propriedades:

- `id`: identificador único do estudante.
- `nome`: nome do estudante.
- `curso`: curso ao qual está vinculado.

As amizades deverão ser representadas por relacionamentos entre os nós, utilizando o tipo `AMIGO_DE`.

Exemplo conceitual:

```
(Estudante)-[:AMIGO_DE]-(Estudante)
```

A aplicação deverá impedir a criação de estudantes duplicados e evitar relacionamentos redundantes entre o mesmo par de estudantes.

A implementação deverá utilizar o driver oficial do Neo4j para Java para executar operações de criação e consulta dos nós e relacionamentos.

### 6.2. Cassandra

As publicações deverão ser armazenadas em uma tabela projetada para recuperar eficientemente as mensagens de determinado estudante, ordenadas por data e hora.

A tabela poderá conter as seguintes colunas:

- `estudante_id`: identificador do autor.
- `publicacao_id`: identificador único da publicação.
- `conteudo`: conteúdo textual da publicação.
- `data_hora`: data e hora da publicação.

A chave primária deverá ser definida de acordo com as consultas previstas. A modelagem deverá permitir recuperar as publicações de um estudante em ordem cronológica decrescente.

O projeto deverá considerar as particularidades do Cassandra, incluindo a definição de uma chave de partição adequada e das colunas de clustering necessárias à ordenação. Não se deverá depender de consultas que exijam varreduras completas da tabela ou ordenação arbitrária dos dados.

As operações deverão utilizar CQL e o driver oficial do Cassandra para Java.

## 7. Organização sugerida do projeto

Uma possível organização do código é:

- `Main`: inicialização da aplicação e apresentação do menu textual.
- `Estudante`: representação dos estudantes.
- `Publicacao`: representação das publicações.
- `Neo4jDAO`: operações de persistência e consulta de estudantes e amizades.
- `CassandraDAO`: operações de persistência e consulta de publicações.
- `RedeSocialService`: implementação das funcionalidades da aplicação e integração dos resultados dos dois bancos.

Essa estrutura é uma sugestão. Outras organizações serão aceitas, desde que apresentem separação adequada de responsabilidades e código compreensível.

## 8. Entrega

A entrega deverá ser realizada pelo SIGAA e deverá conter:

- Código-fonte completo da aplicação.
- Arquivo `README.md` com instruções de configuração, inicialização e execução.
- Scripts necessários para criar as estruturas de dados e preparar os bancos.
- Arquivos de configuração e dependências necessários para compilar e executar o projeto.
- Exemplos de utilização das funcionalidades implementadas.

O README deverá informar, no mínimo, os requisitos de software, as configurações necessárias para conexão com os bancos, os procedimentos para preparar as estruturas de dados e os comandos para executar a aplicação.

**Importante:** remover diretórios de compilação, caches e outros arquivos locais desnecessários antes de compactar o projeto, reduzindo o tamanho do arquivo encaminhado ao SIGAA. Não incluir credenciais pessoais ou senhas reais nos arquivos entregues.

## 9. Critérios de avaliação

| Critério                                                    | Pontuação |
| ----------------------------------------------------------- | --------- |
| Cadastrar estudante e estabelecer amizade no Neo4j          | 1,0       |
| Publicar mensagem no Cassandra                              | 1,0       |
| Consultar amigos utilizando Cypher e o driver Neo4j         | 1,0       |
| Visualizar publicações dos amigos integrando os dois bancos | 1,0       |
| Consultar relatório de atividade integrando os dois bancos  | 1,0       |
| **Total**                                                   | **5,0**   |

A pontuação de cada critério será atribuída considerando o funcionamento efetivo da funcionalidade, o uso correto dos drivers e das consultas, a persistência dos dados e o atendimento aos requisitos especificados.

## 10. Considerações finais

O trabalho deverá demonstrar que uma aplicação Java Vanilla consegue utilizar Neo4j e Cassandra de maneira complementar, respeitando as características de cada tecnologia.

A avaliação considerará a implementação das cinco funcionalidades, a utilização efetiva dos respectivos drivers, a adequação do modelo de dados e a capacidade de demonstrar o funcionamento da aplicação.

Não é necessário desenvolver interface gráfica, aplicação web, API REST, sistema de autenticação ou funcionalidades completas de CRUD. O escopo está limitado às operações descritas neste enunciado.

