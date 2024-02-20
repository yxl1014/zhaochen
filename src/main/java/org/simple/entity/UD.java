package org.simple.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yxl17
 * @Package : org.simple.entity
 * @Create on : 2024/2/20 21:06
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UD {
    private int id;
    private String userId;
    private String dataId;

    public UD(String userId, String dataId) {
        this.userId = userId;
        this.dataId = dataId;
    }
}
