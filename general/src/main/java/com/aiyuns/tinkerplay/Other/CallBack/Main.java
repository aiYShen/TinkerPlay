package com.aiyuns.tinkerplay.Other.CallBack;

public class Main {

    public static void main(String[] args) {
        Caller caller = new Caller();

        // 使用匿名内部类实现回调
        caller.registerCallback(new Callback() {
            @Override
            public void onCallback(String message) {
                System.out.println("回调收到消息: " + message);
            }
        });

        caller.doSomething();
    }
    
}
