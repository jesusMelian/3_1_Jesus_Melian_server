package com.jesusmelian;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static Monitor monitor = new Monitor();

    public static void main(String[] args) throws IOException {
        // write your code here
        final int PORT = 8080;
        Socket socket = null;
        ServerSocket ss = new ServerSocket(PORT);

        while (true) {
            socket = ss.accept();
            Hilo worker = new Hilo(socket);
            worker.start();

        }
    }

    static class Hilo extends Thread {
        private Socket socket = null;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;
        String usuario = null;
        String msg = "";
        private final String BYEMSG = "bye";
        private final String GOODBYEMSG = "Good Bye";
        private final String ERRORMSG = "Algo Fallo!";

        public Hilo(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            //OBTENGO LA HORA QUE COLOCARE EN EL ENVIO DEL MENSAJE
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            System.out.println("CONEXION RECIBIDA DESDE: " + socket.getInetAddress());
            System.out.println("Hora actual: " + dateFormat.format(date));

                //LA PRIMERA VEZ QUE ARRANCO HAGO ESTO:
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos = new ObjectOutputStream(socket.getOutputStream());

                    //INTRODUZCO EL USUARIO
                    //Leo el usuario
                    usuario = (String) ois.readObject();
                    System.out.println("LEO EL USUARIO: "+ usuario);

                    //como es la primera vez, envio el String con los mensajes
                    oos.writeObject(monitor.getAll());

                    while(true) {
                        //Leo el mensaje
                        msg = (String) ois.readObject();

                        //SI ES BYE TERMINO
                        if (msg.equals(BYEMSG)) {
                            oos.writeObject(GOODBYEMSG);
                            System.out.println("CERRAMOS CONEXIÓN");
                        } else {
                            System.out.println("MI MENSAJE: " + msg);
                            //Añado el mensaje al array
                            //Compruebo que empieza por message:
                            String miMsg = this.testAndRefactorMessage(msg);
                            //SI EL MENSAJE NO ES CORRECTO DEVUELVO NULL
                            if(miMsg != null){
                                String addArrayList = "<"+usuario+">"+" : "+dateFormat.format(date).toString()+ "<"+miMsg+">";
                                System.out.println("PARA AÑADIR: "+addArrayList);

                                //AÑADO AL ARRAYLIST EL MENSAJE
                                monitor.putMessage(addArrayList);
                                oos.writeObject(monitor.getAll());
                            }else{
                                oos.writeObject(ERRORMSG);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (oos != null) oos.close();
                        if (ois != null) ois.close();
                        if (socket != null) socket.close();
                        System.out.println("nnñoo se acabo lo que se daba");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        public String testAndRefactorMessage(String message){
            String rMessage = null;
            System.out.println("INICIO DE MSG: "+message.substring(0,8));
            if(message.substring(0,8).equalsIgnoreCase("message:")){
                rMessage=message.substring(8);
                System.out.println("ENVIO EL MENSAJE: "+rMessage);
            }
            return rMessage;
        }
    }


}