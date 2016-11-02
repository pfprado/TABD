package pruebas.appprueba.Entidades;

/**
 * Created by Paulo on 01/11/2016.
 */
public class Interes {

    private int id;
    private String intereses;

    public Interes(int id, String interes){
        this.id = id;
        this.intereses = interes;
    }

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
