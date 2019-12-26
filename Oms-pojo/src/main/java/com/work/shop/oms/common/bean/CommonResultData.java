package com.work.shop.oms.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结果返回对象
 *
 * @param <T>
 * @author caixiao
 */

@Data
@NoArgsConstructor
public class CommonResultData<T> implements Serializable {

    private static final long serialVersionUID = -7321472587088489029L;

    private T result;

    private String msg;

    private String isOk;

    private int total;

}
