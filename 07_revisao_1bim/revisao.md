## Aula de Revisão: Persistência NoSQL (MongoDB e Redis) com Java Vanilla
Sejam bem-vindos à nossa aula de revisão para a Atividade Avaliada! Hoje vamos passar pelos conceitos mais importantes e básicos para conectar o Java nativo diretamente ao MongoDB e ao Redis.
Esqueçam frameworks complexos (como Spring Data ou Hibernate OGM). Nosso foco aqui é o Java Vanilla: abrir a conexão na unha, executar o comando e fechar a conexão de forma segura.
------------------------------
## Parte 1: MongoDB (Banco Orientado a Documentos)
O MongoDB é um banco NoSQL que não utiliza tabelas ou linhas. Ele armazena registros no formato de Documentos (estruturas muito parecidas com o JSON). A grande vantagem é que ele é schemaless (sem esquema rígido), ou seja, cada documento dentro da mesma coleção pode ter atributos diferentes.
## 1. Os 3 Pilares do Driver do MongoDB
Para injetar ou buscar dados no MongoDB via Java nativo, você precisa entender o ciclo de vida dessas três classes fundamentais:

* MongoClient: É o motor da conexão. Ele representa o pool de conexões com o servidor do MongoDB (ex: localhost:27017). Regra de ouro: ele consome recursos físicos e precisa ser fechado ao terminar as operações!
* MongoDatabase: Representa um banco de dados específico criado no servidor (ex: banco "loja" ou banco "escola"). Você o obtém diretamente a partir do seu cliente.
* MongoCollection<Document>: Representa a coleção física (o equivalente às antigas "tabelas") onde os documentos ficam guardados. É através deste objeto que disparamos comandos como .insertOne().

## 2. Manipulando JSON com a classe Document
Como no Java não escrevemos JSON diretamente em texto corrido para salvar no banco, o driver nos fornece a classe Document.
Se você já tem uma String em formato JSON puro vindo de uma API ou de um arquivo, você não precisa mapear atributo por atributo. Basta usar o método estático:

Document doc = Document.parse(jsonString);

Isso converte o texto instantaneamente em uma estrutura de dados que o MongoDB compreende.
------------------------------
## Parte 2: Redis (Banco Chave-Valor em Memória)
Se o MongoDB serve para guardar dados complexos e estruturados, o Redis serve para velocidade extrema. Ele guarda tudo na memória RAM e trabalha com a estrutura de Chave e Valor. É muito utilizado para sistemas de cache, contadores de acesso e gerenciamento de sessões de login (tokens).
## 1. Conexão Vanilla com o Driver Jedis
O driver mais tradicional e direto para conectar o Java ao Redis chama-se Jedis.
Conectar com ele é tão simples quanto instanciar um objeto passando o endereço do servidor:

Jedis jedis = new Jedis("localhost", 6379);

Assim como o cliente do Mongo, o objeto Jedis representa um socket de rede aberto. Se você esquecer de fechá-lo, seu servidor eventualmente vai travar por excesso de conexões abertas.
## 2. O conceito de TTL (Time-To-Live) e Expiração
Uma das funções mais fantásticas do Redis é a capacidade de dar um "tempo de vida" para uma informação. Passado esse tempo, o próprio Redis deleta o dado da memória RAM sozinho.
Para salvar um dado do tipo String e já dizer quanto tempo ele deve durar de forma atômica (tudo em uma única operação), utilizamos o método setex:

jedis.setex(chave, tempoEmSegundos, valor);

Exemplo prático: Se você salvar um token de login com o tempo de 900 segundos, o sistema ganha expiração automática de 15 minutos sem que você precise criar rotinas complexas no Java para apagar registros velhos.
------------------------------
## Resumo Prático para a Prova: Gerenciamento de Recursos
Para garantir nota máxima na parte de código, lembrem-se sempre de gerenciar os recursos de rede. Em Java moderno, a melhor forma de garantir que o MongoClient ou o Jedis sejam fechados mesmo se ocorrer um erro no meio do processo é utilizando o try-with-resources:

// Exemplo genérico com Redistry (Jedis jedis = new Jedis("localhost", 6379)) {
    // Seu código aqui dentro...
    // O Java fecha a conexão automaticamente ao sair do bloco 'try'!
} catch (Exception e) {
    // Trata falhas de rede ou banco fora do ar
}

Para ajudar a fixar esses conceitos antes da atividade, o que você prefere que façamos agora?

* Podemos fazer um simulado rápido de 3 perguntas teóricas de fixação.
* Posso te mostrar a linha de comando (CLI) equivalente aos comandos Java que revisamos.
* Podemos ver o mecanismo de tratamento de exceções caso o banco esteja fora do ar.


