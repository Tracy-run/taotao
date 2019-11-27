package com.demo.taotao.leetcode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*

给你一个产品数组 products 和一个字符串 searchWord ，products  数组中每个产品都是一个字符串。

请你设计一个推荐系统，在依次输入单词 searchWord 的每一个字母后，推荐 products 数组中前缀与 searchWord 相同的最多三个产品。

如果前缀相同的可推荐产品超过三个，请按字典序返回最小的三个。

请你以二维列表的形式，返回在输入 searchWord 每个字母后相应的推荐产品的列表。



*/
public class suggestedProducts {

    public static void main(String[] args) {

        String [] pro = new String[]{"mobile","mouse","moneypot","monitor","mousepad"};

        String searchWord = "mouse";

        System.out.println(suggestedProduct(pro,searchWord));
    }


    public static List<List<String>> suggestedProduct(String[] products, String searchWord) {
        Arrays.sort(products);
        List<List<String>> res = new LinkedList<>();
        for (int i = 1; i <= searchWord.length(); i++) {
            String prefix = searchWord.substring(0, i);
            List<String> collector = new LinkedList<>();
            for (String product : products) {
                if (product.startsWith(prefix)) {
                    collector.add(product);
                    if (collector.size() == 3) {
                        break;
                    }
                }
            }
            res.add(collector);
        }
        return res;
    }
}
