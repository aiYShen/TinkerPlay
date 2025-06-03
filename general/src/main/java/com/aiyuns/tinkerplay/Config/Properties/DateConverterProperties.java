package com.aiyuns.tinkerplay.Config.Properties;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: aiYunS @Date: 2023年2月17日, 0017 下午 4:26:28 @Description:
 * 解决导出Excel的Date格式转换出现的NoSuchMethodError异常
 */
public class DateConverterProperties implements Converter<Date> {

  private static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

  @Override
  public Class<?> supportJavaTypeKey() {
    return Converter.super.supportJavaTypeKey();
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return Converter.super.supportExcelTypeKey();
  }

  @Override
  public WriteCellData<?> convertToExcelData(
      Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration)
      throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
    String dateValue = sdf.format(value);
    return new WriteCellData<>(dateValue);
  }
}
