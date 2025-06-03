package com.aiyuns.tinkerplay.Other.Frame.RPC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: aiYunS @Date: 2023年8月29日, 0029 下午 4:54:23 @Description: 服务器类,通过Socket监听客户端的请求
 */
public class Server {
  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket(8888);
      System.out.println("Server listening on port 8888...");

      while (true) {
        Socket clientSocket = serverSocket.accept();
        new Thread(new ClientHandler(clientSocket)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
      this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
      try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
          ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
        RemoteService service = new RemoteServiceImpl();
        RemoteCall call = (RemoteCall) in.readObject();
        int result = call.invoke(service);
        out.writeInt(result);
        out.flush();
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
