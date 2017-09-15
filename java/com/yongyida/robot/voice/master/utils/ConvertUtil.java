package com.yongyida.robot.voice.master.utils;

public class ConvertUtil {
	/**
     * @param val
     *            :请求接口
     * @param precision
     *            :参数
     * @return 返回结果
     */

    public static Double roundDouble(double val, int precision)
    {
        Double ret = null;
        try
        {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}
