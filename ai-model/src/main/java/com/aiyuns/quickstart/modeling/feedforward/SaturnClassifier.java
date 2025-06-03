package com.aiyuns.quickstart.modeling.feedforward;

import com.aiyuns.utils.DownloaderUtility;
import com.aiyuns.utils.PlotUtil;
import java.io.File;
import java.util.concurrent.TimeUnit;
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
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

/**
 * 《土星》数据分类示例，基于Jason Baldridge的数据：https://github.com/jasonbaldridge/try-tf/tree/master/simdata
 *
 * @author Josh Patterson
 * @author Alex Black (added plots)
 */
@SuppressWarnings("DuplicatedCode")
public class SaturnClassifier {

  public static String dataLocalPath;
  public static boolean visualize = true;

  public static void main(String[] args) throws Exception {
    int batchSize = 50;
    int seed = 123;
    double learningRate = 0.005;
    // 训练轮数（数据的完整遍历次数）
    int nEpochs = 30;

    int numInputs = 2;
    int numOutputs = 2;
    int numHiddenNodes = 20;

    dataLocalPath = DownloaderUtility.CLASSIFICATIONDATA.Download();
    // 加载训练数据:
    RecordReader rr = new CSVRecordReader();
    rr.initialize(new FileSplit(new File(dataLocalPath, "saturn_data_train.csv")));
    DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, 2);

    // 加载测试/评估数据:
    RecordReader rrTest = new CSVRecordReader();
    rrTest.initialize(new FileSplit(new File(dataLocalPath, "saturn_data_eval.csv")));
    DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 2);

    // log.info("Build model....");
    MultiLayerConfiguration conf =
        new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(learningRate, 0.9))
            .list()
            .layer(
                new DenseLayer.Builder()
                    .nIn(numInputs)
                    .nOut(numHiddenNodes)
                    .activation(Activation.RELU)
                    .build())
            .layer(
                new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                    .activation(Activation.SOFTMAX)
                    .nIn(numHiddenNodes)
                    .nOut(numOutputs)
                    .build())
            .build();

    MultiLayerNetwork model = new MultiLayerNetwork(conf);
    model.init();
    model.setListeners(new ScoreIterationListener(10)); // 每10次参数更新打印一次得分

    model.fit(trainIter, nEpochs);

    System.out.println("Evaluate model....");
    Evaluation eval = model.evaluate(testIter);
    System.out.println(eval.stats());
    System.out.println("\n****************Example finished********************");

    // 训练完成。接下来的代码仅用于绘制数据和预测结果
    generateVisuals(model, trainIter, testIter);
  }

  public static void generateVisuals(
      MultiLayerNetwork model, DataSetIterator trainIter, DataSetIterator testIter)
      throws Exception {
    if (visualize) {
      double xMin = -15;
      double xMax = 15;
      double yMin = -15;
      double yMax = 15;

      // 我们将评估每个点在 x/y 输入空间中的预测，并在背景中绘制这个结果
      int nPointsPerAxis = 100;

      // 生成覆盖所有特征范围的 x, y 点
      INDArray allXYPoints = PlotUtil.generatePointsOnGraph(xMin, xMax, yMin, yMax, nPointsPerAxis);
      // 获取训练数据并与预测结果一起绘制
      PlotUtil.plotTrainingData(model, trainIter, allXYPoints, nPointsPerAxis);
      TimeUnit.SECONDS.sleep(3);
      // 获取测试数据，将其通过网络生成预测，并绘制这些预测结果:
      PlotUtil.plotTestData(model, testIter, allXYPoints, nPointsPerAxis);
    }
  }
}
