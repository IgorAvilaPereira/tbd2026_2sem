package apresentacao;

import java.time.LocalDate;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCursor;

import negocio.Cupom;
import persistencia.CupomDAO;

public class Main {
    public static void main(String[] args) {
        Cupom cupom = new Cupom();
        cupom.setValor(2000);
        cupom.setDataInicio(LocalDate.now());
        cupom.setDataFim(LocalDate.now().plusDays(7));
        cupom.setValorMinimo(300);
        cupom.getCategorias().add("cozinha");
        System.out.println(new CupomDAO().adicionar(cupom));
        //
        // System.out.println(new CupomDAO().deletar(new
        // ObjectId("6aa875abc03ba733bf25d39e")));
        //
        MongoCursor<Cupom> cursor = new CupomDAO().listar();
        while (cursor.hasNext()) {
        System.out.println(cursor.next());
        }

        // Cupom c = new CupomDAO().obter(new ObjectId("6aa875b662c9494240f0f2a7"));
        // c.setValor(100);
        // new CupomDAO().atualizar(c);
    }
}