package com.aiyuns.tinkerplay.Other.DesignMode.DynamicProxyMode;

/**
 * @Author: aiYunS @Date: 2023年10月9日, 0009 下午 2:01:43 @Description: 动态代理模式
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义一个接口
interface MyInterface {
  void doSomething();
}

// 实现接口的类
class MyImplementation implements MyInterface {

  @Override
  public void doSomething() {
    System.out.println("Real object is doing something.");
  }
}

// 实现 InvocationHandler 接口的代理处理类
class MyInvocationHandler implements InvocationHandler {
  private Object target;

  public MyInvocationHandler(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 在方法调用前执行额外的操作
    System.out.println("Before method invocation");

    // 调用被代理对象的方法
    Object result = method.invoke(target, args);

    // 在方法调用后执行额外的操作
    System.out.println("After method invocation");

    return result;
  }
}

public class DynamicProxy {
  public static void main(String[] args) {
    // 创建被代理对象
    MyInterface realObject = new MyImplementation();

    // 创建代理对象
    MyInterface proxyObject =
        (MyInterface)
            Proxy.newProxyInstance(
                MyInterface.class.getClassLoader(),
                new Class[] {MyInterface.class},
                new MyInvocationHandler(realObject));

    // 调用代理对象的方法，实际上会触发 MyInvocationHandler 的 invoke 方法
    proxyObject.doSomething();
  }
}
