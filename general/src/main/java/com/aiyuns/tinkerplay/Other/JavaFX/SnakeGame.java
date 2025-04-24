package com.aiyuns.tinkerplay.Other.JavaFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends Application {
    // 游戏设置
    private static final int GRID_SIZE = 20; // 网格大小（20x20）
    private static final int TILE_SIZE = 30; // 每个格子像素大小
    private static final int WIDTH = GRID_SIZE * TILE_SIZE; // 画布宽度
    private static final int HEIGHT = GRID_SIZE * TILE_SIZE; // 画布高度
    private static final double SPEED = 0.1; // 每秒更新次数（秒）

    // 游戏状态
    private List<Point> snake = new ArrayList<>(); // 蛇的身体
    private Point food; // 食物位置
    private Direction direction = Direction.RIGHT; // 蛇的移动方向
    private boolean gameOver = false;
    private int score = 0;
    private long lastUpdate = 0;

    // 随机数生成器
    private final Random random = new Random();

    // 方向枚举
    private enum Direction { UP, DOWN, LEFT, RIGHT }

    // 坐标点类
    private static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }

    @Override
    public void start(Stage primaryStage) {
        // 初始化画布
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 初始化布局
        VBox root = new VBox(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT + 50);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // 初始化游戏
        resetGame();

        // 处理键盘输入
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP:
                    if (direction != Direction.DOWN) direction = Direction.UP;
                    break;
                case DOWN:
                    if (direction != Direction.UP) direction = Direction.DOWN;
                    break;
                case LEFT:
                    if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    break;
                case RIGHT:
                    if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    break;
                case R:
                    if (gameOver) resetGame();
                    break;
            }
        });

        // 游戏循环
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) {
                    renderGameOver(gc);
                    return;
                }

                // 控制更新频率
                if (now - lastUpdate >= SPEED * 1_000_000_000) {
                    updateGame();
                    renderGame(gc);
                    lastUpdate = now;
                }
            }
        }.start();
    }

    private void resetGame() {
        snake.clear();
        snake.add(new Point(5, 5)); // 初始蛇位置
        direction = Direction.RIGHT;
        spawnFood();
        score = 0;
        gameOver = false;
        lastUpdate = 0;
    }

    private void spawnFood() {
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
            final int finalX = x; // 临时 final 变量
            final int finalY = y;
            if (!snake.stream().anyMatch(p -> p.x == finalX && p.y == finalY)) {
                food = new Point(x, y);
                return;
            }
        } while (true);
    }

    private void updateGame() {
        // 获取蛇头
        Point head = new Point(snake.get(0).x, snake.get(0).y);

        // 根据方向移动
        switch (direction) {
            case UP: head.y--; break;
            case DOWN: head.y++; break;
            case LEFT: head.x--; break;
            case RIGHT: head.x++; break;
        }

        // 检查碰撞
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE ||
                snake.stream().anyMatch(p -> p.x == head.x && p.y == head.y)) {
            gameOver = true;
            return;
        }

        // 移动蛇
        snake.add(0, head);
        if (head.x == food.x && head.y == food.y) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void renderGame(GraphicsContext gc) {
        // 清空画布
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制蛇
        gc.setFill(Color.GREEN);
        for (Point p : snake) {
            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 2, TILE_SIZE - 2);
        }

        // 绘制食物
        gc.setFill(Color.RED);
        gc.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE - 2, TILE_SIZE - 2);

        // 绘制分数
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score: " + score, 10, HEIGHT + 30);
    }

    private void renderGameOver(GraphicsContext gc) {
        // 绘制游戏结束画面
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText("Game Over!", WIDTH / 2 - 80, HEIGHT / 2 - 20);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score: " + score, WIDTH / 2 - 40, HEIGHT / 2 + 20);
        gc.fillText("Press R to Restart", WIDTH / 2 - 80, HEIGHT / 2 + 60);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
