package apresentacao;

import java.util.Iterator;
import java.util.List;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import negocio.Pessoa;

public class MainMapping {
    public static void main(String[] args) {

        Pessoa p = null;
        p = new Pessoa("33333333333", "Fulano", LocalDate.fromYearMonthDay(1987, 01, 20), List.of("cassandra"));
        try (Cluster cluster = Cluster.builder()
                .addContactPoint("localhost")
                .build()) {
            Session session = cluster.connect("exemplo_cassandra");
            Mapper<Pessoa> pessoaMapper = new MappingManager(session)
                    .mapper(Pessoa.class);
            // insert
            // pessoaMapper.save(p);

            // deletar
            pessoaMapper.delete("111.111.111-11");
            
            // atualizar
               PreparedStatement cqlUpdate = session.prepare("UPDATE pessoa SET gostos = ? where cpf = ?");
            BoundStatement bound3 = cqlUpdate.bind(List.of("culinaria", "musica", "esportes", "cinema"), "44444444444");
            session.execute(bound3);
            

            // select
            ResultSet results = session.execute ("select * from pessoa");
            Result<Pessoa> pessoas = pessoaMapper.map(results);
            for (Pessoa pessoa : pessoas) {
                System.out.println(pessoa);
            }
        }
    }
}