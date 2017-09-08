package com.sda.savers.framework.common;

/**
 * Created by allen on 2017/6/1.
 */

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 所有ajax请求返回结果
 * @param <T>
 */
@Data
@AllArgsConstructor
public class RestResult<T> {
  private boolean success;
  private String message;
  private T data;
  private String errorCode;
}
