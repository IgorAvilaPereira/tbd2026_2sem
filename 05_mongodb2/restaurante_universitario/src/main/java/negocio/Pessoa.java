package negocio;

import org.bson.types.ObjectId;

public class Pessoa {

      private ObjectId id;
      private String nome;

    

      public Pessoa() {
    }
      public Pessoa(String nome) {
        this.nome = nome;
    }
      public ObjectId getId() {
          return id;
      }
      public void setId(ObjectId id) {
          this.id = id;
      }
      public String getNome() {
          return nome;
      }
      public void setNome(String nome) {
          this.nome = nome;
      }

      
}
