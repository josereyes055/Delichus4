package geekgames.delichus4.customObjects;


public class Ficha {

    public int id;
    public String nombre;
    public String imagen;
    public int idAutor;
    public String autor;
    public String foto;
    public float puntuacion;
    public String descripcion;
    public int    pasos;
    public int color = 0;


    public Ficha(int id, String nombre, String imagen, int idAutor, String autor, String foto, float puntuacion, String descripcion, int pasos) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.idAutor = idAutor;
        this.autor = autor;
        this.foto = foto;
        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.pasos = pasos;
    }
}
