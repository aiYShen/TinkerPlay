package com.aiyuns.tinkerplay.Other.SerializeAndDeserialize;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Person implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  // Getters and toString method
  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Person{name='" + name + "', age=" + age + '}';
  }
}

public class SerializeDemo {
  public static void main(String[] args) {
    Person person = new Person("Alice", 30);
    try (FileOutputStream fileOut = new FileOutputStream("/Users/yuxinbai/Desktop/person.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
      out.writeObject(person);
      System.out.println("Serialized data is saved in person.ser");
    } catch (IOException i) {
      i.printStackTrace();
    }
  }
}
