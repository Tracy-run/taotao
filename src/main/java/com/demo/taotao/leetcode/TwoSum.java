package com.demo.taotao.leetcode;

import java.util.HashMap;

/**
 * nums = [2, 7, 11, 15], target = 9
 *
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 *
 */
public class TwoSum {


    public static void main(String[] args) {

        int [] nums = new int [4];
        nums[0] = 2;
        nums[1] = 6;
        nums[2] = 11;
        nums[3] = 7;

        //int rev [] = twoSum(nums,9);
        //System.out.println(rev[0]+ "  " +rev[1]);

        int [] rev  = twoSum1(nums,9);
        System.out.println(rev[0]+ "  " +rev[1]);
    }




    public static int[] twoSum(int[] nums, int target) {

        int indexMin = -1;
        int indexMax = -1;
        a: for(int i =0;i<nums.length ;i++){

           int temp = nums[i];
           int min = target - temp;

           for(int j=i;j<nums.length;j++){
                if(String.valueOf(min).equals(String.valueOf(nums[j]))){
                    indexMin = i;
                    indexMax = j;
                    break a;
                }
           }
        }
        int [] result = new int[2];
        if(indexMax != -1 && indexMin != -1){
            result[0] = indexMin;
            result[1] = indexMax;
        }
        return result;
    }



    public static int[] twoSum1(int[] nums, int target) {
        int[] indexs = new int[2];

        // 建立k-v ，一一对应的哈希表
        HashMap<Integer,Integer> hash = new HashMap<Integer,Integer>();
        for(int i = 0; i < nums.length; i++){
            if(hash.containsKey(nums[i])){
                indexs[0] = i;
                indexs[1] = hash.get(nums[i]);
                return indexs;
            }
            // 将数据存入 key为补数 ，value为下标
            hash.put(target-nums[i],i);
        }
        return indexs;
    }
}
