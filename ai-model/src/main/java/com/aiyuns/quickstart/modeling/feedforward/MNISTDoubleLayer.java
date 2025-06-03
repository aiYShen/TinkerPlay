package com.aiyuns.quickstart.modeling.feedforward;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个稍微复杂一点的多层感知器（MLP）应用于MNIST数据集（http://yann.lecun.com/exdb/mnist/）的数字分类。
 * 这个示例使用了两个输入层和一个隐藏层。第一个输入层的输入维度为numRows * numColumns，其中这些变量表示图像的垂直和水平像素数。
 * 这个层使用了修正线性单元（ReLU）激活函数。该层的权重通过使用Xavier初始化
 * （https://prateekvjoshi.com/2016/03/29/understanding-xavier-initialization-in-deep-neural-networks/）进行初始化，
 * 以避免出现陡峭的学习曲线。该层将500个输出信号传递给第二层。第二个输入层的输入维度为500。这个层也使用ReLU激活函数。
 * 该层的权重同样通过使用Xavier初始化进行初始化，以避免出现陡峭的学习曲线。该层将100个输出信号传递给隐藏层。隐藏层的输入维度为100，
 * 这些数据来自第二个输入层。该层的权重同样使用Xavier初始化。该层的激活函数是softmax，它将所有10个输出标准化，使得标准化后的和加起来等于1。
 * 然后选择这些标准化值中最高的作为预测的类别
 */
public class MNISTDoubleLayer {

  private static Logger log = LoggerFactory.getLogger(MNISTDoubleLayer.class);

  public static void main(String[] args) throws Exception {
    // 输入图像的行数和列数
    final int numRows = 28;
    final int numColumns = 28;
    int outputNum = 10; // 输出类别的数量
    int batchSize = 64; // 每个epoch的批次大小
    int rngSeed = 123; // 用于可重复性的随机数种子
    int numEpochs = 15; // 要执行的训练轮数（epoch）
    double rate = 0.0015; // 学习率

    // 获取 DataSetIterators:
    DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
    DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);

    log.info("Build model....");
    MultiLayerConfiguration conf =
        new NeuralNetConfiguration.Builder()
            .seed(rngSeed) // 包括一个随机种子以确保可重复性
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .l2(rate * 0.005) // 正则化学习模型
            .list()
            .layer(
                new DenseLayer.Builder() // 创建第一个输入层
                    .nIn(numRows * numColumns)
                    .nOut(500)
                    .build())
            .layer(
                new DenseLayer.Builder() // 创建第二个输入层
                    .nIn(500)
                    .nOut(100)
                    .build())
            .layer(
                new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) // 创建隐藏层
                    .activation(Activation.SOFTMAX)
                    .nOut(outputNum)
                    .build())
            .build();

    MultiLayerNetwork model = new MultiLayerNetwork(conf);
    model.init();
    model.setListeners(new ScoreIterationListener(5)); // 在每次迭代时打印得分

    log.info("Train model....");
    model.fit(mnistTrain, numEpochs);

    log.info("Evaluate model....");
    Evaluation eval = model.evaluate(mnistTest);

    log.info(eval.stats());
    log.info("****************Example finished********************");
  }
}
