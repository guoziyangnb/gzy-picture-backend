package com.yupi.yupicturebackend.api.imagesearch.baidu.sub;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取以图搜图页面地址（step1）
 */
@Slf4j
public class GetImagePageUrlApi {

    /**
     * 获取图片页面地址
     * ? 原网址在 https://graph.baidu.com/pcpage/index?tpl_from=pc
     * @param imageUrl
     * @return
     */
    public static String getImagePageUrl(String imageUrl) {
        // 1. 准备请求参数
        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 这个Acs_Token从请求头复制，至于怎么获得的之后再说
        String Acs_Token = "1766812755158_1766849973302_vZT6z9Br6+AFprJpDrV6murnxNM4tBo4FBwvQ37H9WC3srOQGfesm2uRPksORKH7Djv+fzrWP5RU2xAP9ly8LdPJ4+PlZFwOoL3Le0Ku4g0XqaRScI+A1HwAxJn7YjehzmH13pPhU5vr2keNn7sKXb7IRymuCfomMGEycATsufVQTMERUZns0zRolyGC7lc7K/j4N7MJkgQmjZwGn0WRvNxYut+4D969lLODAS2YMLLRyPaW5CQnWrOb3T5YU3+QX4h929WLeAG+ElbrUg6abl4scd+pxyXntlB/mB1T9751ZP4HhNUCfJ4Lp6rFVYQf3hybTjNVqmHeUPgIsoP2Xo5toR6COLbVdJyw102ztRFq3tlBKM1QAJ5rh5NnVJciLd07bvQQer2XzTVqRGy+CvSYhDNCEmOcJDny8tyfikuChhIS3dN87/cW6T4d8VX5";
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        // 请求地址
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;

        try {
            // 2. 发送 POST 请求到百度接口
            HttpResponse response = HttpRequest.post(url)
                    .header("Acs-Token",Acs_Token)
                    .form(formData)
                    .timeout(5000)
                    .execute();
            // 判断响应状态
            if (response.getStatus() != HttpStatus.HTTP_OK) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            // 解析响应
            String responseBody = response.body();
            Map<String, Object> result = JSONUtil.toBean(responseBody, Map.class);

            // 3. 处理响应结果
            if (result == null || !Integer.valueOf(0).equals(result.get("status"))) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            String rawUrl = (String) data.get("url");
            // 对 URL 进行解码
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            // 如果 URL 为空
            if (StrUtil.isBlank(searchResultUrl)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效结果");
            }
            return searchResultUrl;
        } catch (Exception e) {
            log.error("搜索失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://cdn.svipaigc.com/bizi/2025/05/59d76c.jpg";
        String result = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + result);
    }
}