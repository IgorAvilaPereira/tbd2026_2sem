package apresentacao;

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
import com.mongodb.client.model.Filters;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.List;

import negocio.*;


public class Main {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";
        ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry).build();
       
        try(MongoClient mongoClient = MongoClients.create(mongoClientSettings)) {
            MongoDatabase db = mongoClient.getDatabase("playlist");
            MongoCollection<Musica> collection = db.getCollection("musicas", Musica.class);
            
            // insert
            // Musica musica = new Musica();
            // musica.setTitulo("Restart");
            // musica.setNomeArtista("Eu te esperar!");
            // musica.setDuracao(200);
            // musica.setId(new ObjectId(collection.insertOne(musica).getInsertedId().asObjectId().getValue().toString()));
            // System.out.println(musica.getId().toString());

            // listar todos
            // FindIterable<Musica> iterable = collection.find();
            // MongoCursor<Musica> cursor = iterable.cursor();
            // while (cursor.hasNext()) {            
            //     System.out.println(cursor.next().getTitulo());
            // }

            // deleteOne
            String id = "68f6bbaa69307947e699b60c";
            ObjectId objectId = new ObjectId(id);
            Bson filter = Filters.eq("_id", objectId);
            collection.deleteOne(filter);

            // find one
            id = "68f6befda3318d11ae93a55d";
            objectId = new ObjectId(id);
            Bson filterRestart = Filters.eq("_id", objectId);
            Musica musicaObtida = collection.find(filterRestart).first();

            // update
            musicaObtida.setNomeArtista("Falamansa Barela");
            musicaObtida.setTitulo("Xote da Alegria");
            collection.replaceOne(filterRestart, musicaObtida);


            // https://www.baeldung.com/java-mongodb-filters
            id = "68f6befda3318d11ae93a55d";
            objectId = new ObjectId(id);
            Bson filterDelete = Filters.eq("_id", objectId);
            
            collection.findOneAndDelete(filterDelete);
            
        }
    }
}