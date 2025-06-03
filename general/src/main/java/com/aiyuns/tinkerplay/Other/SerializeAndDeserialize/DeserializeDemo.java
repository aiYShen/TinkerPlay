package com.aiyuns.tinkerplay.Other.SerializeAndDeserialize;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializeDemo {
  public static void main(String[] args) {
    Person person = null;
    try (FileInputStream fileIn = new FileInputStream("/Users/yuxinbai/Desktop/person.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn)) {
      person = (Person) in.readObject();
    } catch (IOException | ClassNotFoundException i) {
      i.printStackTrace();
    }
    System.out.println("Deserialized Person...");
    System.out.println("Name: " + person.getName());
    System.out.println("Age: " + person.getAge());
  }
}
