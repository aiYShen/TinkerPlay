package com.aiyuns.tinkerplay.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Author: aiYunS @Date: 2022-11-22 15:07 @Description: 解析HTML页面工具类
 */
public class ResolverHTMLUtil {

  // 爬博客园文章
  public static void ResolverHTML1(String html) {
    // 6.Jsoup解析html
    Document document = Jsoup.parse(html);
    // 像js一样，通过标签获取title
    System.out.println(document.getElementsByTag("title").first());
    // 像js一样，通过id 获取文章列表元素对象
    Element postList =
        document
            .getElementById("wrapper")
            .getElementById("main_content")
            .getElementById("main_flow")
            .getElementById("post_list");
    // 像js一样，通过class 获取列表下的所有博客
    Elements postItems = postList.getElementsByClass("post-item");
    // 循环处理每篇博客
    for (Element postItem : postItems) {
      // 像jquery选择器一样，获取文章标题元素
      Elements titleEle = postItem.select(".post-item-body a[class='post-item-title']");
      Elements titleEle1 = postItem.select(".post-item-text a[class='post-item-title']");
      System.out.println("文章标题:" + titleEle.text());
      System.out.println("文章地址:" + titleEle.attr("href"));
      // 像jquery选择器一样，获取文章作者元素
      Elements footEle = postItem.select(".post-item-foot a[class='post-item-author']");
      System.out.println("文章作者:" + footEle.text());
      System.out.println("作者主页:" + footEle.attr("href"));
      System.out.println("*********************************");
    }
  }

  // 极影电影网的科幻电影
  public static void ResolverHTML2(String url) {
    // 6.Jsoup解析html 解决乱码
    Document document = null;
    try {
      // 跳过SSL证书
      SslUtils.retrieveResponseFromServer(url);
      document =
          Jsoup.connect(url)
              .header(
                  "User-Agent",
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                      + " Chrome/107.0.0.0 Safari/537.36")
              .timeout(20000)
              .get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // 像js一样，通过id 获取文章列表元素对象
    Elements custom_background = document.getElementsByClass("custom-background");
    // 层层获取 获取页码数
    Elements container = custom_background.select(".b div[class=container]");
    Elements mainleft = container.select("div[class=mainleft]");
    Elements post_container = mainleft.select("ul[id=post_container]");
    Elements post_box_row_fixed_hight = post_container.select("li[class=post box row fixed-hight]");
    Elements thumbnail = post_box_row_fixed_hight.select("div[class=thumbnail]");
    String a_href = thumbnail.select("a").attr("href");

    // 获取下一页按钮
    Elements navigation_container = mainleft.select("div[class=navigation container]");
    Elements pagination = navigation_container.select("div[class=pagination]");
    // 总页数
    int pageNum = pagination.select("a").attr("href").length() - 2;
    // 下一页的URL
    Elements next = pagination.select("a[class=next]");
    String nextPageUrl = next.attr("href");
    // 像js一样，通过class 获取列表下的所有博客
    //        Elements postItems = postList.getElementsByClass("post-item");
    //        //循环处理每篇博客
    //        for (Element postItem : postItems) {
    //            //像jquery选择器一样，获取文章标题元素
    //            Elements titleEle = postItem.select(".post-item-body a[class='post-item-title']");
    //            Elements titleEle1 = postItem.select(".post-item-text
    // a[class='post-item-title']");
    //            System.out.println("文章标题:" + titleEle.text());
    //            System.out.println("文章地址:" + titleEle.attr("href"));
    //            //像jquery选择器一样，获取文章作者元素
    //            Elements footEle = postItem.select(".post-item-foot a[class='post-item-author']");
    //            System.out.println("文章作者:" + footEle.text());
    //            System.out.println("作者主页:" + footEle.attr("href"));
    //            System.out.println("*********************************");
    //            // 5秒抓取一条
    //            try {
    //                Thread.sleep(5000);
    //            } catch (InterruptedException e) {
    //                e.printStackTrace();
    //            }
    //        }
  }
}
