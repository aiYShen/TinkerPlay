package com.aiyuns.quickstart.modeling.feedforward;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.evaluation.classification.Evaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 这个基本示例展示了如何手动创建一个数据集并训练一个基础的神经网络。
 该网络由 2 个输入神经元，1 个隐藏层（包含 4 个隐藏神经元），以及 2 个输出神经元组成。
 我选择了 2 个输出神经元（第一个用于表示假，第二个用于表示真），因为 Evaluation 类需要为每个分类配置一个神经元。
 * <p>
 * +---------+---------+---------------+--------------+
 * | Input 1 | Input 2 | Label 1(XNOR) | Label 2(XOR) |
 * +---------+---------+---------------+--------------+
 * |    0    |    0    |       1       |       0      |
 * +---------+---------+---------------+--------------+
 * |    1    |    0    |       0       |       1      |
 * +---------+---------+---------------+--------------+
 * |    0    |    1    |       0       |       1      |
 * +---------+---------+---------------+--------------+
 * |    1    |    1    |       1       |       0      |
 * +---------+---------+---------------+--------------+
 *
 * @author Peter Großmann
 * @author Dariusz Zbyrad
 */
public class ModelXOR {

    private static final Logger log = LoggerFactory.getLogger(ModelXOR.class);

    public static void main(String[] args) {

        int seed = 1234;        // 用于初始化伪随机数生成器的数字
        int nEpochs = 10000;    // 训练的周期数

        log.info("Data preparation...");

        // 列出输入值，包含4个训练样本的数据，其中每个样本包含2个特征
        // 每个输入神经元
        INDArray input = Nd4j.zeros(4, 2);

        // 相应的期望输出值列表，4个训练样本
        // 包含每个输出神经元2个数据的期望输出值列表，4个训练样本
        INDArray labels = Nd4j.zeros(4, 2);

        // 创建第一个数据集
        // 当第一个输入=0 且第二个输入=0 时
        input.putScalar(new int[]{0, 0}, 0);
        input.putScalar(new int[]{0, 1}, 0);
        // 则第一个输出为假，第二个输出为0（见类注释）
        labels.putScalar(new int[]{0, 0}, 1);
        labels.putScalar(new int[]{0, 1}, 0);

        // 当第一个输入=1 且第二个输入=0 时
        input.putScalar(new int[]{1, 0}, 1);
        input.putScalar(new int[]{1, 1}, 0);
        // 则 XOR 为真，因此第二个输出神经元被激活
        labels.putScalar(new int[]{1, 0}, 0);
        labels.putScalar(new int[]{1, 1}, 1);

        // 与上面相同
        input.putScalar(new int[]{2, 0}, 0);
        input.putScalar(new int[]{2, 1}, 1);
        labels.putScalar(new int[]{2, 0}, 0);
        labels.putScalar(new int[]{2, 1}, 1);

        // 当两个输入都为 1 时，XOR 为假，因此第一个输出应被激活
        input.putScalar(new int[]{3, 0}, 1);
        input.putScalar(new int[]{3, 1}, 1);
        labels.putScalar(new int[]{3, 0}, 1);
        labels.putScalar(new int[]{3, 1}, 0);

        // 创建数据集对象
        DataSet ds = new DataSet(input, labels);

        log.info("Network configuration and training...");

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.1))
                .seed(seed)
                .biasInit(0) // 将偏置初始化为0——也作为经验值
                // 通过摄取（ingesting）输入，网络可以更快、更准确地处理输入数据
                // 小批量（minibatches）每次并行处理5到10个元素.
                // 由于数据集比小批量大小要小，这个例子在没有使用小批量的情况下运行得更好
                .miniBatch(false)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(2)
                        .nOut(4)
                        .activation(Activation.SIGMOID)
                        // 随机初始化权重，取值范围在0和1之间
                        .weightInit(new UniformDistribution(0, 1))
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(2)
                        .activation(Activation.SOFTMAX)
                        .weightInit(new UniformDistribution(0, 1))
                        .build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        // 添加一个监听器，每100次参数更新输出一次误差
        net.setListeners(new ScoreIterationListener(100));

        // 从 LSTMCharModellingExample 复制并粘贴
        // 打印网络中的参数数量（以及每一层的参数数量）
        System.out.println(net.summary());

        // 在这里，实际的学习发生
        for( int i=0; i < nEpochs; i++ ) {
            net.fit(ds);
        }

        // 为每个训练样本创建输出
        INDArray output = net.output(ds.getFeatures());
        System.out.println(output);

        // 让 Evaluation 打印统计信息，显示正确的输出有多少次具有最高值
        Evaluation eval = new Evaluation();
        eval.eval(ds.getLabels(), output);
        System.out.println(eval.stats());

    }
}
