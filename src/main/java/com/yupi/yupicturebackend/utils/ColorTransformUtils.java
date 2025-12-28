package com.yupi.yupicturebackend.utils;

import java.awt.*;

/**
 * 颜色转换工具类
 */
public class ColorTransformUtils {

    private ColorTransformUtils() {
        // 工具类不需要实例化
    }

    /**
     * 将数据万象的颜色值转换为标准颜色字符串
     * @param color
     * @return
     */
    public static String getStandardColor(String color) {
        // 0x080e0 => 0x0800e0
        if (color.length() == 7){
            color = color.substring(0,4) + "0" + color.substring(4,7);
        }
        return color;
    }
}