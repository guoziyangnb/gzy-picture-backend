package com.yupi.yupicturebackend.model.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor //表示接受所有参数的初始化函数
public class SpaceLevel {

    /**
     * 值
     */
    private int value;

    /**
     * 中文值
     */
    private String text;

    /**
     * 最大数量
     */
    private long maxCount;

    /**
     * 最大容量
     */
    private long maxSize;
}