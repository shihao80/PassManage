package com.childpassmanage.net.utils;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: CommonUtils
 * @Description: 通用工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2017年2月22日 上午11:46:44
 */
public class CommonUtils {


    /**
     * @param date    日期
     * @param pattern 模式 如:yyyyMMdd等
     * @return
     * @Title: formatDate
     * @Description: 格式化日期
     * @since: 0.0.1
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * @param strDate String类型日期
     * @param pattern 日期显示模式
     * @return
     * @Title: parseDate
     * @Description: 将String日期转换为Date类型日期
     * @since: 0.0.1
     */
    public static Date parseDate(String strDate, String pattern) {
        SimpleDateFormat formatter = null;
        if (StringUtils.isEmpty(strDate) || strDate.equals("")) {
            return null;
        }
        formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param date   操作前的日期
     * @param field  日期的部分如:年,月,日
     * @param amount 增加或减少的值(负数表示减少)
     * @return
     * @Title: dateAdd
     * @Description: 日期的加减操作
     * @since: 0.0.1
     */
    public static Date dateAdd(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * @param source 源字符串
     * @param offset 填充开始的位置, 0-在左边, source.getBytes().length 在右边, 如果有中文时需小心位置
     * @param c      用于填充的字符
     * @param length 最后字符串的字节长度
     * @return
     * @Title: fill
     * @Description: 填充字符串, 长度是按字节计算, 不是字符
     * @since: 0.0.1
     */
    public static String fill(String source, int offset, char c, int length) throws UnsupportedEncodingException {
        if (null == source) {
            source = "";
        }
        if (source.getBytes("utf-8").length == length) {
            return source;
        }
        byte[] buf = new byte[length];
        byte[] src = source.getBytes("utf-8");
        if (src.length > length) {
            System.arraycopy(src, src.length - length, buf, 0, length);
            return new String(buf, "utf-8");
        }
        if (offset > src.length) {
            offset = src.length;
        } else if (offset < 0) {
            offset = 0;
        }
        int n = length - src.length;

        System.arraycopy(src, 0, buf, 0, offset);
        for (int i = 0; i < n; i++) {
            buf[i + offset] = (byte) c;
        }
        System.arraycopy(src, offset, buf, offset + n, src.length - offset);
        return new String(buf, "utf-8");
    }

    /**
     * @param original 原字符串
     * @param offset   填充开始的位置, 0-在左边, original.getBytes().length 在右边, 如果有中文时需小心位置
     * @param length   替换的字节数
     * @param c        用于替换的字符
     * @return
     * @Title: replace
     * @Description: 替换字符串, 长度是按字节计算, 不是字符
     * @since: 0.0.1
     */
    public static String replace(String original, int offset, int length, char c) throws UnsupportedEncodingException {
        if (original == null) {
            original = "";
        }
        if (original.getBytes("utf-8").length <= offset) {
            return original;
        }
        if (original.getBytes("utf-8").length < offset + length) {
            length = original.getBytes("utf-8").length - offset;
        }
        byte[] buf = new byte[original.length()];
        byte[] src = original.getBytes("utf-8");
        System.arraycopy(src, 0, buf, 0, offset);

        for (int i = offset; i < offset + length; i++) {
            buf[i] = (byte) c;
        }
        System.arraycopy(src, offset + length, buf, offset + length, src.length - offset - length);
        return new String(buf, "utf-8");
    }

    /**
     * @param s 16进制字符串
     * @return
     * @Title: hexToByte
     * @Description: 16进制字符串转字节数组
     * @since: 0.0.1
     */
    public static byte[] hexToByte(String s) {
        byte[] result = null;
        try {
            int i = s.length();
//            if (i % 2 == 1) {
//                throw new Exception("字符串长度不是偶数.");
//            }
            if (i % 2 != 0) {
                throw new Exception("字符串长度不是偶数.");
            }
            result = new byte[i / 2];
            for (int j = 0; j < result.length; j++) {
                result[j] = (byte) Integer.parseInt(s.substring(j * 2, j * 2 + 2), 16);
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
//            log.error("16进制字符串转字节数组时出现异常:", e);
        }
        return result;
    }

    /**
     * @param bytes 字节数组
     * @return
     * @Title: byte2hexString
     * @Description: 字节数组转换为16进制字符串    //0x33 0xD2 0x00 0x46 转换为 "33d20046" 转换和打印报文用
     * @since: 0.0.1
     */
    public static String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * @param hexString 16进制字符串    如:"33d20046" 转换为 0x33 0xD2 0x00 0x46
     * @return
     * @Title: hexString2byte
     * @Description: 16进制字符串转字节数组
     * @since: 0.0.1
     */
    public static byte[] hexString2byte(String hexString) {
        if (null == hexString || hexString.length() % 2 != 0 || hexString.contains("null")) {
            return null;
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = (byte) (Integer.parseInt(hexString.substring(i, i + 2), 16) & 0xff);
        }
        return bytes;
    }

    /**
     * @param i 需要转的int类型数字
     * @return
     * @Title: byte1ToBcd2
     * @Description: int类型转BCD码
     * @since: 0.0.1
     */
    public static String byte1ToBcd2(int i) {
//        return (new Integer(i / 16).toString() + (new Integer(i % 16)).toString());
        return Integer.toString(i / 16) + Integer.toString(i % 16);
    }

    /**
     * @param b 字节数组
     * @return
     * @Title: byteToHex2
     * @Description: 字节数组转换为16进制字符串        For example, byte[] {0x01,0x23,0x45,0x67,0x89,0xAB,0xCD,0xEF} will be changed to String "0123456789ABCDEF"
     * @since: 0.0.1
     */
    public static String byteToHex2(byte[] b) {
        StringBuffer result = new StringBuffer();
        String tmp = "";

        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0xff);
            if (tmp.length() == 1) {
                result.append("0" + tmp);
            } else {
                result.append(tmp);
            }
        }
        return result.toString().toUpperCase();
    }

    /**
     * @param num 数字
     * @param len 字节数组长度
     * @return
     * @Title: intToHexBytes
     * @Description: int类型转16进制字节数组
     */
    public static byte[] intToHexBytes(int num, int len) {
        byte[] bytes = null;
        String hexString = Integer.toHexString(num);
        if (len > 0) {
            int length = len * 2;
            hexString = leftFill(hexString, '0', length);
            bytes = CommonUtils.hexString2byte(hexString);
        }
        return bytes;
    }

    private static String leftFill(String hexString, char c, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        if(hexString.length() < length){
            for(int count = 0; count <length-hexString.length();count++){
                stringBuilder.append(c);
            }
        }
        return stringBuilder.append(hexString).toString();
    }

    /*public static String byteToHex3(byte[] b) {
        String result = "";
        String tmp = "";

        for (int n = 0; n < b.length; n++) {
            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                result = result + "0" + tmp;
            } else {
                result = result + tmp;
            }
            if (n < b.length - 1) {
                result = result + "";
            }
        }
        return result.toUpperCase();
    }*/

    /**
     * @param str 需要转换编码的字符串
     * @return
     * @Title: iso2Gbk
     * @Description: 将ISO-8859-1编码的字符串转成GBK编码的字符串
     * @since: 0.0.1
     */
    public static String iso2Gbk(String str) {
        if (null == str) {
            return str;
        }
        try {
            return new String(str.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
//            log.error("不支持的编码异常:", e);
            e.printStackTrace();
            return str;
        }
    }

//    /**
//     * @param message
//     * @return
//     * @Title: getSubElement
//     * @Description: 分解各子域到HashMap
//     * @since: 0.0.1
//     */
//    public static Map<String, String> getSubElement(byte[] message) {
//        Map<String, String> map = new HashMap<String, String>();
//        String key = null;
//        String value = null;
//        int len = 0;
//        int idx = 0;
//        while (idx < message.length) {
//            key = new String(message, idx, 2);
//            idx += 2;    //取了SE id 移2位
//            len = Integer.parseInt(new String(message, idx, 2));
//            idx += 2;    //取了SE id的内容长度  移2位
//            value = new String(message, idx, len);
//            map.put(key, value);
//            idx += len;
//        }
//        return map;
//    }

    //byte数组转成long

    /**
     * @param b 将字节数组转long类型 位置为小端
     * @return
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * @param b 将字节数组转int类型     位置为小端
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;

        s = s0 | s1 | s2 | s3;
        return s;
    }

    /**
     * int类型转换小端的byte数组
     * @param i
     * @return
     */
    public static byte[] intToLittleBytes(int i) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(i);
        byte[] littleBytes = byteBuffer.array();
        return littleBytes;
    }

    /**
     * 将一个字节转成10进制
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        int value = b & 0xff;
        return value;
    }

    /**
     *  字节数组合并
     * @param bt1   字节数组bt1
     * @param bt2   字节数组bt2
     * @return
     */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

}