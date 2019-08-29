package com.example.mybase.utils;

import android.app.Activity;
import android.content.Context;

import com.example.mybase.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * Created by 小智
 * on 2017/12/19
 * 描述：
 */

public class BCPhotoUtils {
    /**
     * 圆形图片裁剪
     *
     * @param context
     */
    public static void initCircle(Context context) {
        PictureSelector.create((Activity) context)
                       .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                       .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                       .maxSelectNum(1)// 最大图片选择数量
                       .minSelectNum(1)// 最小选择数量
                       .imageSpanCount(4)// 每行显示个数
                       .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                       .previewImage(true)// 是否可预览图片
                       .previewVideo(false)// 是否可预览视频
                       .enablePreviewAudio(false) // 是否可播放音频
                       .isCamera(true)// 是否显示拍照按钮
                       .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                       .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                       .enableCrop(true)// 是否裁剪
                       .compress(true)// 是否压缩
                       .synOrAsy(true)//同步true或异步false 压缩 默认同步
                       .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                       .isGif(false)// 是否显示gif图片
                       .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                       .circleDimmedLayer(true)// 是否圆形裁剪
                       .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                       .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                       .openClickSound(false)// 是否开启点击声音
                       .minimumCompressSize(100)// 小于100kb的图片不压缩
                       .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 非圆形裁剪
     *
     * @param context
     * @param maxNum
     */
    public static void initRectangle(Context context, int maxNum) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create((Activity) context)
                       .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                       .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                       .maxSelectNum(maxNum)// 最大图片选择数量 int
                       .minSelectNum(1)// 最小选择数量 int
                       .imageSpanCount(4)// 每行显示个数 int
                       .openClickSound(false)
                       .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                       .previewImage(true)// 是否可预览图片 true or false
                       .previewVideo(true)// 是否可预览视频 true or false
                       .enablePreviewAudio(false) // 是否可播放音频 true or false
                       .isCamera(true)// 是否显示拍照按钮 true or false
                       .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                       .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                       .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                       .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                       .enableCrop(true)// 是否裁剪 true or false
                       .compress(true)// 是否压缩 true or false
                       .glideOverride(400, 400)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                       .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                       .isGif(false)// 是否显示gif图片 true or false
                       .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                       .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                       .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                       .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                       .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                       .minimumCompressSize(100)// 小于100kb的图片不压缩
                       .synOrAsy(true)//同步true或异步false 压缩 默认同步
                       .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                       .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                       .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code 
    }
}
