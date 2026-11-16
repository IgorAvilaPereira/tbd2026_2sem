package apresentacao;

import java.util.Iterator;
import java.util.List;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Main {
    public static void main(String[] args) {

        Cluster cluster = null;
        try {
            cluster = Cluster.builder().addContactPoint("127.0.0.1")
                    .build();
            Session session = cluster.connect("exemplo_cassandra");
            List<Row> vet = session.execute("SELECT * FROM pessoa").all();
            Iterator<Row> iterator = vet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                System.out.println(row.getString("cpf"));
                System.out.println(row.getList("gostos", String.class).toString());
            }
            PreparedStatement cqlInsert = session.prepare("INSERT INTO pessoa (cpf, nome, nascimento, gostos) VALUES (?, ?, ?, ?);");
            BoundStatement bound = cqlInsert.bind("5555555555", "igor pereira", LocalDate.fromYearMonthDay(1987,1,20), List.of("teatro", "literatura"));
            session.execute(bound);

            // PreparedStatement cqlDelete = session.prepare("DELETE FROM pessoa WHERE cpf = ?");
            // BoundStatement bound2 = cqlDelete.bind("44444444444");
            // session.execute(bound2);

            PreparedStatement cqlUpdate = session.prepare("UPDATE pessoa SET gostos = ? where cpf = ?");
            BoundStatement bound3 = cqlUpdate.bind(List.of("culinaria", "musica", "esportes"), "44444444444");
            session.execute(bound3);
            
        } finally {
            if (cluster != null)
                cluster.close();
        }
    }
}