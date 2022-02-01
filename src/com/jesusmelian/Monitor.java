package com.jesusmelian;

public class Monitor {
  public String[] listMessage= new String[100];
  private int index = 0;

  //LA primera vez que se conecte tendra que enviarle todos los mensajes
  public synchronized String[] getAll() throws InterruptedException{
    while (listMessage.length<1) {
      wait();
    }
    //NOTIFICO A LOS METODOS QUE ESPERAN
    notifyAll();
    return listMessage;
  }

  public String putMessage(String message) {
    String rMessage = null;

    //COMPRUEBO EL MESAJE QUE HA ENVIADO
    if(comprobeAndRefactorMessage(message) != null){
      listMessage[index]=(comprobeAndRefactorMessage(message));
      rMessage=listMessage[index];
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
    while (listMessage.length<1) {
      wait();
    }
    index--;
    rMessage = listMessage[index];
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
    }
    return rMessage;
  }
}


