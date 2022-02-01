package com.jesusmelian;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

            //Comporbara si es la primera vez que se conecta
            boolean first = true;
            System.out.println("CONEXION RECIBIDA DESDE: " + socket.getInetAddress());
            String msg = null;

            while (!msg.equalsIgnoreCase("bye")) {
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    //COMO ES LA PRIMERA VEZ DEFINO EL USUARIO, Y OBTENGO TODOS LOS MENSAJES
                    if (first) {
                        System.out.println("ENTRO POR FIRST");
                        //obtengo la lista de mensajes
                        listMsg = monitor.getAll();
                        oos.writeObject(listMsg);
                        if (listMsg != null) {
                            for (String msgs : listMsg) {
                                System.out.println("LISTAS DE MSG: " + msgs);
                            }
                        }

                        //first=false;

                    } else {

                        msg = (String) ois.readObject();
                        //SI ES BUY TERMINO
                        if (msg.equalsIgnoreCase("bye")) {
                            oos.writeObject("GoodBye");
                            seguir = false;
                        } else {
                            System.out.println("MI MENSAJE: " + msg);
                            //Añado el mensaje al array
                            monitor.putMessage(msg);
                            //Obtengo el mensaje
                            msg = monitor.getMessage();

                            listMsg.add(msg);
                            oos.writeObject(msg);
                        }


                    }

                    //System.out.println("RECIBIDO CORRECTAMENTE DE:  " + socket.getInetAddress() + "Y USUARIO: " + nombreUsuario);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}