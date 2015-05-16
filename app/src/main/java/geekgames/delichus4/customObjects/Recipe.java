package geekgames.delichus4.customObjects;

import org.json.JSONArray;

import java.util.List;

public class Recipe {

    public int id;
    public String nombre;
    public String larga;
    public String imagen;
    public int idAutor;
    public String autor;
    public String foto;
    public String puntuacion;
    public String descripcion;
    public int pasos;
    public int cantidad;
    public int dificultad;
    public int tiempo;
    public JSONArray steps;
    public List<Ingrediente> ingredientes;


    public Recipe(int id, String nombre, String larga, String imagen, int idAutor, String autor,
                  String foto, String puntuacion, String descripcion, int pasos, int cantidad,
                  int dificultad, int tiempo, JSONArray steps) {
        this.id = id;
        this.nombre = nombre;
        this.larga = larga;
        this.imagen = imagen;
        this.idAutor = idAutor;
        this.autor = autor;
        this.foto = foto;
        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.pasos = pasos;
        this.steps = steps;
        this.dificultad = dificultad;
        this.tiempo = tiempo/60;
        this.cantidad = cantidad;
    }

}
