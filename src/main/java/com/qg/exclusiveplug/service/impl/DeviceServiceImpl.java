package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    @Override
    public Double[] listPowerSum(InteractionData interactionData) {
        if(interactionData.getKey() == 3){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            try {
                Date date = sdf.parse(interactionData.getTime());
                log.info(date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Double[1];
    }
}
