package pruebas.appprueba.Entidades;

/**
 * Created by Paulo on 02/11/2016.
 */
public class Interes {

    private int id;
    private String intereses;

    // Nuevamente creamos el constructor de la clase
    public Interes(int id, String interes){
        this.id  = id;
        this.intereses = interes;
    }

    // Creamos los métodos GET y SET

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }
}
