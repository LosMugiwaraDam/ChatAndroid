package clases;

import java.io.Serializable;

public class Archivo implements Serializable {
    private static final long serialVersionUID = 1L;
    public String nombre;
    public String ext;
    public byte[] data;

    public Archivo(String nombreC, byte[] data) {
        super();
        String[] aux = nombreC.split("\\.");
        this.nombre = aux[0];
        this.ext = aux[aux.length - 1];
        this.data = data;
    }

    public String getNombre() {
        return nombre + "." + ext;
    }
}

