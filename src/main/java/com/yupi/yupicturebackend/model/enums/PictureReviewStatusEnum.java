package com.yupi.yupicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片审核状态枚举
 */
@Getter
public enum PictureReviewStatusEnum{  
    REVIEWING("待审核", 0),  
    PASS("通过", 1),  
    REJECT("拒绝", 2);  
  
    private final String text;  
    private final int value;  
  
    PictureReviewStatusEnum(String text, int value) {
        this.text = text;  
        this.value = value;  
    }

    /**
     * 利用Map优化枚举类，避免获取一次枚举值就遍历一次枚举对象
     * 1.首先创造一个枚举类，然后依次填入
     */
    private static final Map<Integer,PictureReviewStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (PictureReviewStatusEnum pictureReviewStatusEnum : PictureReviewStatusEnum.values()){
            VALUE_MAP.put(pictureReviewStatusEnum.value, pictureReviewStatusEnum);
        }
    }
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static PictureReviewStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;  
        }
//        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
//            if (anEnum.value.equals(value)) {
//                return anEnum;
//            }
//        }
//        return null;
        /**
         * 优化
         * 2.之后返回枚举值
         */
        return VALUE_MAP.get(value);
    }  
}