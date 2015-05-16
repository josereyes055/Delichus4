package geekgames.delichus4.customObjects;

public class RecetaExtended {

    private int id;
    private String nombre;
    private String larga;
    private String imagen;
    private String autor;
    private String foto;
    private String puntuacion;
    private String descripcion;
    private int pasos;


    public RecetaExtended(int id, String nombre, String larga, String imagen, String autor, String foto, String puntuacion, String descripcion, int pasos){
        this.id = id;
        this.nombre = nombre;
        this.larga = larga;
        this.imagen = imagen;
        this.autor = autor;
        this.foto = foto;
        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.pasos = pasos;
    }

    public String getImagen() { return imagen; }

    public String getNombre() { return nombre; }

    public String getAutor() { return autor; }

    public String getFoto() { return foto;  }

    public String getPuntuacion() { return puntuacion; }

    public String getDescripcion() { return descripcion; }

    public int getPasos() {
        return pasos;
    }

    public int getId() {
        return id;
    }

    public String getLarga() {
        return larga;
    }
}
