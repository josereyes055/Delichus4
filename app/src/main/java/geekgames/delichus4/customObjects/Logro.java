package geekgames.delichus4.customObjects;


public class Logro {

    private int idLogro;
    private String nombre;
    private String titulo;
    private String descripcion;
    private boolean completed;


    public Logro(int id, String nombre, String titulo, String descripcion, boolean completed) {
        this.idLogro = id;
        this.nombre = nombre;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completed = completed;
    }


    public String getNombre() {
        return nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getId() {
        return idLogro;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
