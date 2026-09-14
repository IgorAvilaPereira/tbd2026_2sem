package negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

public class Cupom {
    private ObjectId id;
    private double valor;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double valorMinimo;
    private List<String> categorias;

    public Cupom() {
        this.categorias = new ArrayList<String>();
        this.categorias.add("Tecnologia");
        this.categorias.add("Eletro");
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDate getDataFim() {
        return dataFim;
    }
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    public double getValorMinimo() {
        return valorMinimo;
    }
    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }
    public List<String> getCategorias() {
        return categorias;
    }
    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }
    @Override
    public String toString() {
        return "Cupom [id=" + id + ", valor=" + valor + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
                + ", valorMinimo=" + valorMinimo + ", categorias=" + categorias + "]";
    }


    
    



}
