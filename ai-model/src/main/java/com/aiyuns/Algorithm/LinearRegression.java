package com.aiyuns.Algorithm;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 线性回归: 找到一条直线（或更高维的超平面），让这条线尽可能“贴近”所有数据点 y=w⋅x+b y：预测的房价 x：房屋面积 w：斜率，表示面积每增加1单位，房价增加多少
 * b：截距，表示当面积为0时的房价（基线值）
 */
public class LinearRegression {
  public static void main(String[] args) {
    // 获取桌面路径
    String desktopPath =
        System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
    System.out.println("图形将保存到: " + desktopPath);

    // 数据
    double[] X = {1, 2, 3, 4}; // 房屋面积
    double[] y = {2, 4, 5, 4}; // 房价
    int n = X.length;

    // 初始化参数
    double w = 0.5; // 初始斜率
    double b = 1.0; // 初始截距
    double learningRate = 0.01; // 学习率
    int epochs = 1000; // 迭代次数

    // 记录损失和参数历史
    List<Double> losses = new ArrayList<>();
    List<Double> wHistory = new ArrayList<>();
    List<Double> bHistory = new ArrayList<>();
    wHistory.add(w);
    bHistory.add(b);

    // 梯度下降
    for (int epoch = 0; epoch < epochs; epoch++) {
      // 前向传播：计算预测值
      double[] yPred = new double[n];
      for (int i = 0; i < n; i++) {
        yPred[i] = w * X[i] + b;
      }

      // 计算误差和损失（MSE）
      double[] error = new double[n];
      double loss = 0;
      for (int i = 0; i < n; i++) {
        error[i] = yPred[i] - y[i];
        loss += error[i] * error[i];
      }
      loss /= n;
      losses.add(loss);

      // 计算梯度
      double dw = 0, db = 0;
      for (int i = 0; i < n; i++) {
        dw += 2 * error[i] * X[i];
        db += 2 * error[i];
      }
      dw /= n;
      db /= n;

      // 更新参数
      w -= learningRate * dw;
      b -= learningRate * db;

      // 记录参数
      wHistory.add(w);
      bHistory.add(b);

      // 每200次打印一次
      if (epoch % 200 == 0) {
        System.out.printf("Epoch %d: Loss = %.4f, w = %.4f, b = %.4f%n", epoch, loss, w, b);
      }
    }

    // 打印最终参数
    System.out.printf("\n最终参数：斜率 w = %.2f, 截距 b = %.2f%n", w, b);

    // 图形1：拟合结果
    XYSeriesCollection dataset1 = new XYSeriesCollection();
    XYSeries dataPoints = new XYSeries("数据点");
    for (int i = 0; i < n; i++) {
      dataPoints.add(X[i], y[i]);
    }
    XYSeries initialLine = new XYSeries("初始直线");
    XYSeries finalLine = new XYSeries("最终拟合直线");
    for (double x = 0; x <= 5; x += 0.1) {
      initialLine.add(x, wHistory.get(0) * x + bHistory.get(0));
      finalLine.add(x, w * x + b);
    }
    dataset1.addSeries(dataPoints);
    dataset1.addSeries(initialLine);
    dataset1.addSeries(finalLine);

    JFreeChart chart1 =
        ChartFactory.createXYLineChart(
            "线性回归拟合", "面积", "房价", dataset1, PlotOrientation.VERTICAL, true, true, false);
    XYPlot plot1 = chart1.getXYPlot();
    XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
    renderer1.setSeriesLinesVisible(0, false);
    renderer1.setSeriesShapesVisible(0, true);
    renderer1.setSeriesPaint(0, Color.BLUE);
    renderer1.setSeriesPaint(1, Color.GREEN);
    renderer1.setSeriesPaint(2, Color.RED);
    plot1.setRenderer(renderer1);
    try {
      ChartUtils.saveChartAsPNG(
          new File(desktopPath + "linear_regression_fit.png"), chart1, 800, 600);
      System.out.println("图形1已保存到桌面: linear_regression_fit.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 图形2：优化过程
    XYSeriesCollection dataset2 = new XYSeriesCollection();
    dataset2.addSeries(dataPoints);
    for (int i = 0; i < wHistory.size(); i += 200) {
      XYSeries line = new XYSeries("Epoch " + i);
      for (double x = 0; x <= 5; x += 0.1) {
        line.add(x, wHistory.get(i) * x + bHistory.get(i));
      }
      dataset2.addSeries(line);
    }
    XYSeries finalLineSeries = new XYSeries("最终拟合直线");
    for (double x = 0; x <= 5; x += 0.1) {
      finalLineSeries.add(x, w * x + b);
    }
    dataset2.addSeries(finalLineSeries);

    JFreeChart chart2 =
        ChartFactory.createXYLineChart(
            "梯度下降优化过程", "面积", "房价", dataset2, PlotOrientation.VERTICAL, true, true, false);
    try {
      ChartUtils.saveChartAsPNG(
          new File(desktopPath + "linear_regression_optimization.png"), chart2, 800, 600);
      System.out.println("图形2已保存到桌面: linear_regression_optimization.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 图形3：损失曲线
    XYSeries lossSeries = new XYSeries("MSE 损失");
    for (int i = 0; i < losses.size(); i++) {
      lossSeries.add(i, losses.get(i));
    }
    XYSeriesCollection dataset3 = new XYSeriesCollection(lossSeries);
    JFreeChart chart3 =
        ChartFactory.createXYLineChart(
            "损失随迭代变化", "迭代次数", "损失 (MSE)", dataset3, PlotOrientation.VERTICAL, true, true, false);
    try {
      ChartUtils.saveChartAsPNG(new File(desktopPath + "loss_curve.png"), chart3, 800, 600);
      System.out.println("图形3已保存到桌面: loss_curve.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 预测示例
    double xTest = 5;
    double yTest = w * xTest + b;
    System.out.printf("预测：面积 %.0f 的房价为 %.2f%n", xTest, yTest);
  }
}
