package com.example.loginchat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.net.ssl.SSLSocket;

import clases.Archivo;
import clases.Mensaje;
import clases.Pidove;

public class HiloInMsg  extends Thread {
    SSLSocket socketSSL;
    public static String ruta = "/storage/self/primary/Download";

    HiloInMsg(SSLSocket s1) {
        this.socketSSL = s1;
    }

    public void run() {    //---------------este hilo lee y redirecciona los mensajes al usuario destino
        System.out.println("--------------------------------------------ESTA EN: " + ruta);
        while (true) {
            try {
                ObjectInputStream flujoObjEntrada = new ObjectInputStream(socketSSL.getInputStream());
                Object o = flujoObjEntrada.readObject();
                if (o instanceof Mensaje) {
                    //----------------------GUARDA EL MENSAJE AUXILIAR EN UN ARRAYLIST DEPENDIENDO DE QUIEN LO ENVIE------------
                    agregarMensajes((Mensaje) o);

                    if (((Mensaje) o).getArchivo() != null) {

                        System.out.println("-------------------------------------------------------------archivo archivesco");
                        guardarArchivo(((Mensaje) o).getArchivo());
                    }

                    SelectorChat.notificacion = true;
                } else if (o instanceof Pidove){
                    if (((Pidove) o).action == 1) {
                        System.exit(0);
                    }
                    if ( ((Pidove) o).action == 2) {
                        SelectorChat.clima = (((Pidove) o).data);
                    }
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public void agregarMensajes(Mensaje msg) {
        SelectorChat.chatsUsers.get(buscarUser(msg.getOrigen().getNombre())).add(msg);
    }

    public static void agregarMensajesPropios(Mensaje msg) {
        SelectorChat.chatsUsers.get(buscarUser(msg.getDestino().getNombre())).add(msg);
    }

    public static int buscarUser(String nombre) {
        int i;
        for (i = 0; i < SelectorChat.usuarios.size(); i++) { //agragamos por cada usuario un arraylist
            if (nombre.equals(SelectorChat.usuarios.get(i).getNombre())) {
                return i;  //en caso de encontrarlo, retornará la posicion exacta en el array
            }
        }
        return -1;  //en caso de no encontrarlo, no retorna nada
    }

    public static boolean guardarArchivo(Archivo a1) {
        try (FileOutputStream fos = new FileOutputStream(ruta + "/" + a1.getNombre())) {
            fos.write(a1.data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
