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
        String nombreUsuario = null;
        ArrayList<String> listMsg = new ArrayList<>();
        boolean seguir = true;
        //Scanner sc = new Scanner(System.in);

        public Hilo(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            Monitor monitor = new Monitor(listMsg);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            System.out.println("CONEXION RECIBIDA DESDE: " + socket.getInetAddress());
            System.out.println("Hora actual: " + dateFormat.format(date));


                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos = new ObjectOutputStream(socket.getOutputStream());

                    //INTRODUZCO EL USUARIO
                    String usuario = (String) ois.readObject();
                    System.out.println("LEO EL USUARIO: "+ usuario);

                    oos.writeObject(monitor.getAll());
                    String msg = "";
                    while(!msg.equals("bye")) {
                        msg = (String) ois.readObject();

                        //SI ES BUY TERMINO
                        if (msg.equalsIgnoreCase("bye")) {
                            oos.writeObject("GoodBye");
                        } else {
                            System.out.println("MI MENSAJE: " + msg);
                            //Añado el mensaje al array
                            monitor.putMessage(msg, usuario, dateFormat.format(date).toString());
                            //Obtengo el mensaje
                            //msg = monitor.getMessage();

                            //listMsg.add(msg);
                            listMsg.add("USER: "+usuario+ " MENSAJE: "+msg);
                            oos.writeObject(msg);

                            for(String msgs: listMsg){
                                System.out.println(msgs);
                            }
                        }
                    }
                    //System.out.println("RECIBIDO CORRECTAMENTE DE:  " + socket.getInetAddress() + "Y USUARIO: " + nombreUsuario);
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
        }
}