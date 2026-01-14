package com.yupi.yupicturebackend.model.dto.space.analyze;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 空间图片分类分析请求类
 */
@EqualsAndHashCode(callSuper = true) //EqualsAndHashCode注解的作用是自动生成equals()和hashCode()方法，
// callSuper表示是否调用父类的equals()和hashCode()方法，再判断子类的equals和hashCode方法
@Data //@Data注解的作用是自动生成@Getter和@Setter方法，包括@ToString、@EqualsAndHashCode、@RequiredArgsConstructor
public class SpaceCategoryAnalyzeRequest extends SpaceAnalyzeRequest {

}