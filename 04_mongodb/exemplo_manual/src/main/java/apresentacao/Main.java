package apresentacao;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("manual");
            MongoCollection<Document> collection = database.getCollection("clientes");
            // ex: insert
            Map<String, String> map = new HashMap<String, String>();
            map.put("nome", "Bruno Ribeiro de Ribeiro");
            Document doc = new Document(map);
            collection.insertOne(doc);
            // ====================================
            // ex: delete
            collection.deleteOne(eq("nome", "Bruno Ribeiro de Ribeiro"));
            // ====================================
            // ex: update
            collection.updateOne(eq("nome", "Marcio"), set("nome", "Marcio Josue Ramos Torres"));
            // ====================================
            // ex: select
            FindIterable<Document> iterable = collection.find();
            MongoCursor<Document> cursor = iterable.cursor();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().get("nome"));
            }
            // ====================================
            // if (doc != null) {
            // System.out.println(doc.toJson());
            // } else {
            // System.out.println("No matching documents found.");
            // }
        }

    }
}