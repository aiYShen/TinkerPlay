package com.aiyuns.tinkerplay.Other.Frame.RPC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author: aiYunS @Date: 2023年8月29日, 0029 下午 4:55:22 @Description: 客户端类: 负责创建RPC调用并将其发送到服务器
 */
public class Client {
  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", 8888);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
      RemoteCall call = new RemoteCall("add", new Object[] {3, 5});
      out.writeObject(call);
      int result = in.readInt();
      System.out.println("Result: " + result);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
