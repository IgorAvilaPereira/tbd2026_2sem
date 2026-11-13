
## 📖 Guia Completo: Apache Cassandra para Aulas

## 1. Visão Geral e Aplicações Reais
O Apache Cassandra é um banco de dados NoSQL (não relacional) altamente distribuído. Ele foi projetado para gerenciar volumes gigantescos de dados (Big Data) espalhados por múltiplos servidores comuns, oferecendo alta disponibilidade e desempenho linear sem um ponto único de falha.
## Casos de Uso no Mercado

* Streaming e Histórico (Netflix): Armazena o ponto exato onde o usuário parou de assistir a um vídeo e alimenta o histórico de perfis de milhões de acessos simultâneos.
* Internet das Coisas (IoT) e Telemetria: Recebe e grava bilhões de dados vindos de sensores (frotas de veículos, medidores térmicos) por segundo, devido à sua alta velocidade de escrita.
* Mensageria e Chats (Instagram): Armazena feeds de atividades e históricos de conversas em tempo real com entrada contínua de dados.
* Detecção de Fraude Financeira: Análise em tempo real de transações de cartões de crédito para identificar comportamentos suspeitos imediatamente.

------------------------------
## 2. Comparativo: Cassandra vs. SGBD Relacional
Embora a sintaxe da linguagem CQL (Cassandra Query Language) se pareça visualmente com o SQL tradicional (usando comandos como SELECT e INSERT), o funcionamento interno e os objetivos de cada sistema são opostos.

| Característica | SGBD Relacional (MySQL / PostgreSQL) | Apache Cassandra |
|---|---|---|
| Arquitetura | Centralizada: Baseada em um servidor principal (Master) focado em consistência extrema. | Distribuída (Masterless): Todos os computadores (nós) trabalham juntos como iguais, sem um nó mestre. |
| Escalabilidade | Vertical (Scale-up): Exige um servidor maior, com mais memória e mais CPU devido a limites físicos. | Horizontal (Scale-out): Para expandir a capacidade, basta plugar novos computadores comuns à rede. |
| Modelagem | Normalizada: Focada em tabelas separadas para evitar a repetição de dados. | Denormalizada: Focada em consultas específicas. Os dados são duplicados de propósito. |
| Operações | JOINs Liberados: Permite cruzar dados de múltiplas tabelas dinamicamente na mesma consulta. | Proibido usar JOIN: O banco não cruza dados. Toda a informação deve estar em uma única tabela. |
| Foco Principal | Consistência (ACID): O dado inserido precisa estar idêntico e atualizado para todos os usuários na mesma hora. | Disponibilidade (Teorema CAP): O sistema prioriza nunca cair, mesmo sob falhas graves na rede. |

------------------------------
## 3. Arquitetura e Mecanismos de Velocidade## Tolerância a Falhas sem Nó Mestre
Em bancos relacionais tradicionais do tipo Master/Slave, o Master coordena as escritas e os Slaves realizam as leituras. Se o Master falha, o sistema para de aceitar novos dados.
O Cassandra elimina esse gargalo adotando uma arquitetura Masterless (sem mestre). Como todos os nós do cluster possuem o mesmo papel, qualquer máquina pode receber operações de escrita ou leitura. Se um computador falhar, os nós vizinhos assumem o trabalho instantaneamente, garantindo zero interrupção.
## Chave Primária (PK) Composta
No Cassandra, o conceito de Chave Primária divide-se em duas partes com funções físicas distintas na organização dos arquivos:
$$\text{Primary Key (PK)} = \text{Partition Key} + \text{Clustering Key (Opcional)}$$ 

* Chave de Partição (Partition Key): Define em qual computador do cluster o dado será guardado. O Cassandra aplica uma função matemática (Hash) no valor dessa chave para descobrir o servidor exato do registro. Ele não busca no cluster inteiro, indo direto na máquina certa.
* Chave de Ordenação (Clustering Key): Define como o dado será ordenado fisicamente dentro do disco rígido daquela máquina. Garante que registros relacionados fiquem gravados sequencialmente no mesmo bloco de armazenamento, acelerando a paginação.

## Escrita em Memória e Concorrência (No Locks)

* MemTable e CommitLog: Quando uma escrita ocorre, ela é registrada simultaneamente em um arquivo de segurança rápido de texto (CommitLog) e armazenada na memória RAM (MemTable). A operação é confirmada para o usuário imediatamente (em sub-milissegundos). Posteriormente, o Cassandra descarrega a RAM para o disco (SSTables) de forma assíncrona.
* Sem Trancas (No Locks): Diferente dos bancos relacionais que travam linhas ou tabelas durante atualizações para evitar dados inconsistentes, o Cassandra nunca bloqueia registros. Ele simplesmente grava a informação nova com um carimbo de data/hora (timestamp). O dado com o carimbo mais recente é o escolhido durante a leitura.

------------------------------
## 4. Estratégia de Modelagem baseada em Consultas
Como a operação de JOIN não existe no Cassandra, a modelagem de dados é feita estritamente com base nas perguntas que a aplicação precisa responder, e não nas entidades do sistema de forma isolada.
## Exemplo Prático: Sistema de Playlists## Cenário Relacional (SQL)
Os dados ficariam separados em 3 tabelas puras para evitar redundância: playlists, musicas e uma tabela intermediária playlist_musicas. O banco processa o relacionamento e junta as partes em tempo de execução.
## Cenário Cassandra (NoSQL)
Criamos duas tabelas independentes e isoladas para responder a duas telas específicas da aplicação:
Pergunta 1: "Quais são as músicas de uma determinada playlist?"

* Tabela: musicas_por_playlist
* Chave de Partição: id_playlist (agrupa as músicas da mesma playlist no mesmo nó)
* Chave de Ordenação: ordem (mantém a lista de músicas ordenada no disco)

| id_playlist (Partition Key) | ordem (Clustering Key) | id_musica | titulo | artista |
|---|---|---|---|---|
| rock_80s | 1 | m_901 | Take On Me | A-ha |
| rock_80s | 2 | m_342 | Beat It | Michael Jackson |

Pergunta 2: "Quais são os detalhes de uma música específica?"

* Tabela: detalhes_da_musica
* Chave de Partição: id_musica (permite encontrar uma única música diretamente por seu ID único)

| id_musica (Partition Key) | titulo | artista | album | ano |
|---|---|---|---|---|
| m_901 | Take On Me | A-ha | Hunting High and Low | 1985 |
| m_342 | Beat It | Michael Jackson | Thriller | 1982 |

------------------------------
## 🔗 Referências Bibliográficas

* [1] HABBEMA, Hugo. Introdução ao Cassandra: Um Banco de Dados colunar com alta escalabilidade e disponibilidade. Medium, 2023. Disponível em: https://medium.com/@habbema/introdu%C3%A7%C3%A3o-ao-cassandra-bb3af92dbc80.
* [2] IONOS. MongoDB vs. Cassandra. Digital Guide Ionos. Disponível em: https://www.ionos.com/pt-br/digitalguide/servidor/conhecimento/mongodb-vs-cassandra/.
* [3] AUTOR INDEFINIDO. Arquitetura e Conceitos Distribuidos do Apache Cassandra. YouTube. Disponível em: https://www.youtube.com/watch?v=4pgdLCu-8zs.
* [4] SANTOS, Hudson dos. Desvendando Cassandra - Parte 01. Blog Hudson dos Santos, 2021. Disponível em: https://hudsondosantos.com/2021/01/06/desvendando-cassandra-prt-01/.
* [5] DEVMEDIA. Introdução ao banco de dados NoSQL Cassandra. DevMedia. Disponível em: https://www.devmedia.com.br/introducao-ao-banco-de-dados-nosql-cassandra/30533.
* [6] IBM. O que é o Apache Cassandra?. IBM Topics. Disponível em: https://www.ibm.com/br-pt/think/topics/cassandra.

Agora que o documento de fundamentos está limpo e consolidado, gostaria de prosseguir com qual etapa da preparação da aula?

* Quer que eu monte um roteiro de perguntas e respostas rápidas baseado neste texto para fixação dos alunos?
* Deseja o arquivo Docker Compose estruturado com um script de inicialização do keyspace e tabelas do Cassandra?



