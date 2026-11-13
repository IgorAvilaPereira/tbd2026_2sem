package persistencia;

import java.util.ArrayList;
import java.util.UUID;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import negocio.Product;

public class ProductDAO {

    private Session session;
    private Mapper<Product> productMapper;

    public ProductDAO(Session session) {
        this.session = session;
        this.productMapper = new MappingManager(session)
                    .mapper(Product.class);
    }

    public Product obter(UUID id){
        String query = "SELECT * FROM product where id = ?";
        PreparedStatement ps = session.prepare(query);
        ResultSet rs = session.execute(ps.bind(id));
        Product product = new Product();
        for (Row row : rs) {
            product.setId(row.getUUID("id"));
            product.setDescription(row.getString("description"));
            
        }
        return product;
    }

    public ArrayList<Product> listar() {
        ResultSet rs = session.execute("SELECT * FROM product");
        ArrayList<Product> vetProducts = new ArrayList<>();
        for (Row row : rs) {
            Product product = new Product();
            product.setId(row.getUUID("id"));
            product.setDescription(row.getString("description"));
            vetProducts.add(product);
        }
        return vetProducts;
    }

    public void inserir(Product product) {
        String query = "INSERT INTO product (id, description) " +
                "VALUES (?, ?)";

        PreparedStatement ps = session.prepare(query);
        session.execute(ps.bind(product.getId(), product.getDescription()));
    }

    public void deletar(Product product) {
        this.productMapper.delete(product);
        // String query = "DELETE FROM product where id = ?";
        // PreparedStatement ps = session.prepare(query);
        // try {
        //     session.execute(ps.bind(id));
        //     // session.close();
            
        //     return true;
        // } catch (Exception e) {
        //     return false;
        // }

    }
}