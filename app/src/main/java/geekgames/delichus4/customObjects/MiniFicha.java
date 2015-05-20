package geekgames.delichus4.customObjects;

/**
 * Created by Jose on 16/05/2015.
 */
public class MiniFicha {

   public int id;
   public String nombre;
   public String imagen;
   public String autor;
   public String descripcion;
   public int pasos;
   public float puntuacion;


   public MiniFicha(int id, String nombre, String imagen,String autor,float puntuacion, String descripcion, int pasos) {
       this.id = id;
       this.nombre = nombre;
       this.imagen = imagen;
       this.autor = autor;
       this.puntuacion = puntuacion;
       this.descripcion = descripcion;
       this.pasos = pasos;
   }
}



