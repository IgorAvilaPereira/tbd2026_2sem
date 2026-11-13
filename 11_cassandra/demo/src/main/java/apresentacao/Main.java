package apresentacao;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import negocio.Product;

public class Main {
    public static void main(String[] args) {

        Cluster cluster = null;
        try {
            cluster = Cluster.builder().addContactPoint("127.0.0.1")
                    .build();
            Session session = cluster.connect("inventory");

            Mapper<Product> productMapper = new MappingManager(session)
                    .mapper(Product.class);

            Product product = new Product();
            product.setDescription("oi");
            product.setId(UUID.randomUUID());
            productMapper.save(product);


            // productMapper.delete(UUID.fromString("b3e6b14d-6cc1-421d-909d-046fc3399d38"));

        } finally {
            if (cluster != null)
                cluster.close();
        }
    }
}