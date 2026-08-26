package com.example;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";
        
        try(MongoClient mongoClient = MongoClients.create(uri)){
            MongoDatabase database = mongoClient.getDatabase("peruca");
            MongoCollection<Document> collection = database.getCollection("solicitantes");
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next().get("nome"));
            }
            Document document = new Document();
            document.put("nome", "aiaiaia");
            Document documentoInterno = new Document();
            documentoInterno.put("rua", "oi");
            documentoInterno.put("bairro", "oi");
            document.put("endereco", documentoInterno);
            Document documentoInterno2 = new Document();
            documentoInterno2.put("rua", "oi");
            documentoInterno2.put("bairro", "oi");
            documentoInterno.put("endereco", documentoInterno2);
            collection.insertOne(document);
        }
    }
}