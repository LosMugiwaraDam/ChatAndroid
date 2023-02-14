package clases;

import java.io.Serializable;

public class Usuario implements Serializable{

    private static final long serialVersionUID = 1L;
    public double nEmpl;
    public String nombre;
    public String apellido1;
    public String apellido2;

    public Usuario(double nEmpl,String nombre, String apellido1, String apellido2) {
        super();
        this.nEmpl = nEmpl;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public Usuario(double nEmpl,String nombre, String apellido1) {
        super();
        this.nEmpl = nEmpl;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public String getNombre(){
        return(nombre);
    }

    public String getApellido1(){
        return(apellido1);
    }

    public String getApellido2(){
        return(apellido2);
    }

    public void setNombre(String s1){
        s1 = nombre;
    }

    public void setApellido1(String s1){
        this.apellido1 = apellido1;
    }

    public void setApellido2(String s1){
        this.apellido2 = apellido2;
    }

    public String toString(){
        return ("usuario: " + nombre);
    }
    /*
    private String IP, nickname;
    public Usuario(String IP, String nickname){
        this.IP=IP;
        this.nickname=nickname;
    }
    public Usuario(){
    }
    public String getIP() {
        return IP;
    }
    public void setIP(String iP) {
        IP = iP;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String toString() {
        return("Nombre: "+nickname+" IP: "+IP);
    }*/
}
