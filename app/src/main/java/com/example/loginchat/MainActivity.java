package com.example.loginchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import clases.Usuario;

public class MainActivity extends AppCompatActivity implements Runnable{
    //public static String HOST = "192.168.1.11"; //Casa
    public static String HOST = "192.168.0.17"; //Cuarto
    //public static String HOST = "192.168.170.93"; //Red wifi privada

    //public static String HOST = "192.168.1.101"; //Clase (wifilanberri202-)

    //public static String HOST = "10.22.2.186";  //Clase (Somorrostro 202 1)
    //public static String HOST = "25.56.25.5"; //Laptop jie (hamachi)
    //public static String HOST = "192.168.1.115"; //Laptop jie
    //public static String HOST = "25.64.200.222"; //Pc jie (hamachi)

    public static Usuario usuario = null;
    private static String nombrecertificado = "usuarioalmacenssljie.bks";

    private EditText etNombreUser, etContra;
    public static String ipUser;
    public static String nombreUser;

    public static SSLSocket skClientSSL;

    public static int PUERTO = 5233;

    Toast notificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificacion = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);

        crearCertificado();
        /**/
        etNombreUser = findViewById(R.id.etNombreUser);
        etContra = findViewById(R.id.etNombreUser2);
    }

    public void login(View view){
        Thread h1 = new Thread(this);
        try{
            h1.start();
            h1.join();
        }catch(Exception e){
            System.out.println("ERROR AL CREAR EL CERTIFICADO");
        }

        Thread th1 =  new Thread(){
            @Override
            public void run(){
                try {
                    if (etNombreUser.getText().toString().length() > 0 && etContra.getText().toString().length() > 0) {
                        DataOutputStream dosServer = new DataOutputStream(skClientSSL.getOutputStream());
                        dosServer.writeLong(Long.parseLong(etNombreUser.getText().toString()));
                        dosServer.writeUTF(etContra.getText().toString());
                        ObjectInputStream oisServer = new ObjectInputStream(skClientSSL.getInputStream());
                        //el server le puede mandar un nulo en caso de no encontrar nada

                        DataInputStream disServer = new DataInputStream(skClientSSL.getInputStream());
                        usuario = (Usuario) oisServer.readObject();
                        if (usuario != null) {
                            nombreUser = usuario.getNombre(); //GUARDAMOS EL NOMBRE CORRECTO DEL USER COMO VARIABLE DE CLASE
                            Intent i = new Intent(getApplicationContext(), SelectorChat.class);
                            SelectorChat.usuarios.clear();
                            SelectorChat.chatsUsers.clear();

                            //me logeo y meto todos los usuarios al array de usuarios
                            int nUsu = disServer.readInt();
                            for (int y = 0; y < nUsu-1; y++) {
                                SelectorChat.usuarios.add((Usuario) oisServer.readObject());
                            }
                            //ejecuto el hilo de enviar y recibir mensajes
                            new HiloInMsg (skClientSSL);

                            startActivity(i);
                        } else {
                            notificacion.setText("Credenciales incorrectas");
                            notificacion.show();
                        }
                    } else {
                        notificacion.setText("Credenciales incompletas");
                        notificacion.show();
                    }
                }catch (Exception e){
                    notificacion.setText("ERROR A LA HORA DE LOGEAR");
                    notificacion.show();
                    System.out.println(e);
                }
            }
        };
        th1.start();
    }

    public void salir(View view){
        finish();
    }

    public void crearCertificado(){
        AssetManager am = getApplicationContext().getAssets();
        try {
            InputStream is = am.open(nombrecertificado);
            File cert = new File(getApplicationContext().getFilesDir() + "/"+nombrecertificado);
            if (!cert.exists()) cert.createNewFile();
            try (FileOutputStream outputStream = new FileOutputStream(cert, false)) {
                int read;
                byte[] bytes = new byte[8*1024];
                while ((read = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String rutaCer(){
        return getApplicationContext().getFilesDir() + "/" + nombrecertificado;
    }

    @Override
    public void run() {
        System.setProperty("javax.net.ssl.trustStore", rutaCer());
        System.setProperty("javax.net.ssl.trustStorePassword", "890123");
        SSLSocketFactory sfact = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            MainActivity.skClientSSL = (SSLSocket) sfact.createSocket(HOST, PUERTO);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}