package personal.idempotent.util;

import java.util.UUID;

/**
 * @author: lazecoding
 * @date: 2020/10/7 20:47
 * @description: 字符串工具类
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 获取字符串
     *
     * @param obj 输入
     * @return 字符串
     */
    public static String getString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 判断字符串是不是空串 (只有空格也是空穿)
     *
     * @param str 字符串
     * @return 是不是空串
     */
    public static boolean isEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是不是非空串
     *
     * @param str 字符串
     * @return 是不是非空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 获取 UUID字符串
     *
     * @return UUID字符串
     */
    public static String getUUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
