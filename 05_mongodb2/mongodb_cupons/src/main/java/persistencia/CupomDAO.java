package persistencia;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;

import static com.mongodb.client.model.Filters.eq;
import negocio.Cupom;

public class CupomDAO {
    private MongoCollection<Cupom> colecao;

    public CupomDAO() {
        this.colecao = new ConexaoMongoDB().getConexao().getCollection("cupons", Cupom.class);
    }

    public boolean adicionar(Cupom cupom) {
        InsertOneResult insertOneResult = colecao.insertOne(cupom);
        return insertOneResult.wasAcknowledged();
    }

    public boolean deletar(ObjectId id) {
        return colecao.deleteOne(eq("_id", id)).getDeletedCount() != 0;
    }

    public MongoCursor<Cupom> listar() {
        return colecao.find().cursor();
    }
    
    public Cupom obter(ObjectId id) {
        return colecao.find(eq("_id", id)).first();
    }

    public boolean atualizar(Cupom c) {
        return colecao.replaceOne(eq("_id", c.getId()), c).getMatchedCount() != 0;
    }
}
