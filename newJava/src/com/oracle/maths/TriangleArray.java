package com.oracle.maths;

/**
 * 杨辉三角
 * http://baike.baidu.com/item/%E6%9D%A8%E8%BE%89%E4%B8%89%E8%A7%92#4_8  百度百科
 * @author Administrator
 *
 */
public class TriangleArray {

    public static void main(String[] args) {

        final int NMAX = 10;
        int[][] odds = new int[NMAX + 1][];
        for (int n = 0; n <= NMAX; n++) {
            odds[n] = new int[n + 1];
        }

        for (int n = 0; n < odds.length; n++) {
            for (int k = 0; k < odds[n].length; k++) {
                int lotteryOdds = 1;
                for (int i = 1; i <= k; i++) {
                    lotteryOdds = lotteryOdds * (n - i + 1) / i;
                }
                odds[n][k] = lotteryOdds;
            }
        }
        for (int[] row : odds) {
            for (int odd : row) {
                System.out.printf("%4d", odd);
            }
            System.out.println();
        }
    }

}
