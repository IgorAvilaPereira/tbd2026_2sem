package mongo_vendas;

import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.max;
import static com.mongodb.client.model.Accumulators.min;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Indexes.ascending;

public class Main {

    public static void main(String[] args) {

        String uri = "mongodb://localhost:27017";

        Gson gson = new Gson();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> vendas = database.getCollection("vendas");

            var resultado = vendas.aggregate(
                    List.of(
                            //     match(
                            //         Filters.eq(
                            //                 "cidade",
                            //                 "Rio Grande"
                            //         )
                            // ),
                            group(
                                    
                                    "$cidade",
                                    sum(
                                            "faturamento",
                                            new Document(
                                                    "$multiply",
                                                    List.of(
                                                            "$quantidade",
                                                            "$preco"
                                                    )
                                            )
                                    ),
                                    sum("quantidadeTotal", "$quantidade"),
                                    avg("precoMedio",
                                            "$preco"),
                                    max("precoMax", "$preco"),
                                    min(
                                            "menorPreco",
                                            "$preco"
                                    )), sort(ascending("_id"))));

            var html = "";
            for (Document doc : resultado) {
                System.out.println(doc.toJson());
                // System.out.println(doc.get("_id") + ":" + doc.get("precoMedio"));
            }
            // System.out.println(html);
        }
    }
}
