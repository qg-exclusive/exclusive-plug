package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;

/**
 * @author Wilder
 */
public interface DeviceService {
    /**
     *
     * @param interactionData
     * @return
     */
    Double[] listPowerSum(InteractionData interactionData);
}
