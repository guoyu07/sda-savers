package com.sda.savers.framework.util;

import java.util.UUID;

/**
 * Created by allen on 2017/5/23.
 */
public class GuidGeneratorUtil {
  public static String newGuid(){
    return UUID.randomUUID().toString().replaceAll("-","");
  }
}
