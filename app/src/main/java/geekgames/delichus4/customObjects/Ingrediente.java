package geekgames.delichus4.customObjects;


public class Ingrediente {

    private String nombre;
    private Double cantidad;
    private String medida;

    public Ingrediente(String nombre, Double cantidad, String medida) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.medida = medida;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public String getMedida() {
        return medida;
    }
}
