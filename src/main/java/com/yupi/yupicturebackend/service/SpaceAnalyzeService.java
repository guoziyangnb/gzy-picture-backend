package com.yupi.yupicturebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupicturebackend.model.dto.space.analyze.SpaceCategoryAnalyzeRequest;
import com.yupi.yupicturebackend.model.dto.space.analyze.SpaceTagAnalyzeRequest;
import com.yupi.yupicturebackend.model.dto.space.analyze.SpaceUsageAnalyzeRequest;
import com.yupi.yupicturebackend.model.entity.Space;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.model.vo.analyze.SpaceCategoryAnalyzeResponse;
import com.yupi.yupicturebackend.model.vo.analyze.SpaceTagAnalyzeResponse;
import com.yupi.yupicturebackend.model.vo.analyze.SpaceUsageAnalyzeResponse;

import java.util.List;

/**
* @author lenovo
* @createDate 2025-11-09 23:32:43
*/
public interface SpaceAnalyzeService extends IService<Space> {
    /**
     * 空间使用情况接口
     * @param spaceUsageAnalyzeRequest
     * @param loginUser
     * @return
     */
    SpaceUsageAnalyzeResponse getSpaceUsageAnalyze(SpaceUsageAnalyzeRequest spaceUsageAnalyzeRequest, User loginUser);

    /**
     * 空间分类统计接口
     * @param spaceCategoryAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceCategoryAnalyzeResponse> getSpaceCategoryAnalyze(SpaceCategoryAnalyzeRequest spaceCategoryAnalyzeRequest, User loginUser);

    /**
     * 空间标签分析接口
     * @param spaceTagAnalyzeRequest
     * @param loginUser
     * @return
     */
    List<SpaceTagAnalyzeResponse> getSpaceTagAnalyze(SpaceTagAnalyzeRequest spaceTagAnalyzeRequest, User loginUser);
}
