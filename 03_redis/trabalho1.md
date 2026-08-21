
## 📋 Escopo do Trabalho: Sistema de Rastreamento de Veículos

O sistema consiste em uma aplicação de console em Java que executa três operações de negócio integradas ao Redis e ao MongoDB.
------------------------------
## ⚙️ Funcionalidades e Distribuição de Pontos## 1. Limpeza Automática de Histórico Antigo — [Valor: 1,0 Ponto]

* Remoção de Dados: O sistema deve oferecer uma funcionalidade para apagar do MongoDB todos os registros de localização que sejam mais antigos que uma quantidade de dias informada pelo usuário.
* Sincronização: Após a limpeza no MongoDB, o sistema deve remover a última posição correspondente desse veículo também no Redis, garantindo que nenhuma informação desatualizada permaneça em cache.

## 2. Registro de Telemetria (Escrita) — [Valor: 2,0 Pontos]

* Entrada de Dados: O sistema recebe via teclado o identificador do veículo, a latitude e a longitude.
* Gravação em Memória (Redis): O sistema salva a localização atualizada do veículo associada ao seu identificador com um tempo de expiração (TTL) de 5 minutos.
* Gravação em Disco (MongoDB): O sistema insere permanentemente a mesma coordenada recebida dentro do histórico de rotas daquele veículo.

## 3. Consulta de Localização (Leitura) — [Valor: 2,0 Pontos]

* Busca de Informação: O sistema solicita o identificador de um veículo para retornar sua última posição.
* Fluxo de Cache Prévio: O sistema tenta ler o dado diretamente no Redis. Se a informação estiver lá, ela é exibida imediatamente ao usuário.
* Fluxo de Recuperação: Se a informação não estiver no Redis (por ter expirado ou nunca ter sido salva), o sistema busca o registro mais recente no MongoDB, exibe o resultado na tela e salva essa informação de volta no Redis com a expiração de 5 minutos.
