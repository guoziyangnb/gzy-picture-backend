package com.yupi.yupicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin");


    private final String text;
    private final String value;

    UserRoleEnum(String text,String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 利用Map优化枚举类，避免获取一次枚举值就遍历一次枚举对象
     * 1.首先创造一个枚举类，然后依次填入
     */
    private static final Map<String ,UserRoleEnum> VALUE_MAP = new HashMap<>();

    static {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()){
            VALUE_MAP.put(roleEnum.value, roleEnum);
        }
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static UserRoleEnum getEnumByValue(String value) {
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
