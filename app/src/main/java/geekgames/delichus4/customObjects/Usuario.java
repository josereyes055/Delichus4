package geekgames.delichus4.customObjects;

import org.json.JSONArray;

public class Usuario {

    public int id;
    public String foto;
    public String nombre;
    public String titulo;
    public int score;
    public int lvl;
    public JSONArray logros;
    public JSONArray favoritos;
    public JSONArray completadas;
    public int porcentaje = 0;


    public Usuario(int id, String foto, String nombre, String titulo, int score, int lvl) {
        this.id = id;
        this.foto = foto;
        this.nombre = nombre;
        this.titulo = titulo;
        this.score = score;
        this.lvl = lvl;
        calcularNivel();
    }

    public void calcularNivel(){
        double nivelPromedio = 1 + (Math.sqrt(score/10));
        lvl = (int) Math.floor( nivelPromedio );
        porcentaje = (int) Math.floor( ( nivelPromedio - lvl)*100 );
    }
    public void sumarPuntaje(int puntaje){
        score += puntaje;
        calcularNivel();
    }
}