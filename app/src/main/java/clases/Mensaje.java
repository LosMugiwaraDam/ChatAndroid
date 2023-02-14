package clases;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensaje implements Serializable{
    private static final long serialVersionUID = 1L;
    public Usuario usuEnv;
    public Usuario usuRec;
    public String texto;
    public Archivo archivo;

    public Mensaje(Usuario usuEnv, Usuario usuRec, String texto, Archivo archivo) {
        super();
        this.usuEnv = usuEnv;
        this.usuRec = usuRec;
        this.texto = texto;
        this.archivo = archivo;
    }
    public Mensaje(){}

    public Usuario getOrigen() {
        return usuEnv;
    }

    public void setOrigen(Usuario origen) {
        this.usuEnv = origen;
    }

    public Usuario getDestino() {
        return usuRec;
    }

    public void setDestino(Usuario destino) {
        this.usuRec = destino;
    }

    public String getMensaje() {
        return texto;
    }

    public void setMensaje(String mensaje) {
        this.texto = mensaje;
    }

    public Archivo getArchivo(){
        return archivo;
    }

    public void setArchivo(Archivo a1){
        this.archivo=archivo;
    }


    /*
    private String origen, destino, mensaje;
    public Mensaje(String origen, String destino, String mensaje){
        this.origen=origen;
        this.destino=destino;
        this.mensaje=mensaje;
    }

    public Mensaje() {}

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String toString() {
        return ("Mensaje para "+destino+":\n"+mensaje+"\nEscrito por "+origen);
    }
    */
}
