package com.yupi.yupicturebackend.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.yupi.yupicturebackend.config.CosClientConfig;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import com.yupi.yupicturebackend.manager.CosManager;
import com.yupi.yupicturebackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 图片上传模板 抽象类  --> 模板设计   通用的流程，一些小细节不一样，抽象成抽象类，然后子类继承后实现不同的方法
 */
@Slf4j
public abstract class PictureUploadTemplate {  
  
    @Resource
    protected CosManager cosManager;
  
    @Resource  
    protected CosClientConfig cosClientConfig;

    /**
     * 模板方法，定义上传流程
     * @param inputSource  输入源
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // ? 1. 校验图片
        validPicture(inputSource);  
  
        // 2. 图片上传地址  
        String uuid = RandomUtil.randomString(16);
        // ? 获取文件名字
        String originFilename = getOriginFilename(inputSource);
        //更改文件名称
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        //不同的文件夹上传不同的文件
        String projectName = "/gzyPicture"; //不同项目上传
        String uploadPath = String.format(projectName + "/%s/%s", uploadPathPrefix, uploadFilename);
  
        File file = null;
        try {  
            // 3. 创建临时文件  
            file = File.createTempFile(uploadPath, null);  
            // ? 处理文件来源（本地或 URL）
            processFile(inputSource, file);  
  
            // 4. 上传图片到对象存储  
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            // 获取图片信息对象
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();

            // 获取图片处理结果
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                // 获取压缩后图片的信心
                CIObject compressedCiObject = objectList.get(0);
                // 缩略图默认等于压缩图
                CIObject thumbnailObject = compressedCiObject;
                // 有生成的缩略图的话才获取
                if (objectList.size() > 1){
                    // 获取缩略图的信息
                    thumbnailObject = objectList.get(1);
                }
                // 封装返回结果
                return buildResult(originFilename, compressedCiObject, thumbnailObject);
            }

            // 5. 封装返回结果  
            return buildResult(originFilename, file, uploadPath, imageInfo);  
        } catch (Exception e) {  
            log.error("图片上传到对象存储失败", e);  
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片上传到对象存储失败");
        } finally {  
            // 6. 清理临时文件  
            deleteTempFile(file);  
        }  
    }

    /**
     * 封装返回结果
     *
     * @param originFilename 原始文件名
     * @param compressedCiObject 压缩后的对象
     * @param thumbnailObject 缩略图对象
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject, CIObject thumbnailObject) {
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();

        // 封装并返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        // 设置压缩图路径地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey()); //设置可访问的在线地址
        // 设置缩略图地址
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailObject.getKey()); //设置可访问的在线地址
        return uploadPictureResult;
    }

    /**  
     * 校验输入源（本地文件或 URL）  
     */  
    protected abstract void validPicture(Object inputSource);  
  
    /**  
     * 获取输入源的原始文件名  
     */  
    protected abstract String getOriginFilename(Object inputSource);  
  
    /**  
     * 处理输入源并生成本地临时文件  
     */  
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 封装webp返回结果
      * @param originFilename
     * @param file
     * @param uploadPath
     * @param imageInfo 对象图片存储的信息
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {  
        UploadPictureResult uploadPictureResult = new UploadPictureResult();  
        int picWidth = imageInfo.getWidth();  
        int picHeight = imageInfo.getHeight();  
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));  
        uploadPictureResult.setPicWidth(picWidth);  
        uploadPictureResult.setPicHeight(picHeight);  
        uploadPictureResult.setPicScale(picScale);  
        uploadPictureResult.setPicFormat(imageInfo.getFormat());  
        uploadPictureResult.setPicSize(FileUtil.size(file));  
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath); //设置可访问的在线地址
        return uploadPictureResult;  
    }  
  
    /**  
     * 删除临时文件  
     */  
    public void deleteTempFile(File file) {  
        if (file == null) {  
            return;  
        }  
        boolean deleteResult = file.delete();  
        if (!deleteResult) {  
            log.error("file delete error, filepath = {}", file.getAbsolutePath());  
        }  
    }  
}