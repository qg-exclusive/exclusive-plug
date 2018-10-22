package com.qg.exclusiveplug.util;

import com.qg.exclusiveplug.model.Device;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FormatMatchingUtil {

    /**
     * 判断嵌入式传来的设备信息是否符合格式
     * @param serviceInfo 设备信息
     * @return 符合返回true，否则为false
     */
    public static boolean isServiceInfo(String serviceInfo) {
        String rexp = "\\b\\w*:V_\\d:[0-9]+[.][0-9]*,I_\\d:[0-9]+[.][0-9]*," +
                "P_\\d:[0-9]+[.][0-9]*,PF_\\d:-?[0-9]+[.][0-9]*,F_\\d:[0-9]+[.][0-9]*,W_\\d:[0-9]+[.][0-9]*end" +
                "\\w*:V_\\d:[0-9]+[.][0-9]*,I_\\d:[0-9]+[.][0-9]*," +
                "P_\\d:[0-9]+[.][0-9]*,PF_\\d:[0-9]+[.][0-9]*,F_\\d:[0-9]+[.][0-9]*,W_\\d:[0-9]+[.][0-9]*end" +
                "\\w*:V_\\d:[0-9]+[.][0-9]*,I_\\d:[0-9]+[.][0-9]*," +
                "P_\\d:[0-9]+[.][0-9]*,PF_\\d:[0-9]+[.][0-9]*,F_\\d:[0-9]+[.][0-9]*,W_\\d:[0-9]+[.][0-9]*end\\b";
        Pattern pat = Pattern.compile(rexp);
        Matcher matcher = pat.matcher(serviceInfo);
        return matcher.matches();
    }

    /**
     * 切割字符串
     * @param serviceInfo 设备信息
     * @return 设备信息实体类
     */
    public static Device getDevice(String serviceInfo) {
        String rexp = "([0-9]+[.][0-9]*)";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(serviceInfo);
        int i = 0;
        double[] doubles = new double[6];
        while(matcher.find()){
            doubles[i] = Double.parseDouble(matcher.group());
            i++;
        }
        return new Device((int) serviceInfo.charAt(serviceInfo.length() - 1) - 48,
                serviceInfo.split(":")[0], doubles[1], doubles[0], doubles[2], doubles[3], doubles[4],
                DateUtil.currentTime(), doubles[5]);
    }
}
