package com.example.loginchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import clases.Mensaje;
import clases.Pidove;
import clases.Usuario;


public class SelectorChat extends AppCompatActivity{
    public static String clima;
    public TextView tvUsername;
    //-------arraylist por conversacion-------->arraylist para las conversaciones
    public static ArrayList<Usuario> usuarios = new ArrayList<>();   //ARRAYLIST MAESTRO, dictamina el orden de los chats
    public static ArrayList<ArrayList<Mensaje>> chatsUsers = new ArrayList<>();     //ARRAYLIST ESCLAVO, guarda los mensajes que entran en el orden que le diga el maestro
    public static ListView lv1;

    public static boolean activo = false;
    public static boolean notificacion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_chat);
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText("Hola: "+ MainActivity.nombreUser);

        HiloInMsg hiloMsg = new HiloInMsg(MainActivity.skClientSSL);
        hiloMsg.start();
        HiloInUsers hiloUsers = new HiloInUsers();
        hiloUsers.start();
        try {
            hiloUsers.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        AdaptadorUsuarios adaptador = new AdaptadorUsuarios(this);

        lv1 = findViewById(R.id.list1);
        lv1.setAdapter(adaptador);

        Intent i = new Intent(this, Chats.class );
        System.out.println(usuarios);
        System.out.println(chatsUsers);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String nombreAux = usuarios.get(position).getNombre();
                Chats.nombreDestino = nombreAux;
                startActivity(i);
            }
        });
        System.out.println(usuarios);
    }

    class AdaptadorUsuarios extends ArrayAdapter<Usuario> {
        AppCompatActivity appCompatActivity;
        AdaptadorUsuarios(AppCompatActivity context) {
            super(context, R.layout.activity_lista_usuarios, usuarios);
            appCompatActivity = context;
        }
        public View getView(int position, View convertView, ViewGroup parent) { //AQUI PONES COMO VER LAS COSAS DE LA BASE DE DATOS
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.activity_lista_usuarios, null);
            TextView textView1 = item.findViewById(R.id.textView2);
            String nombreAux =usuarios.get(position).getNombre();
            textView1.setText(nombreAux);
            return(item);
        }
    }

    public void clima(View view){
        Toast notificacion = Toast.makeText(getApplicationContext(),"Solicitando clima",Toast.LENGTH_SHORT);
        notificacion.show();
        HiloInClima hiloClima = new HiloInClima(MainActivity.skClientSSL);
        try {
            hiloClima.start();
            hiloClima.join();
            while(clima == null){}

            String climaArray[] = clima.split(",");
            int temperatura = Integer.parseInt(climaArray[0]);
            String estado = climaArray[1];
            int viento = Integer.parseInt(climaArray[2]);
            String texto;
            texto = "El clima actual es "+estado+",";
            if(viento>30){
                texto = texto+" dado que el viento es "+viento+"km/h recomendamos no poner los toldos";
            }else{
                texto = texto+" dado que el viento es "+viento+"km/h se pueden poner los toldos";
            }
            if (temperatura > 20){
                texto = texto + " y dado que hace calor("+temperatura +"ºC), lo recomendamos";
            }else{
                texto = texto + ", pero hace frio("+temperatura +"ºC), asi que nada";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(texto)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            clima = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void salir(View view){
        MainActivity.nombreUser=null;   //borramos el nombre del usuario anterior
        finish();
    }
}