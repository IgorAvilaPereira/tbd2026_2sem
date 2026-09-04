package apresentacao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import negocio.Pessoa;
import negocio.Tarefa;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    public static void main(String[] args) {

        String uri = "mongodb://localhost:27017";
        String dbname = "test";
        String collectionName = "tarefas";

        Tarefa tarefaNova = new Tarefa();
        tarefaNova.setTitulo("Estudar MongoDB");
        tarefaNova.setDescricao("Aprender integração Java com MongoDB");
        tarefaNova.setPessoa(new Pessoa("Igor"));

        ConnectionString connectionString = new ConnectionString(uri);

        CodecRegistry pojoCodecRegistry =
                fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry codecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                );

        MongoClientSettings clientSettings =
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .codecRegistry(codecRegistry)
                        .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {

            MongoDatabase db = mongoClient.getDatabase(dbname);

            // MongoCollection<Tarefa> tarefaCollection =
            //         db.getCollection(collectionName, Tarefa.class);

            // tarefaCollection.insertOne(tarefaNova);


            MongoCollection<Pessoa> pessoaCollection = db.getCollection("pessoas", Pessoa.class);
            // pessoaCollection.insertOne(new Pessoa("Igor"));
            FindIterable<Pessoa> iterable =  pessoaCollection.find();
            MongoCursor<Pessoa> cursor =  iterable.cursor();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().getNome());
            }    
            System.out.println("Tarefa inserida com sucesso!");
        }
    }
}