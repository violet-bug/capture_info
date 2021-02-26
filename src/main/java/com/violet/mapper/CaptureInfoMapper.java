package com.violet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.violet.pojo.CaptureInfo;

public interface CaptureInfoMapper extends BaseMapper<CaptureInfo> {
    //爬取的信息存入数据库
    int insert(CaptureInfo captureInfo);
}
