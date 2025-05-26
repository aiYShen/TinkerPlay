package com.aiyuns.tinkerplay.Other.CallBack;

// 定义一个调用者类
public class Caller {

    private Callback callback;

    // 注册回调
    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    // 执行任务并触发回调
    public void doSomething() {
        System.out.println("Caller: 正在执行任务...");
        if (callback != null) {
            callback.onCallback("任务完成！");
        }
    }

}
