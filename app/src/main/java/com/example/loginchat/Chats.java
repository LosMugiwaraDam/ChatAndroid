package com.example.loginchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import clases.Archivo;
import clases.Mensaje;

public class Chats extends AppCompatActivity{
    public TextView tvObjetivo;
    public static EditText etMensaje;
    public static ListView lvChat;

    public static byte[] byteAux;

    public static Bitmap bitAux;
    public static String nombreDestino;
    public static Chats.AdaptadorChats adaptador;
    private final static int GET_FILE_BYTES = 1;

    private final static int GET_FILE = 0;
    private final static int TAKE_PHOTO = 2;
    private final static int CAMERA_PERMISION = 3;
    Toast toast;
    private ImageView imgView;
    private MediaPlayer mediaP;
    private VideoView videoView;


    public Thread h1 = new Thread(){
        @Override
        public void run() {
            while (SelectorChat.activo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        actualizarChat(null);
                        SelectorChat.notificacion=false;
                        lvChat.smoothScrollToPosition(lvChat.getHeight());
                    }
                });
                while(SelectorChat.notificacion==false){}
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectorChat.activo=true;

        setContentView(R.layout.activity_chats);
        tvObjetivo = findViewById(R.id.tvObjetivo);
        etMensaje = findViewById(R.id.etMensaje);
        lvChat = findViewById(R.id.lvChat);
        tvObjetivo.setText(nombreDestino);
        adaptador = new Chats.AdaptadorChats(this);
        lvChat.setAdapter(adaptador);

        h1.start();
    }



    class AdaptadorChats extends ArrayAdapter<Mensaje> {

        AppCompatActivity appCompatActivity;
        AdaptadorChats(AppCompatActivity context) {
            super(context, R.layout.activity_layout_chat, SelectorChat.chatsUsers.get(HiloInMsg.buscarUser(nombreDestino)));
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) { //AQUI PONES COMO VER LAS COSAS DE LA BASE DE DATOS
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.activity_layout_chat, null);
            TextView tvNombre = item.findViewById(R.id.tvNombre);
            TextView tvMensaje = item.findViewById(R.id.tvMensaje);
            TextView tvArchivo = item.findViewById(R.id.tvArchivo);
            ImageView imageView = item.findViewById(R.id.imageView);

            Mensaje msgAux = SelectorChat.chatsUsers.get(HiloInMsg.buscarUser(nombreDestino)).get(position);
            tvNombre.setText(msgAux.getOrigen().getNombre()+": ");
            tvMensaje.setText(msgAux.getMensaje());

            if(msgAux.archivo != null){

                InputStream is = new ByteArrayInputStream((msgAux.archivo).data);

                Bitmap bitmap = BitmapFactory.decodeStream(is);

                imageView.setImageBitmap(bitmap);

                tvArchivo.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.GONE);
                tvArchivo.setVisibility(View.GONE);
            }

            return (item);
        }
    }
    public void salir(View view){
        SelectorChat.activo = false;
        SelectorChat.notificacion = true;
        finish();
    }

    public void enviarMensaje(View view){
        try {
            if (etMensaje.getText().toString().length() > 0) {
                Mensaje msgaux;
                if (byteAux == null) {
                    msgaux = new Mensaje(MainActivity.usuario, SelectorChat.usuarios.get(HiloInMsg.buscarUser(nombreDestino)), etMensaje.getText().toString(), null); //origen destino mensaje
                } else {
                    System.out.println(byteAux);
                    String nombre = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
                    Archivo archAux = new Archivo(nombre, byteAux);
                    msgaux = new Mensaje(MainActivity.usuario, SelectorChat.usuarios.get(HiloInMsg.buscarUser(nombreDestino)), etMensaje.getText().toString(), archAux);
                }
                HiloInMsg.agregarMensajesPropios(msgaux);
                HiloOutMsg hiloMsg = new HiloOutMsg(MainActivity.skClientSSL, msgaux);

                etMensaje.setText("");
                hiloMsg.start();
                actualizarChat(null);
                byteAux = null;

            } else {
                Toast notificacion = Toast.makeText(this, "Porfavor, escriba algo en el mensaje antes de mandarlo", Toast.LENGTH_LONG);
                notificacion.show();
            }
        }catch (Exception e){
            System.out.println("ERROR AL GENERAR EL MENSAJE: \n"+e);
        }
    }

    public void adjuntarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int res, int resultCode, Intent data) {
        super.onActivityResult(res, resultCode, data);
        if (res == GET_FILE_BYTES && data != null) {
            Uri uri = data.getData();

            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(uri);
                byte[] inputData = this.getBytes(iStream);

                Archivo file = new Archivo("prueba.jpg", inputData);
                InputStream is = new ByteArrayInputStream(file.data);
                byteAux = inputData;
                bitAux = BitmapFactory.decodeStream(is);
            } catch (Exception e) {

            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void actualizarChat(View view){
        lvChat.setAdapter(adaptador);
        lvChat.setSelection(SelectorChat.chatsUsers.get(HiloInMsg.buscarUser(nombreDestino)).size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getDelegate().onDestroy();
    }
}