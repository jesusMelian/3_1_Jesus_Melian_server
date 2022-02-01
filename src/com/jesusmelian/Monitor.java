package com.jesusmelian;

import java.util.ArrayList;
import java.util.Collections;

public class Monitor {
  private ArrayList<String> listMessage = null;
  private int index = 0;

  public Monitor(ArrayList<String> listMessage) {
    Collections.synchronizedList(listMessage);
  }

  //LA primera vez que se conecte tendra que enviarle todos los mensajes
  public synchronized ArrayList<String> getAll() throws InterruptedException{
    /*while (listMessage.size()<1) {
      wait();
    }*/
    //NOTIFICO A LOS METODOS QUE ESPERAN
    notifyAll();
    return listMessage;
  }

  public String putMessage(String message, String user, String time) {
    String rMessage = null;

    //COMPRUEBO EL MESAJE QUE HA ENVIADO
    if(comprobeAndRefactorMessage(message) != null){
      String msg = comprobeAndRefactorMessage(message);
      System.out.println("METO EL MENSAJE: "+"<"+user+">"+" : "+time+"<"+msg+">");
      listMessage.add("<"+user+">"+" : "+time+" <"+msg+">");
      rMessage="<"+user+">"+" : "+time+"<"+listMessage.get(index)+">";
      System.out.println("rMessage: "+rMessage);
      index++;

    }else{
      System.out.println("NO SE HA INTRODUCIDO message: ");
    }
    //NOTIFICO A LOS METODOS QUE ESPERAN
    notifyAll();
    return rMessage;
  }

  public synchronized String getMessage() throws InterruptedException{
    String rMessage = null;
    while (listMessage.size()<1) {
      wait();
    }
    index--;
    rMessage = listMessage.get(index);
    //NOTIFICO A LOS METODOS QUE ESPERAN
    notifyAll();
    return rMessage;
  }


  //Comprobara que se ha añadido message:
  public String comprobeAndRefactorMessage(String message){
    String rMessage = null;
    System.out.println("INICIO DE MSG: "+message.substring(0,8));
    if(message.substring(0,8).equalsIgnoreCase("message:")){
      rMessage=message.substring(8);
      System.out.println("ENVIO EL MENSAJE: "+rMessage);
    }
    return rMessage;
  }
}


