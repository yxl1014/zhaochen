package org.simple.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author yxl17
 * @Package : org.simple.entity
 * @Create on : 2024/2/19 20:54
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReData {
    private Map<String,Object[]> xAxis;
    private List<Object> entity;
    private Integer state;
    private Timestamp createTime;
    private String name;
    private String uid;
}
