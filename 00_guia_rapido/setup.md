
# JDK
# Netbeans/VSCODE
# Maven
# PostgreSQL
# [pgAdmin](https://www.pgadmin.org/download/pgadmin-4-apt/)
* [No Linux](https://www.edivaldobrito.com.br/pgadmin4-no-ubuntu/)
# [Docker](https://github.com/IgorAvilaPereira/tbd2024_1sem/blob/main/docker.md)
```
sudo apt install docker.io
```

***

# [KeyDB](https://docs.keydb.dev/)

Alternativa open-source ao Redis

## [Download](https://docs.keydb.dev/docs/download/)

### [Install com Docker](https://hub.docker.com/r/eqalpha/keydb)

```sh
docker pull eqalpha/keydb
```

### [Install PPA-DEB](https://docs.keydb.dev/docs/ppa-deb)
  
```sh
sudo apt install build-essential nasm autotools-dev autoconf libjemalloc-dev tcl tcl-dev uuid-dev libcurl4-openssl-dev libbz2-dev libzstd-dev liblz4-dev libsnappy-dev libssl-dev
```
```sh
echo "deb https://download.keydb.dev/open-source-dist jammy main" | sudo tee /etc/apt/sources.list.d/keydb.list
 sudo wget -O /etc/apt/trusted.gpg.d/keydb.gpg https://download.keydb.dev/open-source-dist/keyring.gpg
sudo apt update
sudo apt install keydb
```
obs: troque *jammy* pelo nome correto da sua versão do seu Ubuntu.

## Status, Start e Stop

```sh
sudo service keydb-server status
sudo service keydb-server start
sudo service keydb-server stop
```

## [Command line](https://docs.keydb.dev/docs/keydbcli)

```sh
keydb-cli 
```

Todos os comandos do Redis são válidos.

## Conexão com Java - Driver [JEDIS](https://github.com/redis/jedis)

Jedis é o mesmo usado para o REDIS

***

## [Redis](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/)

### Distribuições baseadas em Debian/Ubuntu

```bash
sudo apt-get update 
sudo apt-get install redis-server
```

### Docker

```bash
-- 1ª vez
sudo docker run --name redis -p 6379:6379 -d redis:latest
sudo docker exec -it redis redis-cli
-- obs: o sudo pode ser opcional
-- demais vezes
sudo docker start redis
sudo docker exec -it redis redis-cli
-- obs: o sudo pode ser opcional
```

***

## MongoDB

### Docker

```bash
-- 1ª vez
sudo docker run --name some-mongo -p 27017:27017 -d mongo:latest
sudo docker exec -it some-mongo mongosh
-- obs: o sudo pode ser opcional
-- demais vezes
sudo docker start some-mongo
sudo docker exec -it some-mongo mongosh
-- obs: o sudo pode ser opcional
```

 * https://hub.docker.com/_/mongo

### Distribuições baseadas em Debian/Ubuntu

 * https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-ubuntu/

***

## Neo4j

```bash
-- 1ª vez
sudo docker run \
--publish=7474:7474 --publish=7687:7687 \
--volume=$HOME/neo4j/data:/data \
--env=NEO4J_AUTH=neo4j/password \
--name neo4j neo4j

-- demais vezes
sudo docker start neo4j
sudo docker exec -it neo4j bash
```
* Acessar o _client_ em [http://localhost:7474/browser/](http://localhost:7474/browser/)

* Para as conexões (via programas/código) usar a porta 7687

**obs:** 

* o parâmetro --volume define o PATH onde as informações salvas serão armazenadas;
* o parâmetro --volume pode ser também usado para os comandos Docker dos demais BD's. Sem --volume as informações armazenadas serão perdidas após cada execução (exec);

***

## Cassandra

```bash
-- 1ª vez
sudo docker pull cassandra

sudo docker run --name cassandra -p \
127.0.0.1:9042:9042 -p 127.0.0.1:9160:9160 -d cassandra

sudo docker exec -it cassandra cqlsh

-- demais vezes
sudo docker start cassandra
sudo docker exec -it cassandra cqlsh
```
[Baixar todo o material da aula](https://download-directory.github.io/?url=http://github.com/IgorAvilaPereira/tbd2025_1sem/tree/main/./setup)
