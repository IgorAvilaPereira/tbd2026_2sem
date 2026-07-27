# Casos de Uso do Redis e Projetos Práticos para Sala de Aula

O Redis é uma excelente ferramenta para ensinar conceitos de alta disponibilidade, latência, concorrência e estruturas de dados na prática. Abaixo estão as principais aplicações do Redis no mercado e sugestões de projetos para os alunos desenvolverem.

---

## 1. Principais Casos de Uso no Mercado

Antes do código, é importante que os alunos entendam *por que* as grandes empresas (como Netflix, Twitter e Uber) usam o Redis:

*   **Caching de Dados Dinâmicos**: Armazenar dados lidos frequentemente do banco relacional (ex: catálogo de produtos) para evitar consultas pesadas ao disco.
*   **Gerenciamento de Sessões de Usuário**: Manter o estado de login do usuário centralizado em uma arquitetura de microsserviços. Se um servidor cair, o usuário não perde o login.
*   **Contadores e Limitadores de Taxa (Rate Limiting)**: Controlar quantas requisições um usuário pode fazer por minuto para evitar ataques ou sobrecarga no sistema.
*   **Placar de Líderes em Tempo Real (Leaderboards)**: Rankings de jogos ou sistemas de gamificação atualizados instantaneamente.

---

## 2. Explicação Didática: O Comando INCR

Um dos conceitos mais importantes para ensinar aos alunos ao introduzir o Redis é a **atomicidade**, e o comando `INCR` é a ferramenta perfeita para isso.

### O que o INCR faz?
O comando `INCR chave` incrementa o valor inteiro de uma chave em exatamente `1`. 
* Se a chave **não existir**, o Redis a cria com o valor `0` e depois a incrementa para `1` automaticamente.
* Se a chave existir mas **não for um número inteiro** (ex: uma string de texto), o Redis retorna um erro.

### Por que ele é importante? (A Armadilha da Concorrência)
Em bancos de dados tradicionais ou na memória da aplicação (usando variáveis comuns), uma operação de incremento exige três passos (*Read-Modify-Write*):
1. **Ler** o valor atual (ex: vale 10).
2. **Modificar** o valor na CPU (10 + 1 = 11).
3. **Gravar** o novo valor de volta (vale 11).

Se dois usuários tentarem incrementar a mesma variável ao mesmo tempo, ambos podem ler o valor `10` simultaneamente. Ambos vão calcular `11` e gravar `11`. O resultado final deveria ser `12`, mas vira `11` devido a uma **condição de corrida (Race Condition)**.

### A Solução Atômica do Redis
O Redis possui uma arquitetura *single-threaded* (executa um comando por vez). O comando `INCR` realiza a leitura, a soma e a gravação como uma **única operação atômica isolada**. Nenhum outro comando consegue interceptar esse processo. 

Isso garante que, mesmo que 10.000 alunos cliquem em um botão de incremento exatamente no mesmo milissegundo, a contagem final será perfeitamente exata, sem necessidade de implementar travas complexas (*locks*) no código da aplicação.

---

## 3. Exemplos de Projetos para Desenvolver em Aula

Abaixo estão 2 sugestões de laboratórios práticos para os alunos consolidarem esses conceitos.

### Projeto 1: Limitador de Requisições (Rate Limiter) - Nível Fácil
**Objetivo**: Ensinar o conceito de segurança, controle de tráfego e o comando `INCR` com expiração de chaves no Redis.

*   **Cenário**: Uma API pública que só permite que cada usuário faça 5 requisições por minuto.
*   **Como funciona no Redis**: 
    1. A chave é o IP ou ID do usuário (ex: `user:102:req_count`).
    2. A cada requisição, o sistema roda o comando `INCR`.
    3. Se o valor retornar `1`, significa que é a primeira requisição do minuto. O sistema define um `EXPIRE` de 60 segundos para essa chave.
    4. Se o valor retornado passar de `5`, a API bloqueia a requisição retornando o status `429 Too Many Requests`.

```java
// Exemplo didático em Java/Jedis para os alunos:
public boolean verificarLimite(String userId) {
    String key = "rate:limit:" + userId;
    long totalRequests = jedis.incr(key);
    
    if (totalRequests == 1) {
        jedis.expire(key, 60); // Inicia a janela de 1 minuto apenas na primeira requisição
    }
    
    return totalRequests <= 5; // Retorna falso se estourou o limite
}
```

---

### Projeto 2: Sistema de Votação/Enquete em Tempo Real - Nível Médio
**Objetivo**: Demonstrar o uso de contadores avançados e como o Redis lida com concorrência muito melhor do que bancos relacionais tradicionais.

*   **Cenário**: Uma enquete ao vivo recebendo múltiplos votos por segundo de diversos alunos simultaneamente.
*   **Como funciona no Redis**:
    *   Usar um **Hash** através do comando variante `HINCRBY` para computar de forma atômica os votos de cada opção em uma única chave objeto.
    *   Ou usar um **Sorted Set** (`ZINCRBY`) se o objetivo for exibir de forma automática um ranking ordenado de quais opções estão ganhando a votação.

```bash
# Comandos que os alunos testariam no terminal do Redis:
ZINCRBY enquete:12 1 "Opcao_A"
ZINCRBY enquete:12 1 "Opcao_B"

# Buscar os mais votados para gerar o gráfico na tela:
ZREVRANGE enquete:12 0 -1 WITHSCORES
```
