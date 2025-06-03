package com.aiyuns.tinkerplay.Utils;

import java.io.*;
import java.lang.reflect.Field;

/**
 * @Author: aiYunS @Date: 2024年3月5日, 0005 上午 11:14:46 @Description: 对象拷贝工具类
 */
public class CopyUtil {

  // 对象的深拷贝
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> T deepCopy(T object)
      throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(object);
    out.flush();
    out.close();
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream in = new ObjectInputStream(bis);
    T copiedObject = (T) in.readObject();
    in.close();
    return copiedObject;
  }

  // 匹配的字段,一个对象的属性拷贝到另一个对象
  public static void copyMatchingFields(Object source, Object destination) {
    Class<?> sourceClass = source.getClass();
    Class<?> destinationClass = destination.getClass();
    for (Field sourceField : sourceClass.getDeclaredFields()) {
      // 获取源对象中的字段名和类型
      String fieldName = sourceField.getName();
      Class<?> fieldType = sourceField.getType();

      try {
        // 在目标对象中查找匹配的字段
        Field destinationField = destinationClass.getDeclaredField(fieldName);

        // 检查字段类型是否匹配
        if (destinationField.getType().equals(fieldType)) {
          // 设置源对象中字段的访问权限
          sourceField.setAccessible(true);
          // 设置目标对象中字段的访问权限
          destinationField.setAccessible(true);
          // 将源对象中的值复制到目标对象中
          destinationField.set(destination, sourceField.get(source));
        }
      } catch (NoSuchFieldException | IllegalAccessException e) {
        // 忽略字段未找到或拷贝时出现的异常
      }
    }
  }
}
