package com.aiyuns.tinkerplay.Listener;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * @Author: aiYunS @Date: 2023年10月2日, 0002 上午 10:14:02 @Description: 文件监听 （1）可以监控文件夹的创建、删除和修改
 * （2）可以监控文件的创建、删除和修改 （3）采用的是观察者模式来实现的 （4）采用线程去定时去刷新检测文件的变化情况
 */
public class FileListener extends FileAlterationListenerAdaptor {

  /** 文件创建执行 */
  @Override
  public void onFileCreate(File file) {
    System.out.println("[新建]:" + file.getAbsolutePath());
  }

  /** 文件创建修改 */
  @Override
  public void onFileChange(File file) {
    System.out.println("[修改]:" + file.getAbsolutePath());
  }

  /** 文件删除 */
  @Override
  public void onFileDelete(File file) {
    System.out.println("[删除]:" + file.getAbsolutePath());
  }

  /** 目录创建 */
  @Override
  public void onDirectoryCreate(File directory) {
    System.out.println("[新建]:" + directory.getAbsolutePath());
  }

  /** 目录修改 */
  @Override
  public void onDirectoryChange(File directory) {
    System.out.println("[修改]:" + directory.getAbsolutePath());
  }

  /** 目录删除 */
  @Override
  public void onDirectoryDelete(File directory) {
    System.out.println("[删除]:" + directory.getAbsolutePath());
  }

  @Override
  public void onStart(FileAlterationObserver observer) {
    // TODO Auto-generated method stub
    super.onStart(observer);
  }

  @Override
  public void onStop(FileAlterationObserver observer) {
    // TODO Auto-generated method stub
    super.onStop(observer);
  }

  public static void main(String[] args) throws Exception {
    // 监控目录
    String rootDir = "E:\\aiyuns\\TinkerPlay\\logs\\tinkerplay";
    // 轮询间隔 5 秒
    long interval = TimeUnit.SECONDS.toMillis(1);
    // 创建过滤器
    IOFileFilter directories =
        FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
    IOFileFilter files =
        FileFilterUtils.and(
            FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".log"));
    IOFileFilter filter = FileFilterUtils.or(directories, files);
    // 使用过滤器
    FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
    // 不使用过滤器
    // FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
    observer.addListener(new FileListener());
    // 创建文件变化监听器
    FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
    // 开始监控
    monitor.start();
  }
}
