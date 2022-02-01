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
        String [] listMsg = new String[100];
        //Scanner sc = new Scanner(System.in);

        public Hilo(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            Monitor monitor = new Monitor();

            //Comporbara si es la primera vez que se conecta
            boolean first = true;
            System.out.println("CONEXION RECIBIDA DESDE: " + socket.getInetAddress());
            String msg = null;

            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                //COMO ES LA PRIMERA VEZ DEFINO EL USUARIO, Y OBTENGO TODOS LOS MENSAJES

                if (first) {
                    //obtengo la lista de mensajes
                    listMsg=monitor.getAll();
                    oos.writeObject(listMsg);

                }else{

                    msg=(String) ois.readObject();
                    //SI ES BUY TERMINO
                    if(msg.equalsIgnoreCase("bye")){
                        oos.writeObject("GoodBye");
                    }else{
                        //Añado el mensaje al array
                        monitor.putMessage(msg);
                        //Obtengo el mensaje
                        msg= monitor.getMessage();

                        listMsg[listMsg.length-1]=msg;
                        oos.writeObject(msg);
                    }


                }

                System.out.println("RECIBIDO CORRECTAMENTE DE:  " + socket.getInetAddress() + "Y USUARIO: " + nombreUsuario);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
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