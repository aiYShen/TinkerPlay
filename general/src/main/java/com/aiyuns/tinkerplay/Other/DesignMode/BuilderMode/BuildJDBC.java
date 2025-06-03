package com.aiyuns.tinkerplay.Other.DesignMode.BuilderMode;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: aiYunS @Date: 2022-7-15 上午 10:35 @Description: JDBC配置类:
 * host、port、user、password、database是必选参数，其他都非必须，有默认值 为了避免构造函数参数爆炸，所以使用构建者模式
 */
public class BuildJDBC {

  // IP
  private String host;
  // 端口
  private int port;
  // 账号
  private String user;
  // 密码
  private String password;
  // 数据库
  private String database;
  // 是否使用编码
  private Boolean useUnicode;
  // 编码格式
  private String characterEncoding;
  // 是否自动重连
  private Boolean autoReconnect;
  // 是否连接超时
  private Boolean failOverReadOnly;
  // 最大连接数
  private int maxReconnects;
  // 默认超时时长
  private int initialTimeout;
  // 连接超时时长
  private int connectTimeout;
  // 客户端从服务器读取数据的超时时长
  private int socketTimeout;

  public BuildJDBC(JDBCBuilder jdbcBuilder) {
    this.host = jdbcBuilder.host;
    this.port = jdbcBuilder.port;
    this.user = jdbcBuilder.user;
    this.password = jdbcBuilder.password;
    this.database = jdbcBuilder.database;
    this.useUnicode = jdbcBuilder.useUnicode;
    this.characterEncoding = jdbcBuilder.characterEncoding;
    this.autoReconnect = jdbcBuilder.autoReconnect;
    this.failOverReadOnly = jdbcBuilder.failOverReadOnly;
    this.maxReconnects = jdbcBuilder.maxReconnects;
    this.initialTimeout = jdbcBuilder.initialTimeout;
    this.connectTimeout = jdbcBuilder.connectTimeout;
    this.socketTimeout = jdbcBuilder.socketTimeout;
  }

  @Override
  public String toString() {
    return "BuildJDBC{"
        + "host='"
        + host
        + '\''
        + ", port="
        + port
        + ", user='"
        + user
        + '\''
        + ", password='"
        + password
        + '\''
        + ", database='"
        + database
        + '\''
        + ", useUnicode="
        + useUnicode
        + ", characterEncoding='"
        + characterEncoding
        + '\''
        + ", autoReconnect="
        + autoReconnect
        + ", failOverReadOnly="
        + failOverReadOnly
        + ", maxReconnects="
        + maxReconnects
        + ", initialTimeout="
        + initialTimeout
        + ", connectTimeout="
        + connectTimeout
        + ", socketTimeout="
        + socketTimeout
        + '}';
  }

  /*
   静态内部类: JDBC的Builder
   静态内部类定义在类中，任何方法外，用 static 定义。 静态内部类只能访问外部类的 静态成员
   生成（new）一个静态内部类不需要外部类成员：这是静态内部类和 成员内部类 的区别。
   静态内部类的对象可以直接生成： 而不需要通过生成外部 类对象 来生成。
  */
  public static class JDBCBuilder {

    private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    private boolean useUnicode = true;
    private String characterEncoding = "gbk";
    private Boolean autoReconnect = false;
    private Boolean failOverReadOnly = false;
    private int maxReconnects = 3;
    private int initialTimeout = 1800;
    private int connectTimeout = 1800;
    private int socketTimeout = 1800;

    public BuildJDBC JDBCBuild() {
      if (StringUtils.isBlank(host)) {
        throw new IllegalArgumentException("host不能为空!");
      }
      if (port < 0) {
        throw new IllegalArgumentException("port不能为空!");
      }
      if (StringUtils.isBlank(user)) {
        throw new IllegalArgumentException("user不能为空!");
      }
      if (StringUtils.isBlank(password)) {
        throw new IllegalArgumentException("password不能为空!");
      }
      if (StringUtils.isBlank(database)) {
        throw new IllegalArgumentException("database不能为空!");
      }
      return new BuildJDBC(this);
    }

    public JDBCBuilder setHost(String host) {
      if (StringUtils.isBlank(host)) {
        throw new IllegalArgumentException("host不正确,请检查host!");
      }
      this.host = host;
      return this;
    }

    public JDBCBuilder setPort(int port) {
      if (port < 0) {
        throw new IllegalArgumentException("port不正确,请检查port!");
      }
      this.port = port;
      return this;
    }

    public JDBCBuilder setUser(String user) {
      if (StringUtils.isBlank(user)) {
        throw new IllegalArgumentException("user不正确,请检查user!");
      }
      this.user = user;
      return this;
    }

    public JDBCBuilder setPassword(String password) {
      if (StringUtils.isBlank(password)) {
        throw new IllegalArgumentException("password不正确,请检查password!");
      }
      this.password = password;
      return this;
    }

    public JDBCBuilder setDatabase(String database) {
      if (StringUtils.isBlank(database)) {
        throw new IllegalArgumentException("database不正确,请检查database!");
      }
      this.database = database;
      return this;
    }

    public JDBCBuilder setUseUnicode(boolean useUnicode) {
      this.useUnicode = useUnicode;
      return this;
    }

    public JDBCBuilder setCharacterEncoding(String characterEncoding) {
      this.characterEncoding = characterEncoding;
      return this;
    }

    public JDBCBuilder setAutoReconnect(Boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
      return this;
    }

    public JDBCBuilder setFailOverReadOnly(Boolean failOverReadOnly) {
      this.failOverReadOnly = failOverReadOnly;
      return this;
    }

    public JDBCBuilder setMaxReconnects(int maxReconnects) {
      this.maxReconnects = maxReconnects;
      return this;
    }

    public JDBCBuilder setInitialTimeout(int initialTimeout) {
      this.initialTimeout = initialTimeout;
      return this;
    }

    public JDBCBuilder setConnectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public JDBCBuilder setSocketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
      return this;
    }
  }
}
