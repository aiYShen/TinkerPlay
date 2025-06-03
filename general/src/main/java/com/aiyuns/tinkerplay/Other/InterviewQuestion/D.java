package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 4:38:11 @Description: 又又又忘了
 */

// 抽象类实现接口,支持实现多个接口
abstract class D implements A, B, C {
  /*
     1. 抽象类不能被实例化(初学者很容易犯的错)，如果被实例化，就会报错，编译无法通过。只有抽象类的非抽象子类可以创建对象。
     2. 抽象类中不一定包含抽象方法，但是有抽象方法的类必定是抽象类。
     3. 抽象类中的抽象方法只是声明，不包含方法体，就是不给出方法的具体实现也就是方法的具体功能。
     4. 构造方法，类方法（用 static 修饰的方法）不能声明为抽象方法。
     5. 抽象类的子类必须给出抽象类中的抽象方法的具体实现，除非该子类也是抽象类。
  */

  static int a = 0;
  final int b = 1;
  private int c = 3;

  // 静态代码块
  static {
  }

  // 非静态代码块
  {
  }

  // 构造方法
  public D() {}

  // 抽象方法
  abstract void methmod1();

  // 非抽象方法
  @Override
  public void methmod2() {}
  ;

  // 非抽象静态方法
  static void methmod3() {}
}

// 抽象类继承抽象类,类不支持多继承
abstract class E extends D {}
