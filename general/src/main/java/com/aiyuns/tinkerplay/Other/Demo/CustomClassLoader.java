package com.aiyuns.tinkerplay.Other.Demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

  private String classDir;

  public CustomClassLoader(String classDir) {
    this.classDir = classDir;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    String classPath = name.replace('.', '/') + "/" + name + ".class";
    Path classFile = Paths.get(classDir, classPath);
    if (Files.exists(classFile)) {
      try {
        byte[] classBytes = Files.readAllBytes(classFile);
        return defineClass(name, classBytes, 0, classBytes.length);
      } catch (IOException e) {
        throw new ClassNotFoundException("Failed to load class " + name, e);
      }
    } else {
      // 找不到此类,则尝试委托父类加载器去加载
      return super.findClass(name);
    }
  }

  public static void main(String[] args) {
    // 类路金
    String customClassDir =
        "/Users/yuxinbai/aiyuns/TinkerPlay/src/main/java/com/aiyuns/tinkerplay/Other/Demo";
    // 创建自定义的类加载器
    CustomClassLoader customClassLoader = new CustomClassLoader(customClassDir);
    try {
      // 加载类
      Class<?> myClass =
          customClassLoader.loadClass("com.aiyuns.tinkerplay.Other.Demo.ChildClass1");
      // 创建实例对象
      Object object = myClass.getDeclaredConstructor().newInstance();
      // 打印类加载器信息
      System.out.println(object.getClass().getClassLoader());
      // 调用类方法
      myClass.getMethod("methmod").invoke(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
