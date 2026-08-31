package mongodb_document;

import org.bson.Document;
import static com.mongodb.client.model.Filters.*;


import java.util.HashMap;
import java.util.Map;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;


/*
Extensões MongoDB VSCODE:
   Clients:
        >> MongoDB
        >> GuDB
    
   SQL -> MongoDB Converter  

*/

public class Main {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("minha_base");
            MongoCollection<Document> collection = database.getCollection("professores");
            // find all
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next().toJson());
            }
            
            // find
            // Document doc = collection.find(eq("nome", "luciano")).first();
            // if (doc != null) {
            // System.out.println(doc.toJson());
            // }

            // delete
            // DeleteResult deleteResult = collection.deleteOne(Filters.eq("nome",
            // "luciano"));
            // // if (deleteResult.getDeletedCount() == 1) {
            // System.out.println("SUCESSO!");
            // } else {
            // System.out.println("No matching documents found.");
            // }

            // insert
            // Map<String, Object> map = new HashMap<String, Object>();
            // map.put("nome", "igor");
            // Document document = new Document(map);
            // collection.insertOne(document);

            // update
            collection.updateOne(eq("nome", "igor"), set("nome", "Marcio Josue Ramos Torres"));


        }

    }
}