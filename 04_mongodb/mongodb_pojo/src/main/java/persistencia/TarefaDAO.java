package persistencia;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import static com.mongodb.client.model.Filters.eq;


import negocio.Tarefa;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private String uri;
    private String dbname;
    private String collectionName;
    private ConnectionString connectionString;
    private CodecRegistry pojoCodecRegistry;
    private CodecRegistry codecRegistry;
    private MongoClientSettings clientSettings;

    public TarefaDAO(){
        uri = "mongodb://localhost:27017";
        dbname = "test";
        collectionName = "tarefas";
        connectionString = new ConnectionString(uri);
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .codecRegistry(codecRegistry)
        .build();
    }
      

    public void adicionar(Tarefa tarefaNova){         
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase(dbname);
            MongoCollection<Tarefa> tarefaCollection = db.getCollection(collectionName, Tarefa.class);
            tarefaCollection.insertOne(tarefaNova);
        }
    }

    public void remover(ObjectId id){
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase(dbname);
            MongoCollection<Tarefa> tarefaCollection = db.getCollection(collectionName, Tarefa.class);
            Bson filter = eq("_id", id);
            tarefaCollection.deleteOne(filter);
        }
    }

    public Tarefa obter(ObjectId id){
        Tarefa tarefa = new Tarefa();
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {            
            MongoDatabase db = mongoClient.getDatabase(dbname);
            MongoCollection<Tarefa> tarefaCollection = db.getCollection(collectionName, Tarefa.class);
            Bson filter = eq("_id", id);
            FindIterable<Tarefa> iterator =  tarefaCollection.find(filter);
            tarefa = iterator.first();
        }
        return tarefa;
    }

    public void atualizar(Tarefa tarefa){
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {            
            MongoDatabase db = mongoClient.getDatabase(dbname);
            MongoCollection<Tarefa> tarefaCollection = db.getCollection(collectionName, Tarefa.class);
            Bson filter = eq("_id", tarefa.getId());
            FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            tarefaCollection.findOneAndReplace(filter, tarefa,  returnDocAfterReplace);
        }

    }


    public List<Tarefa> listar() {
        List<Tarefa> vetTarefa = new ArrayList<Tarefa>();
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {            
            MongoDatabase db = mongoClient.getDatabase(dbname);
            MongoCollection<Tarefa> tarefaCollection = db.getCollection(collectionName, Tarefa.class);
            MongoCursor<Tarefa> mongoCursor =  tarefaCollection.find().iterator();
            while(mongoCursor.hasNext()){
                vetTarefa.add(mongoCursor.next());
            }            
        }
        return vetTarefa;

    }
}
