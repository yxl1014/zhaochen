package org.simple.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yxl17
 * @Package : org.simple.entity
 * @Create on : 2024/2/19 20:31
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String uuid;
    private String userTel;
    private String userPwd;
}
