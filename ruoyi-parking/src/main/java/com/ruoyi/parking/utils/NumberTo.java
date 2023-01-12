package com.ruoyi.parking.utils;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/11/15:12
 * @Description:
 */
public class NumberTo {
    public static String num2Ch(long num) {
        String[] convert = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"个", "十", "百", "千", "万", "亿"};
        if (num == 0) return convert[0];//0比较特殊
        //将数字分为4个一组处理
        Stack<String> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        int pre = -1;//上一个数字
        int count = 0;//记录当前处理了几个数字
        while (num > 0) {
            int digit = (int) (num % 10);
            num = num / 10;
            if (digit > 0 || (digit == 0 && pre != 0 && count > 0)) {//避免有多个零出现
                //添加单位,首先要满足不是0
                if (digit > 0 && count % 4 == 1) sb.append(unit[1]);
                if (digit > 0 && count % 4 == 2) sb.append(unit[2]);
                if (digit > 0 && count % 4 == 3) sb.append(unit[3]);
                sb.append(convert[digit]);
            }
            pre = digit;
            count++;
            if (count == 4 || count == 8 || num <= 0) {//如果处理满一组或不满一组，但已经处理完
                String s = sb.reverse().toString();
                if (s.startsWith("一十")) s = s.substring(1);//特殊情况: 10 不念一十 ，念十
                stack.push(s);
                count = 0;//下一组重新初始化
                pre = -1;
                sb.setLength(0);
            }
        }
        sb.setLength(0);//清空
        //对栈中的数据进行处理
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
            if (stack.size() == 2) {//为亿
                sb.append(unit[5]);
            } else if (stack.size() == 1 && sb.charAt(sb.length() - 1) != '亿') {//为万，特殊情况 一亿，后面不能加万了
                sb.append(unit[4]);
            }
        }
        return sb.toString();
    }

    @Test
    public void test() throws Exception {
        //阿拉伯数字转中文数字
        System.out.println(num2Ch(1));
        System.out.println(num2Ch(10));
        System.out.println(num2Ch(15));
        System.out.println(num2Ch(25));
        System.out.println(num2Ch(100));
        System.out.println(num2Ch(115));
        System.out.println(num2Ch(234));
        System.out.println(num2Ch(999));
        System.out.println(num2Ch(1501));
        System.out.println(num2Ch(1006));
        System.out.println(num2Ch(9905));
        System.out.println(num2Ch(20394));
        System.out.println(num2Ch(100001));
        System.out.println(num2Ch(100000));
        System.out.println(num2Ch(100101822));
        System.out.println(num2Ch(123412341234L));
        System.out.println(num2Ch(10000000000L));
    }

}
