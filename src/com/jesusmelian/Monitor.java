package com.jesusmelian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Monitor {
  private List<String> listMessage = Collections.synchronizedList(new ArrayList<>());

  public Monitor() {

  }
  //LA primera vez que se conecte tendra que enviarle todos los mensajes
  public synchronized String getAll() throws InterruptedException{
    //RESULTA QUE EL CLIENTE NO HE ENCONTRADO LA FORMA DE QUE RECIBA UN ARRAYLIST, CN LO CUAL LE ENVIO LOS MENSAJE EN FORMATO STRING
    String msgSend = "";
    for (int i = 0; i < listMessage.size(); i++) {
      msgSend += listMessage.get(i) + "\n";
    }
    return msgSend;
  }

  public synchronized void putMessage(String message) {
    listMessage.add(message);
  }
}


