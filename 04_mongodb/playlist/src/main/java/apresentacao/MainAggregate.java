package apresentacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import negocio.Musica;

public class MainAggregate {

    public static void main(String[] args) {

        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
            MongoCollection<Document> colecao = mongoDatabase.getCollection("orders");
            colecao.aggregate(
                    Arrays.asList(
                        // pipeline 1
                            Aggregates.match(Filters.eq("name", "Cheese")),
                            // pipeline 2
                            Aggregates.group("$name", Accumulators.sum("total", 1)))
            // Prints the result of the aggregation operation as JSON
            ).forEach(doc -> System.out.println(doc.toJson()));

        }
    }

}
