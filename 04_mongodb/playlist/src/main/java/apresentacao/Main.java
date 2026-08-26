package apresentacao;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.javalin.Javalin;
import negocio.Musica;
import persistencia.MusicaDAO;

public class Main {
    public static void main(String[] args) {
        // new MusicaDAO().listar()
                // .forEach(m -> System.out.println(m.getId() + " - " + m.getTitulo() + " - " + m.getDuracao()));
        // System.out.println(new MusicaDAO().remover(new ObjectId("68ed7eb6f7de24434f7bdc0f")));


        // Musica m = new Musica();
        // m.setId(new ObjectId("68ed8419f487304bf233935a"));
        // m.setTitulo("Aquarela do Brasil - Atualizado");
        // m.setDuracao(200);
        // System.out.println(new MusicaDAO().atualizar(m));

        // Musica m = new Musica();
        // m.setTitulo("Aquarela do Brasil");
        // m.setDuracao(180);
        // if (new MusicaDAO().inserir(m) == true) {
        //     System.out.println("Música inserida com sucesso!");
        // } else {
        //     System.out.println("Erro ao inserir música!");
        // }

         var app = Javalin.create(/*config*/)
            .get("/", ctx -> ctx.result("Hello World"))
            .start(7070);

        app.get("/musicas", ctx -> ctx.json(new MusicaDAO().listar()));

            
        // Gson gson = new Gson();
        // System.out.println(gson.toJson(new MusicaDAO().listar()));
    }
}