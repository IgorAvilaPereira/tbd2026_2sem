package persistencia;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;

/**
 * UrlDAO
 */
public class UrlMongoDAO {
    public static final String uri = "mongodb://localhost:27017";
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public UrlMongoDAO() {

    }

    public String encurtar(String url, String urlEncurtada) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("minha_base");
            collection = database.getCollection("url_collection");
            Document doc = collection.find(eq("url", url)).first();
            if (doc == null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("url", url);
                map.put("urlEncurtada", urlEncurtada);
                map.put("acessos", 0);
                Document document = new Document(map);
                collection.insertOne(document);
                return urlEncurtada;
            } else {
                return doc.get("urlEncurtada").toString();

            }

        }

    }

    public String obter(String urlEncurtada) {
        long inicio = System.nanoTime();                      
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("minha_base");
            collection = database.getCollection("url_collection");
            Document doc = collection.find(eq("urlEncurtada", urlEncurtada)).first();
            if (doc != null) {
                long fim = System.nanoTime();
                long duracaoNano = fim - inicio;
                long duracaoMillis = duracaoNano / 1_000_000; // Converte para milissegundos
                System.out.println("Tempo de execução: " + duracaoMillis + " ms");
                int acessos = Integer.parseInt(doc.get("acessos").toString());
                acessos++;
                collection.updateOne(eq("urlEncurtada", doc.get("urlEncurtada").toString()), set("acessos", acessos));
                return doc.get("url").toString();
            }
        }
        return null;
    }

}
