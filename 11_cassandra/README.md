

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

```xml
<dependencies>
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-core</artifactId>
            <version>3.11.0</version>
        </dependency>
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-mapping</artifactId>
            <version>3.11.0</version>
        </dependency>
    </dependencies>
```



[Baixar todo o material da aula](https://download-directory.github.io/?url=http://github.com/IgorAvilaPereira/tbd2026_2sem/tree/main/11_cassandra)

&nbsp;
