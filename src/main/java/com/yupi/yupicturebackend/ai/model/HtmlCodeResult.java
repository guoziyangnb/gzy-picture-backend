package com.yupi.yupicturebackend.ai.model;

import lombok.Data;

/**
 * 生成 HTML 代码结果
 */
@Data
public class HtmlCodeResult {

    /**
     * HTML 代码
     */
    private String htmlCode;

    /**
     * 描述
     */
    private String description;
}