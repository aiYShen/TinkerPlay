package com.aiyuns.tinkerplay.Other.JavaFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXDemo extends Application {

  @Override
  public void start(Stage primaryStage) {
    // 创建一个按钮
    Button btn = new Button();
    btn.setText("Say 'Hello World'");
    btn.setOnAction(event -> System.out.println("Hello World!"));

    // 创建布局
    StackPane root = new StackPane();
    root.getChildren().add(btn);

    // 创建场景
    Scene scene = new Scene(root, 300, 250);

    // 设置舞台
    primaryStage.setTitle("Hello World!");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
