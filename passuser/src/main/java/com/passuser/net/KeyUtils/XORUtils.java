package com.passuser.net.KeyUtils;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 异或(xor)算法加密/解密工具
 *
 * @author xietansheng
 */
public class XORUtils {

    /**
     * 异或算法加密/解密
     *
     * @param data 数据（密文/明文）
     * @param key 密钥
     * @return 返回解密/加密后的数据
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return data;
        }

        byte[] result = new byte[data.length];

        // 使用密钥字节数组循环加密或解密
        for (int i = 0; i < data.length; i++) {
            // 数据与密钥异或, 再与循环变量的低8位异或（增加复杂度）
            result[i] = (byte) (data[i] ^ key[i % key.length] ^ (i & 0xFF));
        }

        return result;
    }

    /**
     * 对文件异或算法加密/解密
     *
     * @param inFile 输入文件（密文/明文）
     * @param outFile 结果输出文件
     * @param key 密钥
     */
    public static void encryptFile(File inFile, File outFile, byte[] key) throws Exception {
        InputStream in = null;
        OutputStream out = null;

        try {
            // 文件输入流
            in = new FileInputStream(inFile);
            // 结果输出流, 异或运算时, 字节是一个一个读取和写入, 这里必须使用缓冲流包装,
            // 等缓冲到一定数量的字节（10240字节）后再写入磁盘（否则写磁盘次数太多, 速度会非常慢）
            out = new BufferedOutputStream(new FileOutputStream(outFile), 10240);

            int b = -1;
            long i = 0;

            // 每次循环读取文件的一个字节, 使用密钥字节数组循环加密或解密
            while ((b = in.read()) != -1) {
                // 数据与密钥异或, 再与循环变量的低8位异或（增加复杂度）
                b = (b ^ key[(int) (i % key.length)] ^ (int) (i & 0xFF));
                // 写入一个加密/解密后的字节
                out.write(b);
                // 循环变量递增
                i++;
            }
            out.flush();

        } finally {
            close(in);
            close(out);
        }
    }

    private static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // nothing
            }
        }
    }

}

