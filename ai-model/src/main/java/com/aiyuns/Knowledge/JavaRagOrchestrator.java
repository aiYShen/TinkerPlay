package com.aiyuns.Knowledge;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.time.Duration;

/** 典型的 RAG 编排流程包含：文档加载 -> 切片 -> 向量化与存储 -> 检索 -> 提示词组装 -> 大模型生成. */
public class JavaRagOrchestrator {
  // 定义 AI 服务接口
  interface IntelligentAssistant {
    String ask(String question);
  }

  public static void main(String[] args) {
    // 1. 配置本地 LLM (LM Studio)
    OpenAiChatModel model =
        OpenAiChatModel.builder()
            .baseUrl("http://localhost:1234/v1") // LM Studio API 端点
            .apiKey("google/gemma-3n-e4b")
            .timeout(Duration.ofMinutes(1))
            .maxRetries(1)
            .logRequests(true)
            .logResponses(true)
            .build();

    // 2. 配置本地 Embedding 模型 (实现全本地化)
    EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    // 3. 准备知识库 (加载并切片)
    // 假设你有一个包含 Java 并发规范的文档
    EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    // 1.10.0 推荐的链式调用加载方式
    Document document =
        Document.from(
            """
                公司 Java 规范：
                1. 所有业务线程池必须由 Spring 管理，严禁手动 new Thread()。
                2. 线程池核心数 corePoolSize 必须根据 CPU 密集型或 IO 密集型配置。
                3. 必须配置拒绝策略为 CallerRunsPolicy 以防任务丢失。
                """);

    // 使用递归字符切片 (500字符一块，100重叠)
    var segments = DocumentSplitters.recursive(500, 100).split(document);

    // 将切片向量化并存入内存库
    embeddingStore.addAll(embeddingModel.embedAll(segments).content(), segments);

    // 4. 构建内容检索器
    ContentRetriever contentRetriever =
        EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(3)
            .minScore(0.6) // 1.10.0 支持更精细的过滤
            .build();

    // 5. 最终编排
    IntelligentAssistant assistant =
        AiServices.builder(IntelligentAssistant.class)
            .chatModel(model)
            .contentRetriever(contentRetriever)
            // 1.10.0 默认支持对话记忆，不需要额外配置简单场景的 ChatMemory
            .build();

    // 6. 运行
    String response = assistant.ask("解释一下为什么在 Java 规范中禁止手动 new Thread()?");
    System.out.println("\n[本地 AI 回答]:\n" + response);
  }
}
