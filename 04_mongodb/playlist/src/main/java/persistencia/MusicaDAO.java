package persistencia;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import negocio.Musica;

public class MusicaDAO {

    private static final String DATABASE = "playlist";
    private static final String COLLECTION = "playlist";
    private String uri;

    public MusicaDAO() {
        this.uri = "mongodb://localhost:27017";
    }

    public boolean atualizar(Musica musica){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> colecao = mongoDatabase.getCollection(COLLECTION);
            Bson filter = Filters.eq("_id", musica.getId());
            Document doc = new Document();
            doc.append("titulo", musica.getTitulo());
            doc.append("duracao", musica.getDuracao());
            Document updateDoc = new Document("$set", doc);
            colecao.updateOne(filter, updateDoc);
            mongoClient.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar música: " + e.getMessage());
        }
        return false;
    }

    public boolean remover(ObjectId objectId) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> colecao = mongoDatabase.getCollection(COLLECTION);
            Bson filter = Filters.eq("_id", objectId);
            DeleteResult result = colecao.deleteOne(filter);
            boolean resultado = false;
            if (result.getDeletedCount() == 1) {
                resultado = true;
            }
            mongoClient.close();
            return resultado;
        } catch (Exception e) {
            System.out.println("Erro ao remover música: " + e.getMessage());
        }
        return false;
    }

    public Musica obter(ObjectId objectId) {
        Musica m = new Musica();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> colecao = mongoDatabase.getCollection(COLLECTION);
            Bson filter = Filters.eq("_id", objectId);
            Document doc = colecao.find(filter).first();           
            m.setId(doc.getObjectId("_id"));
            m.setTitulo(doc.getString("titulo"));
            if (doc.containsKey("duracao") == true) {
                m.setDuracao(doc.getInteger("duracao"));
            }
            mongoClient.close();
            return m;
        } catch (Exception e) {
            System.out.println("Erro ao obter música: " + e.getMessage());
        }
        return m;
    }

    public boolean inserir(Musica m) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> colecao = mongoDatabase.getCollection(COLLECTION);
            Document doc = new Document();
            doc.append("titulo", m.getTitulo());
            doc.append("duracao", m.getDuracao());
            colecao.insertOne(doc);
            mongoClient.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir música: " + e.getMessage());
        }
        return false;
    }


    

    public List<Musica> listar(){
        List<Musica> veList = new ArrayList<>();
        
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> colecao = mongoDatabase.getCollection(COLLECTION);             
            FindIterable<Document> find = colecao.find();
            MongoCursor<Document> cursor = find.iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Musica m = new Musica();
                m.setId(doc.getObjectId("_id"));
                m.setTitulo(doc.getString("titulo"));
                if (doc.containsKey("duracao") == true) {
                    m.setDuracao(doc.getInteger("duracao"));
                }
                veList.add(m);                
            }
            mongoClient.close();
            return veList;            
        } catch (Exception e) {
            System.out.println("Erro ao conectar no MongoDB: " + e.getMessage());
        }
        return veList;
    }

}
