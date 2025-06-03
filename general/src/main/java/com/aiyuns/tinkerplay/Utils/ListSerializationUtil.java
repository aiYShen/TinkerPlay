package com.aiyuns.tinkerplay.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年7月26日, 0026 下午 4:39:00 @Description: 序列化到文件和文件反序列化到List
 */
public class ListSerializationUtil {

  public static void serializeList(List<String> list, String fileName) {
    try (FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(list);
      System.out.println("List serialized to " + fileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<String> deserializeList(String fileName) {
    List<String> deserializedList = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      deserializedList = (List<String>) ois.readObject();
      System.out.println("List deserialized from " + fileName);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return deserializedList;
  }
}
