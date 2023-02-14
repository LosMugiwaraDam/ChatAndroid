package com.example.loginchat;

import java.util.ArrayList;

import clases.Mensaje;

public class HiloInUsers extends Thread{    //este hilo se usa para recibir los usuarios conectados al server
    HiloInUsers(){}

    public void run() {
        for (int i = 0; i < SelectorChat.usuarios.size(); i++) { //agragamos por cada usuario un arraylist
            SelectorChat.chatsUsers.add(new ArrayList<Mensaje>());//ARRAYLIST auxiliar de dentro del ARRAYLIST ESCLAVO
            //convierte ese array en un array list de arrays conversaciones de usuarios aka, arraylist(de arraylists)->arraylist(1 por user en el server)->MENSAJES
        }
    }
}
