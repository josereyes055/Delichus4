package geekgames.delichus4.customObjects;


public class Paso {

    private int posicion;
    private String descripcion;
    private int tiempo;
    private int tipo;

    public Paso(int posicion, String descripcion, int tiempo, int tipo) {
        this.posicion = posicion;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.tipo = tipo;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getTipo() {
        return tipo;
    }
}
