package apresentacao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinMustache;
import negocio.ToDo;
import negocio.Usuario;
import persistencia.ToDoDAO;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {

            config.fileRenderer(new JavalinMustache());
            config.routes.get("/list", ctx -> {
                Map<String, Object> map = new HashMap<>();
                map.put("vetTodo", new ToDoDAO().listar());
                ctx.render("/templates/list.html", map);
            });

            config.routes.get("/", ctx -> {
                Map<String, String> map = new HashMap<>();
                ctx.render("/templates/index.html", map);
            });

             config.routes.get("/delete/{id}", ctx -> {
                new ToDoDAO().deletar(ctx.pathParam("id"));
                ctx.redirect("/");
            });

              config.routes.get("/screen_edit/{id}", ctx -> {
                Map<String, Object> map = new HashMap<>();
                ToDo todo = new ToDoDAO().obter(ctx.pathParam("id"));
                map.put("todo", todo);
                ctx.render("/templates/screen_edit.html", map);
            });


            config.routes.post("/edit", ctx -> {
                String titulo = ctx.formParam("titulo");
                String texto = ctx.formParam("texto");
                String data = ctx.formParam("data");
                String responsavel = ctx.formParam("responsavel");
                String id = ctx.formParam("id");
                // int segundos = Integer.parseInt(ctx.formParam("segundos"));
                ToDo toDo = new ToDo();
                toDo.setId(UUID.fromString(id));
                toDo.setTitulo(titulo);
                toDo.setTexto(texto);
                toDo.setResponsavel(new Usuario(responsavel));
                toDo.setData(LocalDate.parse(data));

                new ToDoDAO().salvar(toDo);
                ctx.redirect("/list");
            });


            config.routes.post("/add", ctx -> {
                String titulo = ctx.formParam("titulo");
                String texto = ctx.formParam("texto");
                String data = ctx.formParam("data");
                // System.out.println(data);
                String responsavel = ctx.formParam("responsavel");
                int segundos = Integer.parseInt(ctx.formParam("segundos"));
                ToDo toDo = new ToDo();
                toDo.setTitulo(titulo);
                toDo.setTexto(texto);
                toDo.setResponsavel(new Usuario(responsavel));
                toDo.setData(LocalDate.parse(data));
                if (segundos != 0) new ToDoDAO().salvar(toDo, segundos);
                else new ToDoDAO().salvar(toDo);
                ctx.redirect("/");
            });

        }).start(7070);

        // new ToDoDAO().listar().forEach(p -> System.out.println(p));
        // ToDo tarefa1 = new ToDo();
        // tarefa1.setTexto("Reescrever um código aceitável!");
        // tarefa1.setTitulo("tarefa");
        // tarefa1.setResponsavel(new Usuario("David"));
        // tarefa1.setData(LocalDate.now());
        // new ToDoDAO().salvar(tarefa1);
        // obter
        // System.out.println(new
        // ToDoDAO().obter("5425be51-c8bf-475b-8330-0d81d2a0fc37").getData().toString());
        // deletar
        // new ToDoDAO().deletar("c9456846-b46b-408a-a781-c9163cc5fcae");
    }
}