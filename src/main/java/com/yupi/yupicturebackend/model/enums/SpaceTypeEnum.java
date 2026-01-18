package com.yupi.yupicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 空间类型枚举
 */
@Getter
public enum SpaceTypeEnum {

    PRIVATE("私有空间", 0),
    TEAM("团队空间", 1);

    private final String text;

    private final int value;

    SpaceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    private static final Map<Integer ,SpaceTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (SpaceTypeEnum spaceTypeEnum : SpaceTypeEnum.values()){
            VALUE_MAP.put(spaceTypeEnum.value, spaceTypeEnum);
        }
    }

    /**
     * 根据 value 获取枚举
     */
    public static SpaceTypeEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        return VALUE_MAP.get(value);
    }
}