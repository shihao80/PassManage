package com.childpassmanage.net.utils;

import org.apache.commons.lang.math.RandomUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Shamir {
    public static Map<String, Long> split(int secret) {
        //Scanner in = new Scanner(System.in);
        long a[] = new long[100];// 放系数
        long f[] = new long[100];// 放密钥的值
        long dian[] = new long[100];// 放密钥的点
        Map<String, Long> hashMap = new HashMap<>();
        Random rand = new Random();
        int bit_num = rand.nextInt(29) + 2;// 随机2到31位
        BigInteger q = BigInteger.probablePrime(bit_num, rand);// 生成一个随机大素数,必须大于输入的随机整数
        long p = q.longValue();
        while (p < secret) {// 如果不大于输入的数
            bit_num = rand.nextInt(29) + 2;
            q = BigInteger.probablePrime(bit_num, rand);
            p = q.longValue();
        }
        hashMap.put("p", p);
        int k = 2;
        int zi_key = 3;
        for (int i = 0; i < k - 1; i++) {
            a[i] = (long) (Math.random() * p);// 生成伪随机系数
            if (a[i] == 0) {// 生成的随机系数不能为0
                i--;
            }
        }
        for (int i = 0; i < zi_key; i++) {
            dian[i] = (long) (Math.random() * 100);// 生成100以内随机点,不能重复，如果大了，可能在以后的运算中超出数据类型范围，导致错误
            if (dian[i] == 0) {// 生成的随机点不能为0
                i--;
            }
        }
        for (int i = 0; i < zi_key; i++) {// 计算zi_key个子密钥
            f[i] = secret;
            for (int j = 0; j < k - 1; j++) {// 一个子密钥
                long zhishu = j + 1;
                f[i] = Math.floorMod((long) (a[j] * Math.pow(dian[i], zhishu)) + f[i], p);
            }
        }

        hashMap.put("dian[0]", dian[0]);
        hashMap.put("dian[1]", dian[1]);
        hashMap.put("dian[2]", dian[2]);
        hashMap.put("f[0]", f[0]);
        hashMap.put("f[1]", f[1]);
        hashMap.put("f[2]", f[2]);

        return hashMap;
    }


    public static long combine(Map<String, Long> hashMap) {
        long m = (hashMap.get("p"));
        int k = 2;
        long key_d[] = new long[k];
        long key_z[] = new long[k];
        long num_x[] = new long[k];
        long num_s[] = new long[k];
        long s = 0;

        int flag = RandomUtil.getRandomRange(3, 1);
        if (flag == 1) {
            key_d[0] = (hashMap.get("dian[0]"));
            key_d[1] = (hashMap.get("dian[1]"));
            key_z[0] = (hashMap.get("f[0]"));
            key_z[1] = (hashMap.get("f[1]"));
        } else if(flag == 2){
            key_d[0] = (hashMap.get("dian[0]"));
            key_d[1] = (hashMap.get("dian[2]"));
            key_z[0] = (hashMap.get("f[0]"));
            key_z[1] = (hashMap.get("f[2]"));
        }else if(flag == 3){
            key_d[0] = (hashMap.get("dian[1]"));
            key_d[1] = (hashMap.get("dian[2]"));
            key_z[0] = (hashMap.get("f[1]"));
            key_z[1] = (hashMap.get("f[2]"));
        }

        for (int i = 0; i < k; i++) {
            num_x[i] = 1;
            num_s[i] = 1;
            for (int j = 0; j < k; j++) {
                if (j == i) {
                    j++;
                }
                if (j == k) {
                    break;
                }
                num_x[i] = num_x[i] * (key_d[i] - key_d[j]);
                num_s[i] = (-key_d[j]) * num_s[i];
            }
            num_x[i] = qiu_ni(num_x[i], m);
            s = num_x[i] * num_s[i] * key_z[i] + s;
        }
        s = Math.floorMod(s, m);
        return s;
    }

    // 求逆函数
    static long qiu_ni(long a, long b) {// 最后s[1]为s即逆元,t[1]为t
        long s[] = { 1, 0, 0 };
        long t[] = { 0, 1, 0 };
        long r1 = a;
        long r2 = b;
        long tmp;
        int i = (int) (r1 / r2);
        tmp = r2;
        r2 = Math.floorMod(r1, r2);
        r1 = tmp;
        while (r2 != 0) {
            s[2] = s[0] - i * s[1];
            t[2] = t[0] - i * t[1];
            s[0] = s[1];
            s[1] = s[2];
            t[0] = t[1];
            t[1] = t[2];
            if (r2 != 0)
                i = (int) (r1 / r2);
            tmp = r2;
            r2 = Math.floorMod(r1, r2);
            r1 = tmp;
        }
        return s[1];
    }


/*
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("分解秘密请选择1，还原秘密选择2, 退出选择3");
            int flag = in.nextInt();
            if (flag == 1) {
                long a[] = new long[100];// 放系数
                long f[] = new long[100];// 放密钥的值
                long dian[] = new long[100];// 放密钥的点
                System.out.println("输入你的秘密");
                int secret = in.nextInt();// 输入为一个数小于2,147,483,647
                Random rand = new Random();
                int bit_num = rand.nextInt(29) + 2;// 随机2到31位
                BigInteger q = BigInteger.probablePrime(bit_num, rand);// 生成一个随机大素数,必须大于输入的随机整数
                long p = q.longValue();
                while (p < secret) {// 如果不大于输入的数
                    bit_num = rand.nextInt(29) + 2;
                    q = BigInteger.probablePrime(bit_num, rand);
                    p = q.longValue();
                }
                System.out.println("模为  " + p);
                //System.out.println("输入门限值");// 门限值小于等于100
                int k = 2;
                //System.out.println("输入得到几个子密钥");// 子密钥值小于等于100
                int zi_key = 3;
                for (int i = 0; i < k - 1; i++) {
                    a[i] = (long) (Math.random() * p);// 生成伪随机系数
                    if (a[i] == 0) {// 生成的随机系数不能为0
                        i--;
                    }
                }
                for (int i = 0; i < zi_key; i++) {
                    dian[i] = (long) (Math.random() * 100);// 生成100以内随机点,不能重复，如果大了，可能在以后的运算中超出数据类型范围，导致错误
                    if (dian[i] == 0) {// 生成的随机点不能为0
                        i--;
                    }
                }
                System.out.println("子密钥为(确保子密钥没有重复，如果有请重新生成)   ");
                for (int i = 0; i < zi_key; i++) {// 计算zi_key个子密钥
                    f[i] = secret;
                    for (int j = 0; j < k - 1; j++) {// 一个子密钥
                        long zhishu = j + 1;
                        f[i] = Math.floorMod((long) (a[j] * Math.pow(dian[i], zhishu)) + f[i], p);
                    }
                    System.out.print(dian[i] + " ");
                    System.out.println(f[i]);
                }
            } else if (flag == 2) {
                System.out.println("输入模值");
                int m = in.nextInt();
                System.out.println("输入门限");
                int k = in.nextInt();
                long key_d[] = new long[k];
                long key_z[] = new long[k];
                long num_x[] = new long[k];
                long num_s[] = new long[k];
                long s = 0;
                System.out.println("请输入密钥(先输入所有的点，再输入各点对应的值) ");
                for (int i = 0; i < k; i++) {
                    key_d[i] = in.nextLong();
                }
                for (int i = 0; i < k; i++) {
                    key_z[i] = in.nextLong();
                }
                for (int i = 0; i < k; i++) {
                    num_x[i] = 1;
                    num_s[i] = 1;
                    for (int j = 0; j < k; j++) {
                        if (j == i) {
                            j++;
                        }
                        if (j == k) {
                            break;
                        }
                        num_x[i] = num_x[i] * (key_d[i] - key_d[j]);
                        num_s[i] = (-key_d[j]) * num_s[i];
                    }
                    num_x[i] = qiu_ni(num_x[i], m);
                    s = num_x[i] * num_s[i] * key_z[i] + s;
                }
                s = Math.floorMod(s, m);
                System.out.println("秘密为 " + s);
            } else {
                break;
            }
        }
    }
*/

}
