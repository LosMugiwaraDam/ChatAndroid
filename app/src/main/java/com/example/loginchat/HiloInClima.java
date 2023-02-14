package com.example.loginchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import clases.Mensaje;
import clases.Pidove;

public class HiloInClima extends Thread{    //este hilo se usa para recibir los usuarios conectados al server
    SSLSocket socketSSL;
    HiloInClima(SSLSocket s1){ this.socketSSL = s1; }

    public void run() {
        System.out.println("Voy a enviar PIDOVE");
        try {
            ObjectOutputStream flujoObjSalida = new ObjectOutputStream(socketSSL.getOutputStream());
            Pidove p1 = new Pidove(2,MainActivity.usuario,null);
            flujoObjSalida.writeObject(p1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("ERROR POR NULO");
        }
    }
}
