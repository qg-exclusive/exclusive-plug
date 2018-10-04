package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.DataMapper;
import com.qg.exclusiveplug.dao.DeviceMapper;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.PredicatedData;
import com.qg.exclusiveplug.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Wilder
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 取出某电器在某一天的总用电量
     *
     * @param machineName 用电器名称
     * @param date        天数
     * @return 该天第一条数据与最后一条数据
     */
    @Override
    public void listDevicesCPByDate(String machineName, String date) {

        log.info(machineName + ":" + date);
        List<Device> deviceList = dataMapper.listDevicesCPByDate(machineName, date);
        log.info("开始收集每日用电总量");
        PredicatedData predicatedData = new PredicatedData();
        log.info("第一天的数据:" + deviceList.get(1).getDate() + deviceList.get(1).getCumulativePower());
        log.info("第二天的数据:" + deviceList.get(0).getDate() + deviceList.get(0).getCumulativePower());
        predicatedData.setCumulativePower(deviceList.get(1).getCumulativePower()
                - deviceList.get(0).getCumulativePower());
        predicatedData.setDate(date);
        predicatedData.setName(machineName);
        dataMapper.saveDayPower(predicatedData);
        log.info("收集每日用电总量结束");

    }

    /**
     * 保存未读入的设备信息
     *
     * @param multipartFile 设备信息
     */
    @Override
    public void saveDevice(MultipartFile multipartFile) {
        if(!multipartFile.isEmpty()){
            try {
                StringBuffer stringBuffer = new StringBuffer();
                InputStreamReader inputStreamReader = new InputStreamReader(multipartFile.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                List<Device> deviceList = new ArrayList<>();
                while(null != (line = bufferedReader.readLine())){
                    if(!line.startsWith("[2018")){
                        //查看是哪个串口
                        int index = (int)line.charAt(line.split(",")[0].length() + 3) - 48;
                        //得到单个插口所有参数信息
                        String[] plugs = line.split(",");
                        String name = plugs[0].split(":")[0];
                        double voltage = Double.parseDouble(plugs[0].split(":")[2]);
                        double current = Double.parseDouble(plugs[1].split(":")[1]);
                        double power = Double.parseDouble(plugs[2].split(":")[1]);
                        double powerFactor = Double.parseDouble(plugs[3].split(":")[1]);
                        double frequency = Double.parseDouble(plugs[4].split(":")[1]);
                        double cumulativePower = Double.parseDouble(plugs[5].split(":")[1].split("\\[")[0]);
                        String date = line.split("\\[")[1].split("\\.")[0];
                        // 如果输入数据是下午
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date1 = null;
                        try {
                            date1 = sdf.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date1);
                        calendar.add(Calendar.HOUR_OF_DAY, 12);
                        date = sdf.format(calendar.getTime());

                        Device device = new Device(index, name, current, voltage, power,
                                powerFactor, frequency, date, cumulativePower);
                        deviceList.add(device);
                        log.info(device.toString());
                    }
                }
                //将设备信息存入数据库
                deviceMapper.saveDevices(deviceList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
