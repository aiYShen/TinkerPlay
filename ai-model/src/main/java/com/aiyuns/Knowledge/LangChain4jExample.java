package com.aiyuns.Knowledge;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import java.util.List;

/** 文档结构化切片. 根据文档的特定格式（如 Markdown、HTML、Java 源代码）进行切片 */
public class LangChain4jExample {
  public static void main(String[] args) {
    Document doc = Document.from("你的超长文本内容...");

    // 创建一个递归切片器，每块200字符，重叠40字符
    DocumentSplitter splitter = DocumentSplitters.recursive(200, 40);

    List<TextSegment> segments = splitter.split(doc);
    segments.forEach(seg -> System.out.println(seg.text()));
  }
}
