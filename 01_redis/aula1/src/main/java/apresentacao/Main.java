package apresentacao;

import java.util.UUID;

import negocio.Nota;
import persistencia.NotaDAO;

public class Main {
    public static void main(String[] args) {
        NotaDAO notaDAO = new NotaDAO();
        // deletar todos
        // notaDAO.deletarTodos();
           // inserir
        // Nota nota = new Nota();
        // nota.setTexto("Atividade 3 - Meu TCC");
        // nota.setTitulo("pensar em nomes de orientadores");
        // notaDAO.salvar(nota);

        Nota notaBuscada = notaDAO.buscar(UUID.fromString("16bd2d1a-bad2-4737-a584-0d87fc5c7e06"));
        System.out.println(notaBuscada.getTitulo());

        // listar
        notaDAO.listar().forEach    (p -> System.out.println(p.getDataHora().toString()));

        // deletar
        // notaDAO.deletar(UUID.fromString("126d5339-65be-4b3a-82ee-3e89a8e9cbc8"));

     
    }
}