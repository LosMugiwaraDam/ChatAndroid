package com.example.loginchat;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

import clases.Archivo;
import clases.Mensaje;
import clases.Usuario;


public class HiloOutMsg extends Thread{
    SSLSocket socketSSL;
    Mensaje m;
    HiloOutMsg(SSLSocket socketSSL, Mensaje m){
        this.socketSSL=socketSSL;
        this.m = m;
    }
    public void run(){
        Usuario usuEnv = m.getOrigen();
        Usuario usuRec = m.getDestino();
        String texto = m.getMensaje();
        Archivo archivo = m.getArchivo();
        Mensaje msg01 = new Mensaje(usuEnv,usuRec,texto,archivo);
        System.out.println("Voy a enviar "+msg01);
        try {
            ObjectOutputStream flujoObjSalida = new ObjectOutputStream(socketSSL.getOutputStream());
            flujoObjSalida.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("ERROR POR NULO");
        }
    }
}
