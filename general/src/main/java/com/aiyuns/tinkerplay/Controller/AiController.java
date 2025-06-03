package com.aiyuns.tinkerplay.Controller;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: aiYunS @Date: 2023年3月24日, 0024 下午 12:03:04 @Description: ChatGPT
 */
@Controller
@Tag(name = "AiController", description = "ChatGPT模块")
@RequestMapping("/AI")
public class AiController {

  @Operation(summary = "ChatGPT(开启异步)")
  @GetMapping("/sse")
  @CrossOrigin
  @ResponseBody
  @Async
  public CompletableFuture<String> sseEmitter(String prompt) {
    // 国内需要代理 国外不需要
    // Proxy proxy = Proxys.http("192.168.245.1", 15732);
    ChatGPT chatGPT =
        ChatGPT.builder()
            .timeout(600)
            .apiKey("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")
            // .proxy(proxy)
            .apiHost("https://api.openai.com/")
            .build()
            .init();
    // Message system = Message.ofSystem(prompt);
    Message message = Message.of(prompt);

    ChatCompletion chatCompletion =
        ChatCompletion.builder()
            .model(ChatCompletion.Model.GPT_3_5_TURBO)
            // .model("gpt-3.5-turbo-0613")
            .messages(Arrays.asList(message))
            .maxTokens(3000)
            .temperature(0.9)
            .build();
    ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
    Message res = response.getChoices().get(0).getMessage();
    System.out.println(res.getContent());
    return CompletableFuture.completedFuture(res.getContent());
  }

  @Operation(summary = "ChatGPT2(关闭异步)")
  @GetMapping("/sse2")
  @CrossOrigin
  @ResponseBody
  public String sseEmitter2(String prompt) {
    // 国内需要代理 国外不需要
    // Proxy proxy = Proxys.http("192.168.245.1", 15732);
    ChatGPT chatGPT =
        ChatGPT.builder()
            .timeout(600)
            .apiKey("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")
            // .proxy(proxy)
            .apiHost("https://api.openai.com/")
            .build()
            .init();
    // Message system = Message.ofSystem(prompt);
    Message message = Message.of(prompt);

    ChatCompletion chatCompletion =
        ChatCompletion.builder()
            .model(ChatCompletion.Model.GPT_3_5_TURBO)
            // .model("gpt-3.5-turbo-0613")
            .messages(Arrays.asList(message))
            .maxTokens(3000)
            .temperature(0.9)
            .build();
    ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
    Message res = response.getChoices().get(0).getMessage();
    System.out.println(res.getContent());
    return res.getContent();
  }

  @Operation(summary = "ChatGPT(流式使用)")
  @GetMapping("/sse3")
  @CrossOrigin
  public SseEmitter sseEmitter3(String prompt) {
    // 国内需要代理 国外不需要
    // Proxy proxy = Proxys.http("192.168.245.1", 15732);
    ChatGPTStream chatGPTStream =
        ChatGPTStream.builder()
            .timeout(600)
            .apiKey("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")
            // .proxy(proxy)
            .apiHost("https://api.openai.com/")
            .build()
            .init();
    SseEmitter sseEmitter = new SseEmitter(-1L);

    SseStreamListener listener = new SseStreamListener(sseEmitter);
    Message message = Message.of(prompt);

    chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);
    return sseEmitter;
  }
}
