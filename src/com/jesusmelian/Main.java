package com.jesusmelian;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

        public Hilo(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            boolean first = true;
            System.out.println("CONEXION RECIBIDA DESDE: " + socket.getInetAddress());
            String nombreUsuario = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                if (first) {
                    nombreUsuario = (String) ois.readObject();
                }

                String saludo = "Hola friki [" + nombreUsuario + "] TIME: " + System.currentTimeMillis();
                oos.writeObject(saludo);
                System.out.println("SALUDO ENVIADO CON EXITO A: " + socket.getInetAddress() + "Y USUARIO: " + nombreUsuario);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null) ois.close();
                    if (oos != null) oos.close();
                    if (socket != null) socket.close();
                    System.out.println("NIÑOOOO SE ACABO LO QUE SE DABA");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}