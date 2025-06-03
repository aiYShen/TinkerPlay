package com.aiyuns.tinkerplay.Algorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: aiYunS @Date: 2022-10-11 下午 02:57 @Description: 贪心算法 * 思路分析 *
 * （1）使用穷举法，列出每个可能广播台集合，这被称为幂集。 * （2）假设有n个广播台，则广播台的组合共有2^n-1个，假设每秒可以计算10个子集 * 广播台数量 子集总数 需要的时间 * 5
 * 32 3.2秒 * 10 1024 102.4秒 * ... * * 案例：集合覆盖问题 * 假设存在下面需要付费的广播台，以及广播信号可以覆盖的地区，如何选择 *
 * 最少的广播台，让所有的地区都可以接收信息 * 广播台 覆盖地区 * K1 "北京","上海","天津" * K2 "广州","北京","深圳" * K3 "成都","上海","杭州" * K4
 * "上海","天津" * K5 "杭州","大连"
 */
public class GreedyAlgorithm {

  public static void main(String[] args) {

    // 广播电台
    Map<String, Set<String>> broadcasts = new HashMap<>();
    broadcasts.put(
        "K1", Arrays.stream(new String[] {"北京", "上海", "天津"}).collect(Collectors.toSet()));
    broadcasts.put(
        "K2", Arrays.stream(new String[] {"广州", "北京", "深圳"}).collect(Collectors.toSet()));
    broadcasts.put(
        "K3", Arrays.stream(new String[] {"成都", "上海", "杭州"}).collect(Collectors.toSet()));
    broadcasts.put("K4", Arrays.stream(new String[] {"上海", "天津"}).collect(Collectors.toSet()));
    broadcasts.put("K5", Arrays.stream(new String[] {"杭州", "大连"}).collect(Collectors.toSet()));
    // [上海, 天津, 北京, 广州, 深圳, 成都, 杭州, 大连]
    // 表示所有需要覆盖的地区
    List<String> allAreas =
        broadcasts.values().stream()
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    System.out.println("allAreas=" + allAreas);
    // 选择的地区集合
    List<String> selects = new ArrayList<>();
    // 定义一个临时的集合，在遍历过程中，存放遍历过程中的电台覆盖的地区和当前还没有覆盖的地区的交集
    Set<String> tempSet = new HashSet<>();
    // 最大的电台，保存在一次遍历过程中，能够覆盖最大未覆盖的地区对应的电台key
    String maxKey;
    while (allAreas.size() != 0) {
      // 置空
      maxKey = null;
      // 遍历broadcasts，取出对应key
      for (String key : broadcasts.keySet()) {
        tempSet.clear(); // 清空
        Set<String> areas = broadcasts.get(key);
        tempSet.addAll(areas);
        // tempSet = tempSet与allAreas的交集
        tempSet.retainAll(allAreas);
        if (tempSet.size() > 0
            && (maxKey == null || tempSet.size() > broadcasts.get(maxKey).size())) {
          maxKey = key;
        }
      }
      if (maxKey != null) {
        selects.add(maxKey);
        // 将maxKey指向的广播电台覆盖地区，从allAreas去掉
        System.out.println("maxKey=" + maxKey);
        allAreas.removeAll(broadcasts.get(maxKey));
      }
    }
    System.out.println("得到的选择结果是：" + selects);
  }
}
