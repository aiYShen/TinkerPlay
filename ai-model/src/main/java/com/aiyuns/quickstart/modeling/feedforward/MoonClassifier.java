package com.aiyuns.quickstart.modeling.feedforward;

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
import com.aiyuns.utils.DownloaderUtility;
import com.aiyuns.utils.PlotUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * “月亮”数据分类示例，基于Jason Baldridge的数据：https://github.com/jasonbaldridge/try-tf/tree/master/simdata
 *
 * @author Josh Patterson
 * @author Alex Black (added plots)
 *
 */
@SuppressWarnings("DuplicatedCode")
public class MoonClassifier {

    public static boolean visualize = true;
    public static String dataLocalPath;

    public static void main(String[] args) throws Exception {
        int seed = 123;
        double learningRate = 0.005;
        int batchSize = 50;
        int nEpochs = 100;

        int numInputs = 2;
        int numOutputs = 2;
        int numHiddenNodes = 50;

        dataLocalPath = DownloaderUtility.CLASSIFICATIONDATA.Download();

        //加载训练数据:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(dataLocalPath,"moon_data_train.csv")));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr,batchSize,0,2);

        //加载测试/评估数据:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(dataLocalPath,"moon_data_eval.csv")));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest,batchSize,0,2);

        //log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(learningRate, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .nIn(numHiddenNodes).nOut(numOutputs).build())
                .build();


        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));    //Print score every 100 parameter updates

        model.fit( trainIter, nEpochs );

        System.out.println("Evaluate model....");
        Evaluation eval = model.evaluate(testIter);

        //打印评估统计信息
        System.out.println(eval.stats());
        System.out.println("\n****************Example finished********************");

        //训练完成。接下来的代码仅用于绘制数据和预测结果
        generateVisuals(model, trainIter, testIter);
    }

    public static void generateVisuals(MultiLayerNetwork model, DataSetIterator trainIter, DataSetIterator testIter) throws Exception {
        if (visualize) {
            double xMin = -1.5;
            double xMax = 2.5;
            double yMin = -1;
            double yMax = 1.5;

            //让我们在 x/y 输入空间的每个点评估预测，并将其绘制在背景中
            int nPointsPerAxis = 100;

            //生成覆盖整个特征范围的 x, y 点
            INDArray allXYPoints = PlotUtil.generatePointsOnGraph(xMin, xMax, yMin, yMax, nPointsPerAxis);
            //获取训练数据并绘制预测结果
            PlotUtil.plotTrainingData(model, trainIter, allXYPoints, nPointsPerAxis);
            TimeUnit.SECONDS.sleep(3);
            //获取测试数据，将测试数据输入网络生成预测，并绘制这些预测结果:
            PlotUtil.plotTestData(model, testIter, allXYPoints, nPointsPerAxis);
        }
    }

}
