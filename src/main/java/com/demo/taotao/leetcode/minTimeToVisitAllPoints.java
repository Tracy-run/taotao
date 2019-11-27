package com.demo.taotao.leetcode;

/**
 *
 * 平面上有 n 个点，点的位置用整数坐标表示 points[i] = [xi, yi]。请你计算访问所有这些点需要的最小时间（以秒为单位）。
 *
 * 你可以按照下面的规则在平面上移动：
 *
 * 每一秒沿水平或者竖直方向移动一个单位长度，或者跨过对角线（可以看作在一秒内向水平和竖直方向各移动一个单位长度）。
 * 必须按照数组中出现的顺序来访问这些点。
 *
 *
 * points.length == n
 * 1 <= n <= 100
 * points[i].length == 2
 * -1000 <= points[i][0], points[i][1] <= 1000
 *
 */
public class minTimeToVisitAllPoints {


    public static void main(String[] args) {
        int[][] points = new int [10][2];
        points = new int[][]{{1,2},{4,5}};
        System.out.println(minTimeToVisitAllPoints(points));
    }


    public static int minTimeToVisitAllPoints(int[][] points) {
        int time = 0;
        for (int i = 0;i < points.length-1;i++) {
            int[] pointA = points[i];
            int[] pointB = points[i + 1];
            int offsetX = pointB[0] - pointA[0] >= 0 ? pointB[0] - pointA[0]:pointA[0] - pointB[0];
            int offsetY = pointB[1] - pointA[1] >= 0 ? pointB[1] - pointA[1]:pointA[1] - pointB[1];
            time += offsetX >= offsetY ? offsetX : offsetY;
        }
        return time;
    }

}
