package com.aiyuns.tinkerplay.Other.PictureAndAudioAndVideo;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

// 地图图像RAW文件转换为TIF格式
public class RawToGeoTiffConverter {

  public static void main(String[] args) {
    // 注册GDAL库
    gdal.AllRegister();

    String rawFilePath = "/Users/yuxinbai/Desktop/input.raw";
    String geoTiffFilePath = "/Users/yuxinbai/Desktop/output.tif";
    // 替换为实际的坐标系
    String projection = "EPSG:4326";
    // 设置高斯-克吕格2000投影的Proj.4字符串
    // String proj4String = "+proj=tmerc +lat_0=0 +lon_0=120 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80
    // +towgs84=0,0,0,0,0,0,0 +units=m +no_defs";
    // 替换为实际的中央经线等参数
    // 读取RAW文件
    Dataset rawDataset = gdal.Open(rawFilePath, gdalconstConstants.GA_ReadOnly);
    if (rawDataset == null) {
      System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
      System.err.println(gdal.GetLastErrorMsg());
      System.exit(1);
    }

    // 创建GeoTIFF文件
    Driver driver = gdal.GetDriverByName("GTiff");
    if (driver == null) {
      System.err.println("GTiff driver not available");
      System.exit(1);
    }

    Dataset geoTiffDataset = driver.CreateCopy(geoTiffFilePath, rawDataset);
    if (geoTiffDataset == null) {
      System.err.println("GDALCreateCopy failed - " + gdal.GetLastErrorNo());
      System.err.println(gdal.GetLastErrorMsg());
      System.exit(1);
    }

    // 设置投影和地理变换（如果需要）
    geoTiffDataset.SetProjection(projection);
    // 如果需要设置地理变换（例如原点坐标和像素大小）
    // double[] geoTransform = { x_min, pixel_width, 0, y_max, 0, -pixel_height };
    // geoTiffDataset.SetGeoTransform(geoTransform);

    // 关闭数据集
    geoTiffDataset.FlushCache();
    geoTiffDataset.delete();
    rawDataset.delete();

    System.out.println("Conversion completed successfully.");
  }
}
