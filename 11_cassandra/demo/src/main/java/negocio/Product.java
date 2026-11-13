package negocio;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.*;

@Table (keyspace="inventory", name="product")
public class Product {

  @PartitionKey
  private UUID id;
  private String description;

  public Product() {
  }

  public Product(UUID id, String description) {
    this.id = id;
    this.description = description;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}