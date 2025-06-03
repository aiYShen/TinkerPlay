package com.aiyuns.DJL;

import ai.djl.MalformedModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author yuxinbai
 */
public class DistilBertInference {

  public static void main(String[] args)
      throws IOException, ModelNotFoundException, MalformedModelException {

    // 定义模型路径
    String modelNameOrPath = "/Users/yuxinbai/Documents/DistilBERT";

    // 输入 (问答)
    QAInput input = new QAInput("法国的首都是哪里?", "巴黎是法国的首都.");

    // *** 1. 创建 Translator (提取到外部) ***
    Translator<QAInput, String> translator =
        new Translator<>() {

          private HuggingFaceTokenizer tokenizer;

          @Override
          public void prepare(TranslatorContext ctx) throws IOException {
            tokenizer = HuggingFaceTokenizer.newInstance(modelNameOrPath);
          }

          @Override
          public NDList processInput(TranslatorContext ctx, QAInput input) {
            Encoding encoding = tokenizer.encode(input.getQuestion());
            // 获取 long[] 数组
            long[] inputIdsArray = encoding.getIds();
            long[] attentionMaskArray = encoding.getAttentionMask();

            // 使用 NDManager 从 long[] 创建 NDArray
            NDManager manager = ctx.getNDManager();
            NDArray inputIds = manager.create(inputIdsArray);
            NDArray attentionMask = manager.create(attentionMaskArray);

            // 将 NDArray 对象添加到 NDList
            return new NDList(inputIds, attentionMask);
          }

          @Override
          public String processOutput(TranslatorContext ctx, NDList list) {
            NDArray startLogits = list.get(0);
            NDArray endLogits = list.get(1);
            int startIdx = (int) startLogits.argMax().getLong();
            int endIdx = (int) endLogits.argMax().getLong();
            return tokenizer.decode(new long[] {startIdx, endIdx}, true);
          }
        };

    // *** 2. 使用 Criteria 构建器，并设置 Translator ***
    Criteria<QAInput, String> criteria =
        Criteria.builder()
            // 输入和输出类型
            .setTypes(QAInput.class, String.class)
            // 模型名称或路径
            .optModelPath(Paths.get(modelNameOrPath))
            // *** 设置 Translator ***
            .optTranslator(translator)
            // 明确指定模型文件名
            .optModelName("DistilBERT.pt")
            .optEngine("PyTorch")
            // 显示进度条
            .optProgress(new ProgressBar())
            .build();

    // *** 3. 使用 Criteria 加载模型并创建 Predictor ***
    try (Predictor<QAInput, String> predictor = criteria.loadModel().newPredictor()) {
      String prediction = predictor.predict(input);
      System.out.println("Answer: " + prediction);
    } catch (TranslateException e) {
      throw new RuntimeException(e);
    }
  }
}
