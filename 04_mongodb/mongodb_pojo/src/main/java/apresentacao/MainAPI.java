package apresentacao;

import io.javalin.Javalin;
import negocio.Tarefa;
import negocio.TarefaController;

public class MainAPI {

    public static void main(String[] args) {
        TarefaController tarefaController = new TarefaController();

        var app = Javalin.create(/* config */)
                .get("/listar", ctx -> ctx.json(tarefaController.listar())).

                get("/remover/<id>", ctx -> ctx.json(tarefaController.remover(ctx.pathParam("id"))))
                
                .start(7070);
    }

}
