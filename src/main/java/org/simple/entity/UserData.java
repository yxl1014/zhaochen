package org.simple.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author yxl17
 * @Package : org.simple.entity
 * @Create on : 2024/2/19 20:38
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserData {
    private String uuid;
    private String dataName;
    private byte[] data;
    // 0、为保存 1、保存 2、公开 3、copy不允许公开
    private int state;
    private Timestamp createTime;
}
