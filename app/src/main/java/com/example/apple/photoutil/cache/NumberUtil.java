package com.example.apple.photoutil.cache;

import java.math.BigDecimal;

/**
 * 作    者：高学军
 * 时    间：16/3/19
 * 描    述：
 * 修改时间：
 */
public class NumberUtil {

    //float 转换至 int 小数四舍五入
    public static int convertFloatToInt(float sourceNum) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
}
