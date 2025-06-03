package com.aiyuns.quickstart.modeling.feedforward;

import com.aiyuns.utils.DownloaderUtility;
import java.io.File;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    《简单的多层感知器（MLP）神经网络》

    1. 数据加载
	•	CSVRecordReader 用于加载 iris.txt 数据文件。该数据文件包含了鸢尾花的数据集，文件以逗号分隔。
	•	使用 RecordReaderDataSetIterator 将加载的数据转换为 DataSet 对象，准备好输入到神经网络中。每行数据包含 4 个输入特征和 1 个标签（类别）。

2. 数据预处理
	•	数据分为训练集和测试集，使用 65% 的数据用于训练，剩余 35% 用于测试。
	•	标准化数据：通过 NormalizerStandardize 对数据进行标准化处理，使每个特征的均值为 0，方差为 1。
	•	从训练数据中收集统计信息（均值和标准差），然后将其应用于训练和测试数据。

3. 构建神经网络模型
	•	神经网络配置：
	•	输入层有 4 个神经元（与鸢尾花的 4 个特征相对应）。
	•	隐藏层由两个 DenseLayer 组成，输入和输出的神经元数分别为 4->3 和 3->3。
	•	输出层使用 Softmax 激活函数，适用于分类任务，输出 3 个类别（鸢尾花的 3 个类型）。
	•	使用 Xavier 初始化方法初始化权重，采用 SGD（随机梯度下降）优化器，学习率为 0.1。
	•	L2 正则化用于防止过拟合，l2(1e-4)。

4. 训练模型
	•	通过调用 model.fit(trainingData) 训练模型，迭代 1000 次，使用训练数据进行学习。
	•	每 100 次迭代记录一次模型的得分，以监控训练过程。

5. 评估模型
	•	使用 Evaluation 对象评估模型在测试集上的性能。通过计算模型输出和真实标签之间的误差，输出分类评估指标（如精度、召回率等）。
	•	最后通过 log.info(eval.stats()) 打印评估结果。
 */
@SuppressWarnings("DuplicatedCode")
public class IrisClassifier {

  private static Logger log = LoggerFactory.getLogger(IrisClassifier.class);

  public static void main(String[] args) throws Exception {

    // 首先：使用记录阅读器获取数据集。CSVRecordReader处理加载/解析
    int numLinesToSkip = 0;
    char delimiter = ',';
    RecordReader recordReader = new CSVRecordReader(numLinesToSkip, delimiter);
    recordReader.initialize(
        new FileSplit(new File(DownloaderUtility.IRISDATA.Download(), "iris.txt")));

    // 第二: RecordReaderDataSetIterator 负责将数据转换为 DataSet 对象，准备好用于神经网络
    int labelIndex = 4; // iris.txt CSV 中每行有 5 个值：4 个输入特征，后跟一个整数标签（类别）索引。标签是每行的第 5 个值（索引为 4）
    int numClasses = 3; // iris 数据集包含 3 个类别（types of iris flowers）。类别的整数值为 0、1 或 2
    int batchSize = 150; // Iris 数据集：总共有 150 个样本。我们将它们全部加载到一个 DataSet 中（不推荐用于大型数据集）

    DataSetIterator iterator =
        new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);
    DataSet allData = iterator.next();
    allData.shuffle();
    SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65); // 使用 65% 的数据进行训练

    DataSet trainingData = testAndTrain.getTrain();
    DataSet testData = testAndTrain.getTest();

    // 我们需要对数据进行标准化。我们将使用 NormalizeStandardize（它将数据转化为均值为 0，方差为 1）
    DataNormalization normalizer = new NormalizerStandardize();
    normalizer.fit(trainingData); // 从训练数据中收集统计信息（均值/标准差）。这不会修改输入数据
    normalizer.transform(trainingData); // 对训练数据应用标准化
    normalizer.transform(testData); // 对测试数据应用标准化。这是使用从 训练 数据集计算得出的统计信息

    final int numInputs = 4;
    int outputNum = 3;
    long seed = 6;

    log.info("Build model....");
    MultiLayerConfiguration conf =
        new NeuralNetConfiguration.Builder()
            .seed(seed)
            .activation(Activation.TANH)
            .weightInit(WeightInit.XAVIER)
            .updater(new Sgd(0.1))
            .l2(1e-4)
            .list()
            .layer(new DenseLayer.Builder().nIn(numInputs).nOut(3).build())
            .layer(new DenseLayer.Builder().nIn(3).nOut(3).build())
            .layer(
                new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .activation(Activation.SOFTMAX) // 为这一层将全局的 TANH 激活函数覆盖为 softmax
                    .nIn(3)
                    .nOut(outputNum)
                    .build())
            .build();

    // 运行模型
    MultiLayerNetwork model = new MultiLayerNetwork(conf);
    model.init();
    // 每 100 次迭代记录一次得分
    model.setListeners(new ScoreIterationListener(100));

    for (int i = 0; i < 1000; i++) {
      model.fit(trainingData);
    }

    // 在测试集上评估模型
    Evaluation eval = new Evaluation(3);
    INDArray output = model.output(testData.getFeatures());
    eval.eval(testData.getLabels(), output);
    log.info(eval.stats());
  }
}
