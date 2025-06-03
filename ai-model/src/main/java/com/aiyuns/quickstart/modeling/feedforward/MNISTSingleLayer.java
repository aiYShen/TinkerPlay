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
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个简单的多层感知机（MLP），应用于MNIST数据集的数字分类（MNIST数据集）。此文件构建了一个输入层和一个隐藏层。
 *
 * <p>• 输入层：该层的输入维度为 numRows*numColumns，其中这些变量表示图像中垂直和水平像素的数量。 此层使用 ReLU（修正线性单元）激活函数。该层的权重使用
 * Xavier初始化（了解Xavier初始化）进行初始化，以避免学习曲线过陡。 此层将有1000个输出信号传递给隐藏层。 •
 * 隐藏层：该层的输入维度为1000，这些信号来自输入层。该层的权重同样使用 Xavier初始化。 此层的激活函数为 Softmax，它对所有10个输出进行标准化，使得标准化后的和为1。
 * 最终，选择这些标准化值中最大的作为预测的类别
 */
public class MNISTSingleLayer {

  private static Logger log = LoggerFactory.getLogger(MNISTSingleLayer.class);

  public static void main(String[] args) throws Exception {
    // 输入图像的行数和列数
    final int numRows = 28;
    final int numColumns = 28;
    int outputNum = 10; // 输出类别的数量
    int batchSize = 128; // 每个epoch的批量大小
    int rngSeed = 123; // 用于可重复性的随机数种子
    int numEpochs = 15; // 要执行的训练周期数

    // 获取 DataSetIterators:
    DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
    DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);

    log.info("Build model....");
    MultiLayerConfiguration conf =
        new NeuralNetConfiguration.Builder()
            .seed(rngSeed) // 包括一个随机种子以确保可重复性
            // 使用随机梯度下降（SGD）作为优化算法
            .updater(new Nesterovs(0.006, 0.9))
            .l2(1e-4)
            .list()
            .layer(
                new DenseLayer.Builder() // 创建第一个输入层并使用 Xavier 初始化
                    .nIn(numRows * numColumns)
                    .nOut(1000)
                    .activation(Activation.RELU)
                    .weightInit(WeightInit.XAVIER)
                    .build())
            .layer(
                new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) // 创建隐藏层
                    .nIn(1000)
                    .nOut(outputNum)
                    .activation(Activation.SOFTMAX)
                    .weightInit(WeightInit.XAVIER)
                    .build())
            .build();

    MultiLayerNetwork model = new MultiLayerNetwork(conf);
    model.init();
    // 每进行一次迭代打印一次分数
    model.setListeners(new ScoreIterationListener(1));

    log.info("Train model....");
    model.fit(mnistTrain, numEpochs);

    log.info("Evaluate model....");
    Evaluation eval = model.evaluate(mnistTest);
    log.info(eval.stats());
    log.info("****************Example finished********************");
  }
}
