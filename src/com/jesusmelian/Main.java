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
        private int index = 0;
        String nombreUsuario = null;
        ArrayList<String> listMsg = new ArrayList<>();
        boolean seguir = true;
        //Scanner sc = new Scanner(System.in);

        public Hilo(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            ////Monitor monitor = new Monitor(listMsg);
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
                    String usuario = (String) ois.readObject();
                    System.out.println("LEO EL USUARIO: "+ usuario);

                    //como es la primera vez, envio el arrayList con los mensajes
                    ////oos.writeObject(monitor.getAll());
                    oos.writeObject(listMsg);
                    String msg = "";

                    //MIENTRAS NO SE ESCRIBA BYE
                    while(!msg.equals("bye")) {
                        //Leo el mensaje
                        msg = (String) ois.readObject();

                        //SI ES BYE TERMINO
                        if (msg.equalsIgnoreCase("bye")) {
                            oos.writeObject("GoodBye");
                        } else {
                            System.out.println("MI MENSAJE: " + msg);
                            //Añado el mensaje al array
                            ////monitor.putMessage(msg, usuario, dateFormat.format(date).toString());

                            //Compruebo que empieza por message:
                            String miMsg = this.testAndRefactorMessage(msg);
                            String addArrayList = "<"+usuario+">"+" : "+dateFormat.format(date).toString()+ "<"+miMsg+">";
                            System.out.println("PARA AÑADIR: "+addArrayList);

                            //añado al arrayList el mensaje formateado
                            listMsg.add(addArrayList);

                            //Obtengo el mensaje
                            ////msg = monitor.getMessage();

                            ////oos.writeObject(msg);
                            //Le envio al cliente el mensaje formateado
                            oos.writeObject(addArrayList);

                            for(String msgs: listMsg){
                                System.out.println(msgs);
                            }
                        }
                    }
                    //System.out.println("RECIBIDO CORRECTAMENTE DE:  " + socket.getInetAddress() + "Y USUARIO: " + nombreUsuario);
                } catch (IOException | ClassNotFoundException e) {
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

        /*public String putMessage(String message, String user, String time) {
            String rMessage = null;

            //COMPRUEBO EL MESAJE QUE HA ENVIADO
            if(testAndRefactorMessage(message) != null){
                String msg = testAndRefactorMessage(message);
                System.out.println("METO EL MENSAJE: "+"<"+user+">"+" : "+time+"<"+msg+">");
                String msgSave = "<"+user+">"+" : "+time+" <"+msg+">";
                listMsg.add(msgSave);
                rMessage="<"+user+">"+" : "+time+"<"+listMsg.get(index)+">";
                System.out.println("rMessage: "+rMessage);
                index++;

            }else{
                System.out.println("NO SE HA INTRODUCIDO message: ");
            }
            //NOTIFICO A LOS METODOS QUE ESPERAN
            notifyAll();
            return rMessage;
        }*/
    }


}