package com.aiyuns.tinkerplay;

import static com.aiyuns.tinkerplay.Utils.TokenCreateUtil.isJwtExpired;

import com.aiyuns.tinkerplay.Algorithm.RecursiveAlgorithm;
import com.aiyuns.tinkerplay.Controller.Service.RedisService;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.ProjBaseDao;
import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.UserDao;
import com.aiyuns.tinkerplay.DataStructure.Stack;
import com.aiyuns.tinkerplay.Entity.ExcelPOJO;
import com.aiyuns.tinkerplay.Entity.ProjbaseException;
import com.aiyuns.tinkerplay.Entity.TokenAccess;
import com.aiyuns.tinkerplay.Entity.User;
import com.aiyuns.tinkerplay.Other.DesignMode.BuilderMode.BuildJDBC;
import com.aiyuns.tinkerplay.Utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.zip.ZipOutputStream;
import javax.net.ssl.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import lombok.Cleanup;
import org.apache.commons.codec.binary.Hex;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @Author: aiYunS @Date: 2022-7-7 下午 04:57 @Description: 单元测试
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-windows.yml")
public class junitTest implements Runnable {
  @Autowired private UserDao userRepository;
  // private UserDaoTwo userRepository2;
  @Autowired ProjBaseDao projBaseDao;
  @Autowired RedisService redisService;

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  // 多线程传递参数
  ArrayList list = null;

  public void setList(ArrayList list) {
    this.list = list;
  }

  @Test
  public void test1() throws Exception {
    // 创建1条记录
    // userRepository.insertOne(new User(100,"AAA","海南岛","男",simpleDateFormat.parse("1993-01-01")));
    long startTime1 = System.currentTimeMillis();
    List<User> users = userRepository.findAll0();
    long endTime1 = System.currentTimeMillis();
    System.out.println("第一次查询耗时: " + (endTime1 - startTime1) + "ms");

    //        long startTime2 = System.currentTimeMillis();
    //        User u2 = userRepository.findAll("AAA");
    //        long endTime2 = System.currentTimeMillis();
    //        System.out.println("第二次查询：" + u2 + " 耗时: " + (endTime1-startTime1)+"ms");
  }

  @Test
  public void test2() throws Exception {
    for (int i = 101; i <= 10100; i++) {
      userRepository.insertOne(
          new User(i, "AAA", "海南岛", "男", simpleDateFormat.parse("1993-01-01")));
    }
  }

  @Test
  public void test3() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = null;
    Date d2 = null;
    try {
      d1 = simpleDateFormat.parse("1993-01-01");
      d2 = simpleDateFormat.parse("1995-12-31");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    List<User> u = userRepository.findByTime(d1, d2);
    System.out.println("默认形式: " + u);
    String[] fixedColumn = {"id", "username", "address", "sex", "birthday"};
    String[] fixedColumnName = {"序号", "姓名", "地址", "性别", "生日"};
    try {
      RowConvertColUtil.ConvertData lists =
          RowConvertColUtil.doConvertReturnObj(
              u, "username", fixedColumn, "sex", true, fixedColumnName, "0");
      System.out.println("行转列后: " + lists);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 构建者模式构建复杂对象之JDBC配置类对象
  @Test
  public void test4() {
    com.aiyuns.tinkerplay.Other.DesignMode.BuilderMode.BuildJDBC BuildJDBC =
        new BuildJDBC.JDBCBuilder()
            .setHost("127.0.0.1")
            .setPort(3306)
            .setUser("root")
            .setPassword("19930218")
            .setDatabase("tinkerplay")
            .setCharacterEncoding("utf8")
            .setInitialTimeout(6000)
            .JDBCBuild();
    System.out.println("jdbcConfig = " + BuildJDBC);
  }

  // 测试JDBC工具类JDBCUtils
  @Test
  public void test5() throws Exception {}

  // 测试JDBC工具类DBUtil读取db.properties配置文件创建jdbc连接
  @Test
  public void test6() throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    User s = null;
    conn = DBUtil.getConnection();
    String sql = "select * from user";
    ps = conn.prepareStatement(sql);
    rs = ps.executeQuery();
    while (rs.next()) { // 当结果集中有就一直进行执行
      int id = rs.getInt("id");
      String username = rs.getString("username");
      String birthday = rs.getString("birthday");
      String address = rs.getString("address");
      String sex = rs.getString("sex");
      // 只要有数据每读取一行就创建一个Student对象
      s = new User();
      // 将从表中获取的数据存储到一个Student类对象中
      s.setId(id);
      s.setUsername(username);
      s.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
      s.setAddress(address);
      s.setSex(sex);
      System.out.println(
          "===========================================================================");
      System.out.println(s);
      System.out.println(
          "===========================================================================");
    }
    DBUtil.release(conn, ps, null);
  }

  // 测试 通过AOP 进行日志打印
  @Test
  public void test7() throws Exception {
    User u = userRepository.findById(67);
    System.out.println(u);
  }

  // 测试resolveTokenUtil工具类解析token
  @Test
  public void test8() {
    //        TokenAccess tokenAccess = ResolveTokenUtil.resolveToken("Bearer
    // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIzMyIsInVzZXJfdGVsIjoiIiwidXNlcl9uYW1lIjoicXQtaHkxIiwidG9rZW5fcHdkX3N0cm9uZ19mbGFnIjoiMSIsImF1dGhvcml0aWVzIjpbIkRFTEVURSMvKioiLCJQT1NUIy8qKiIsIkdFVCMvKioiLCJQVVQjLyoqIl0sInVzZXJfaWRfbnVtYmVyIjoiIiwiY2xpZW50X2lkIjoiZ2lzcS02MmM2NGNkZS02NmZlLTExZTktOTM3YS0yYzRkNTRmMDFiYTQiLCJ1c2VyX2tpbmQiOjEsInRlbmFudF91aWQiOiI3MzNlNTI2ZS1jZDI5LTExZWMtOWU3OC0wMjQyMTBkOGE5ZjMiLCJ1c2VyX2lkIjoiOTkwY2QxMTItMDc3YS00ZjBiLThkYTktNjZlMjQ5Mzk1OTViIiwic2NvcGUiOlsiYWxsIl0sInVzZXJfbmlja25hbWUiOiLkvZXotZ8iLCJ0b2tlbl9wd2Rfb3ZlcmR1ZV9mbGFnIjoiMSIsImV4cCI6MTY1OTk3NDY1MSwianRpIjoiNTYxNzZjYTgtMWY2Ni00MzUzLWEwMDktZTVlMWI1NzJhZmI5In0.mUaeLwj6HdLtyMy3hbducirjAGX8_kvOR-Qhlbu6dTqf_QqqefWhYKOxAfixjza39rKkJtF6rmad3zrmsxJD_FMnLbO6DcLuJ9CaluQ2liAsXtjFdd1dAfnNLfK5ZqKWjCc0GlaTCooPu6f0ajxT3kGfYXFyWm8Pj8Pi0CDJ8durd_iJZMfCmHwEZ-bBKLbPNnC1BnIqOdHwiE6-RyYzxnkc7m2_gBkFCBSioASUaWI7VAGcBE-9Wf29di6_WhqPgV0-uEJZAGtTZ7ih4sq5SJTqnGUUQHyOvdz77RIKaeSyRAY4aCG_A1s5ziYcq677qEiVMwzfn2kDxKyi8EWk2g");
    //        System.out.println("=========================================================");
    //        System.out.println("测试解析Token = " + tokenAccess);
    //        System.out.println("=========================================================");

    TokenAccess tokenAccess2 =
        ResolveTokenUtil.resolveToken(
            "Bearer"
                + " eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIzMyIsInVzZXJfdGVsIjoiIiwidXNlcl9uYW1lIjoiZGYyMDcwMzg1OTg2NiIsInRva2VuX3B3ZF9zdHJvbmdfZmxhZyI6IjEiLCJhdXRob3JpdGllcyI6WyJERUxFVEUjLyoqIiwiUE9TVCMvKioiLCJHRVQjLyoqIiwiUFVUIy8qKiJdLCJ1c2VyX2lkX251bWJlciI6IiIsImNsaWVudF9pZCI6Imdpc3EtNjJjNjRjZGUtNjZmZS0xMWU5LTkzN2EtMmM0ZDU0ZjAxYmE0IiwidXNlcl9raW5kIjoxLCJ0ZW5hbnRfdWlkIjoiZWQ0YzU4MjAtY2QxYS0xMWVjLTllNzgtMDI0MjEwZDhhOWYzIiwidXNlcl9pZCI6ImRkMDQxZDQ4LTBlZTQtMTFlZC1iZGQ0LTAyNDIxMGQ4YTlmMyIsInNjb3BlIjpbImFsbCJdLCJ1c2VyX25pY2tuYW1lIjoi6L-H5LiA5YekIiwidG9rZW5fcHdkX292ZXJkdWVfZmxhZyI6IjEiLCJleHAiOjE2NjU2NjE5MjIsImp0aSI6IjNiYjhmNjEyLWJhYmItNDkwMS1hMzMyLTQ1MmQ2NzUyNGJlOCJ9.w65GcLT_yGkhkFQr66ackzravacE-ifU40eF0sSgo-0x_8UegZunQDq_HwzwaAQXWgMuZQxk2xzGgKy4tvq2-yMz5-GOYgNT07KXoeLQL9KeJAN0zwW3KGUUEeLzItDoTh1jcxla2nkKATmkiLNQ_F5L35HCnjRlNCCiRaiTpMP1GXwnvzu54i1yjUW3dE9qglfLBVQfJwexX8rz7AEj6zxe39ohysS2HJ6tWDHia2igdi2LE3647Bks0M5wsjbVKYNNGuN8fL5lNddZ3mOjr4cQvm6yRWEXAgJPes2QfxOcDMaliTm7ne9zpkJAT06QMJpv95j0i80VQY0HXtwVjw");
    System.out.println("=========================================================");
    System.out.println("测试解析Token = " + tokenAccess2);
    System.out.println("=========================================================");

    //        TokenAccess tokenAccess3 = ResolveTokenUtil.resolveToken("Bearer
    // eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eNpMy2EKgzAMBeC75LeFJqa1eoMdo2vyo4M5WRUG4t0tTsEHj0cg3wplecIAUd55hAZK-kx6u_U3wYDeB0K2nW1gKfp9SP2w_-BZPvcKVpzjfGDniQ78mnOV3CKT02RCRYYxqYmuFyMSsE9tJEsM2w4AAP__.8e4nsQenDe4jFMBK4iTAWX-wS_a5Qq5ZAV73nndGgzg");
    //        System.out.println("=========================================================");
    //        System.out.println("测试解析Token = " + tokenAccess3);
    //        System.out.println("=========================================================");
  }

  // 测试单表批量插入,springboot框架的性能
  // 结论: 10万条数据3s,100万条数据约40s
  @Test
  public void test9() {
    long startTime = System.currentTimeMillis();
    List<User> users = new ArrayList<User>();
    for (int i = 7; i < 1000007; i++) {
      User user = new User();
      user.setId(i);
      user.setUsername("test" + i);
      if (i % 2 == 0) {
        user.setSex("男");
      } else {
        user.setSex("女");
      }
      user.setAddress("地址" + i);
      try {
        user.setBirthday(simpleDateFormat.parse("2020-08-26"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      users.add(user);
    }
    userRepository.insertAll(users);
    long endTime = System.currentTimeMillis();
    System.out.println(" 耗时: " + (endTime - startTime) / 1000 + "s");
  }

  // 测试readTXTUtil工具类
  @Test
  public void test10() {
    System.out.println(ReadTXTtoJsonObjUtil.readTXTtoObj(""));
  }

  // 测试RecursiveAlgorithm工具类 递归搜索本机文件
  @Test
  public void test11() {
    // TreeSet<String> filePath = RecursiveAlgorithm.fileSearch(new
    // File("D:\\Users\\lenovo\\Desktop"),"nginx_BI.conf");
    TreeSet<String> filePath = RecursiveAlgorithm.fileSearch(new File("E:\\"), "config.js");
    System.out.println("该文件路径在: " + filePath);
  }

  // 测试ReadExcelUtil工具类 读取excel 写入数据库
  @Test
  public void test12() throws Exception {
    /**
     * 方法一 fileName:Excel文件路径 StatrRow：读取的开始行数（默认填0） EndRow：读取的结束行数（填-1为全部）
     * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据作为KEY，若无则KEY为当前列数）
     */
    // List<List<Map<String,Object>> >result =
    // ReadExcelUtil.ReadExcelByRC("D:\\Users\\lenovo\\Desktop\\每㎡大于10万.xlsx",0,-1,true,"-1");
    List<List<Map<String, Object>>> result =
        ReadExcelUtil.ReadExcelByRC(
            "D:\\Users\\lenovo\\Desktop\\每㎡大于10万-副本.xlsx", 0, -1, true, "-1");
    System.out.println(result.size());
    System.out.println(result);

    /**
     * 方法二 ReadExcelByPOJO(String fileName, int StatrRow, int EndRow, Class t) fileName:Excel文件路径
     * StatrRow：读取的开始行数（默认填0） EndRow：读取的结束行数（填-1为全部） Class<T>：传过来的实体类类型
     */
    List<List<Object>> result2 = null;

    // result2 = ReadExcelUtil.ReadExcelByPOJO("D:\\Users\\lenovo\\Desktop\\每㎡大于10万.xlsx",0,-1,
    // ExcelPOJO.class,"-1");
    result2 =
        ReadExcelUtil.ReadExcelByPOJO(
            "D:\\Users\\lenovo\\Desktop\\每㎡大于10万-副本.xlsx", 0, -1, ExcelPOJO.class, "-1");
    System.out.println(result2.size());
    System.out.println(result2);
  }

  // 测试token生成工具类
  @Test
  public void test13() throws Exception {
    String token = new TokenCreateUtil().token("admin", "admin!@#$1234");
    System.out.println(token);
    TokenCreateUtil.checkToken(token);
    System.out.println(isJwtExpired(token));
  }

  // 测试二维码生成工具
  @Test
  public void test14() {
    try {
      QRCodeGeneratorUtil.generateQRCodeImage(
          "maomiaomiao and ergougou ", 350, 350, "D:\\Users\\lenovo\\Desktop\\QRTest.png");
    } catch (WriterException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 测试识别二维码图片工具类
  @Test
  public void test15() {
    System.out.println(QRCodeUtil.deEncodeByPath("D:\\Users\\lenovo\\Desktop\\a.jpg"));
  }

  // 测试栈的数据结构
  @Test
  public void test16() {

    User u1 = new User("u1");
    User u2 = new User("u2");
    User u3 = new User("u3");
    User u4 = new User("u4");
    User u5 = new User("u5");
    User u6 = new User("u6");

    Stack s = new Stack();

    // 元素压栈
    s.push(u1);
    s.push(u2);
    s.push(u3);
    s.push(u4);
    s.push(u5);
    s.push(u6);

    // 元素弹栈
    s.pop();
    s.pop();
    s.pop();
    s.pop();
    s.pop();
    s.pop();
  }

  // 测试HttpUtil和MacUtil工具类
  @Test
  public void test17() throws Exception {

    // String addr = MacUtil.getAddress("2.0.1.57");
    // System.out.println(addr);
    // String BKYHtml = HttpUtil.doGet("https://www.cnblogs.com/");
    // System.out.println(html);
    // ResolverHTML.ResolverHTML1(BKYHtml);
    // String JYHtml = HttpUtil.doGet("https://www.jiyingw.vip/movie");
    // ResolverHTML.ResolverHTML2("https://www.jiyingw.vip/movie");
    // ResolverHTML.ResolverHTML2(JYHtml);

    String[] args = new String[1];
    // args[0] = "www.jiyingw.vip";
    args[0] = "www.baidu.com";
    String host;
    int port;
    char[] passphrase;
    if ((args.length == 1) || (args.length == 2)) {
      String[] c = args[0].split(":");
      host = c[0];
      port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
      String p = (args.length == 1) ? "changeit" : args[1];
      passphrase = p.toCharArray();
    } else {
      System.out.println("Usage: java InstallCert <host>[:port] [passphrase]");
      return;
    }

    File file = new File("jssecacerts");
    if (file.isFile() == false) {
      char SEP = File.separatorChar;
      File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
      file = new File(dir, "jssecacerts");
      if (file.isFile() == false) {
        file = new File(dir, "cacerts");
      }
    }
    System.out.println("Loading KeyStore " + file + "...");
    InputStream in = new FileInputStream(file);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(in, passphrase);
    in.close();

    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
    InstallCertUtil.SavingTrustManager tm =
        new InstallCertUtil.SavingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[] {tm}, null);
    SSLSocketFactory factory = context.getSocketFactory();

    System.out.println("Opening connection to " + host + ":" + port + "...");
    SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
    socket.setSoTimeout(10000);
    try {
      System.out.println("Starting SSL handshake...");
      socket.startHandshake();
      socket.close();
      System.out.println();
      System.out.println("No errors, certificate is already trusted");
    } catch (SSLException e) {
      System.out.println();
      e.printStackTrace(System.out);
    }

    X509Certificate[] chain = tm.chain;
    if (chain == null) {
      System.out.println("Could not obtain server certificate chain");
      return;
    }

    //        BufferedReader reader =
    //                new BufferedReader(new InputStreamReader(System.in));

    System.out.println();
    System.out.println("Server sent " + chain.length + " certificate(s):");
    System.out.println();
    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    for (int i = 0; i < chain.length; i++) {
      X509Certificate cert = chain[i];
      System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
      System.out.println("   Issuer  " + cert.getIssuerDN());
      sha1.update(cert.getEncoded());
      System.out.println("   sha1    " + InstallCertUtil.toHexString(sha1.digest()));
      md5.update(cert.getEncoded());
      System.out.println("   md5     " + InstallCertUtil.toHexString(md5.digest()));
      System.out.println();
    }

    System.out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
    // String line = reader.readLine().trim();
    String line = "1";
    int k;
    try {
      k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
    } catch (NumberFormatException e) {
      System.out.println("KeyStore not changed");
      return;
    }

    X509Certificate cert = chain[k];
    String alias = host + "-" + (k + 1);
    ks.setCertificateEntry(alias, cert);

    OutputStream out = new FileOutputStream("jssecacerts");
    ks.store(out, passphrase);
    out.close();

    System.out.println();
    System.out.println(cert);
    System.out.println();
    System.out.println("Added certificate to keystore 'jssecacerts' using alias '" + alias + "'");
  }

  // 测试Sm2工具类,测试Sm2加解密
  @Test
  public void test18() throws Exception {
    KeyPair keyPair = Sm2Util.generateSm2KeyPair();
    String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    String data = "{\"daId\":\"123456\"}";
    String encryptedJsonStr = Hex.encodeHexString(Sm2Util.encrypt(data, publicKey)) + ""; // 16进制字符串
    String decryptedJsonStr = Sm2Util.decrypt(Hex.decodeHex(encryptedJsonStr), privateKey);
    String sign = Hex.encodeHexString(Base64.getDecoder().decode(Sm2Util.sign(data, privateKey)));
    boolean flag = Sm2Util.verify(Hex.encodeHexString(data.getBytes()), sign, publicKey);
    System.out.println("base64后privateKey:" + privateKey);
    System.out.println("base64后publicKey:" + publicKey);
    System.out.println("加密前数据:" + data);
    System.out.println("公钥加密后16进制字符串:" + encryptedJsonStr);
    System.out.println("私钥解密后数据：" + decryptedJsonStr);
    System.out.println("私钥加签后数据(16进制)：" + sign);
    System.out.println("公钥验签结果：" + flag);
  }

  // 测试纯JDBC流式查询一次性读取400w数据量
  @Test
  public void test19() throws Exception {
    @Cleanup Connection connection = DBUtil.getConnection();
    @Cleanup
    Statement statement =
        connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    statement.setFetchSize(Integer.MIN_VALUE);
    List<ProjbaseException> projbaseExceptionList = new ArrayList<>();
    long start = System.currentTimeMillis();
    // long offset = 0;
    // int size = 100;
    // long count = 0;
    // while (true) {
    // String sql = String.format("SELECT * FROM projbase LIMIT %s, %s", offset, size);
    // @Cleanup ResultSet rs = statement.executeQuery(sql);

    @Cleanup ResultSet rs = statement.executeQuery("SELECT * FROM projbase");
    System.out.println(
        "  🚀🚀🚀 400万数据量查询耗时 :: {} " + (System.currentTimeMillis() - start) / 1000 + " 秒");
    while (rs.next()) {
      System.out.println("  程序走到循环处理数据耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
      ProjbaseException projbaseException = new ProjbaseException();
      JSONObject obj = JSON.parseObject(rs.getString("jsonObj"));
      // 获取登录人名称
      String recvUserName = "";
      if (obj.getString("recvUserName") != null) {
        recvUserName = obj.getString("recvUserName");
      }
      // 获取查询人姓名
      JSONObject affFormInfo = JSON.parseObject(obj.getString("affFormInfo"));
      String sqrname = "";
      if (affFormInfo.getString("sqrname") != null) {
        sqrname = affFormInfo.getString("sqrname");
      }
      // 判断不一致信息
      if (!recvUserName.equals(sqrname) && sqrname.length() < 7) {
        projbaseException.setProjId(obj.getString("projId"));
        if (rs.getString("gmtApply") != null && !"".equals(rs.getString("gmtApply"))) {
          projbaseException.setCjsj(rs.getString("gmtApply"));
        } else {
          if (rs.getString("cjsj") != null && !"".equals(rs.getString("cjsj"))) {
            projbaseException.setCjsj(rs.getString("cjsj"));
          } else {
            projbaseException.setCjsj(null);
          }
        }
        projbaseException.setProjectName(obj.getString("projectName"));
        projbaseException.setApplyName(affFormInfo.getString("sqrname"));
        projbaseException.setApplyCardNo(affFormInfo.getString("zjh"));
        projbaseException.setQxdm(affFormInfo.getString("xzqbm"));
        projbaseException.setRecvUserName(obj.getString("recvUserName"));
        projbaseException.setRecvDeptCode(obj.getString("recvDeptName"));
        projbaseException.setRecvUserId(obj.getString("recvUserId"));
        projbaseException.setFaceValidationResult(obj.getString("faceValidationResult"));
        projbaseExceptionList.add(projbaseException);
      }
      // count++;
    }
    // if (count == 0) break;
    // offset += size;
    // }
    if (projbaseExceptionList != null && projbaseExceptionList.size() > 0) {
      System.out.println(" 开始插入: " + projbaseExceptionList.size() + "条数据...");
      long start1 = System.currentTimeMillis();
      projBaseDao.writeProjbaseException(projbaseExceptionList);
      System.out.println(" 批量插入数据耗时: " + (System.currentTimeMillis() - start1) / 1000 + " 秒");
      System.out.println(" 开始插入: " + projbaseExceptionList.size() + "条数据...");
    }
  }

  /* 测试纯JDBC流式查询一次性读取400w数据量: 优化数据处理和分批插入,每次插入5w条数据
     分批插入:
         2500 --> 2938
         1000 --> 3306
         2000 --> 3443
     插入功能开启多线程后:
         5000 --> 3848
         50000 --> 3449
         20000 --> 3360
  */
  @Test
  public void test23() throws Exception {
    Connection connection = DBUtil.getConnection();
    Statement statement =
        connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    statement.setFetchSize(Integer.MIN_VALUE);
    List<ProjbaseException> projbaseExceptionList = new ArrayList<>();
    long start = System.currentTimeMillis();
    // long offset = 0;
    // int size = 100;
    // long count = 0;
    // while (true) {
    // String sql = String.format("SELECT * FROM projbase LIMIT %s, %s", offset, size);
    // @Cleanup ResultSet rs = statement.executeQuery(sql);
    // 批次
    int pc = 0;
    ResultSet rs = statement.executeQuery("SELECT * FROM projbase");
    System.out.println(
        "  🚀🚀🚀 400万数据量查询耗时 :: {} " + (System.currentTimeMillis() - start) / 1000 + " 秒");
    while (rs.next()) {
      ProjbaseException projbaseException = new ProjbaseException();
      JSONObject obj = JSON.parseObject(rs.getString("jsonObj"));
      // 获取登录人名称
      String recvUserName = "";
      if (obj.getString("recvUserName") != null) {
        recvUserName = obj.getString("recvUserName");
      }
      // 获取查询人姓名
      JSONObject affFormInfo = JSON.parseObject(obj.getString("affFormInfo"));
      String sqrname = "";
      if (affFormInfo.getString("sqrname") != null) {
        sqrname = affFormInfo.getString("sqrname");
      }
      // 判断不一致信息
      if (!recvUserName.equals(sqrname) && sqrname.length() < 7) {
        projbaseException.setProjId(obj.getString("projId"));
        if (rs.getString("gmtApply") != null && !"".equals(rs.getString("gmtApply"))) {
          projbaseException.setCjsj(rs.getString("gmtApply"));
        } else {
          if (rs.getString("cjsj") != null && !"".equals(rs.getString("cjsj"))) {
            projbaseException.setCjsj(rs.getString("cjsj"));
          } else {
            projbaseException.setCjsj(null);
          }
        }
        projbaseException.setProjectName(obj.getString("projectName"));
        projbaseException.setApplyName(affFormInfo.getString("sqrname"));
        projbaseException.setApplyCardNo(affFormInfo.getString("zjh"));
        projbaseException.setQxdm(affFormInfo.getString("xzqbm"));
        projbaseException.setRecvUserName(obj.getString("recvUserName"));
        projbaseException.setRecvDeptCode(obj.getString("recvDeptName"));
        projbaseException.setRecvUserId(obj.getString("recvUserId"));
        projbaseException.setFaceValidationResult(obj.getString("faceValidationResult"));
        projbaseExceptionList.add(projbaseException);

        if (projbaseExceptionList != null && projbaseExceptionList.size() > 0) {
          if (projbaseExceptionList.size() == 2500) {
            System.out.println("第" + ++pc + "批次,开始插入: " + projbaseExceptionList.size() + "条数据...");
            long start1 = System.currentTimeMillis();
            projBaseDao.writeProjbaseException(projbaseExceptionList);
            System.out.println(" 批量插入数据耗时: " + (System.currentTimeMillis() - start1) / 1000 + " 秒");
            // 插入5w条数据后,清空list
            projbaseExceptionList.clear();
          }
        }
      }
    }
    // 最后剩余的数据,但是不满5w条,进行最后一次插入
    projBaseDao.writeProjbaseException(projbaseExceptionList);
    System.out.println(" 总耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
    DBUtil.release(connection, statement, null, rs);
  }

  // 测试纯JDBC流式查询一次性读取25w数据量,并进行数据分析.结论: 都是0秒查询完毕
  // 测试SQL查询全部使用　*　号和使用具体字段名的性能差异
  @Test
  public void test20() throws Exception {
    Connection connection = DBUtil.getConnection();
    Statement statement =
        connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    statement.setFetchSize(Integer.MIN_VALUE);
    PreparedStatement preparedStatement = null;
    long start = System.currentTimeMillis();
    // @Cleanup ResultSet rs = statement.executeQuery("SELECT * FROM projbase_exception");
    ResultSet rs = statement.executeQuery("SELECT count(distinct projid) FROM projbase_exception");
    ResultSet rs2 = null;
    int count = 0;
    while (rs.next()) {
      count = rs.getInt(1);
    }
    System.out.println("========== 总记录条数: " + count + "条 ==========");
    // 查询总人数
    rs =
        statement.executeQuery(
            "SELECT distinct recvUserName, recvUserId, recvDeptCode FROM projbase_exception order"
                + " by recvUserName");
    int headCount = 0;
    System.out.println("姓名     " + "查询次数     " + "查询者ID     " + "查询部门");
    while (rs.next()) {
      String recvUserName = rs.getString("recvUserName");
      String recvUserId = rs.getString("recvUserId");
      String recvDeptCode = rs.getString("recvDeptCode");
      headCount++;
      String sql =
          "SELECT count(*) FROM projbase_exception where recvUserName = ? and recvUserId = ?";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, rs.getString("recvUserName"));
      preparedStatement.setString(2, rs.getString("recvUserId"));
      rs2 = preparedStatement.executeQuery();
      int everyCount = 0;
      while (rs2.next()) {
        everyCount = rs2.getInt(1);
      }

      System.out.println(recvUserName + "," + everyCount + "," + recvUserId + "," + recvDeptCode);
    }
    System.out.println("========== 查询总人数: " + headCount + "人 ==========");
    // 释放资源
    DBUtil.release(connection, statement, preparedStatement, rs);
    System.out.println(
        "  🚀🚀🚀 25万数据量查询耗时 :: {} " + (System.currentTimeMillis() - start) / 1000 + " 秒");
  }

  @Test
  public void test21() {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, String> map = new HashMap<>();
    Map<String, String> map2 = new HashMap<>();
    Map<String, Object> userMap = new IdentityHashMap<>();

    map.put("id", "5");
    map.put("username", "pdf对象");
    map.put("address", "E盘");
    map.put("sex", "男");
    map.put("birthday", "2023-1-14");
    map.put("ID_CARD", "123456789987654321");
    map.put("PHONE", "18047300222");
    map.put("EMAIL", "123456789@qq.com");
    map.put("nowdate", formatter.format(new Date()));
    /////////////////////////////////////////////
    //        map.put("id","7");
    //        map.put("username","pdf对象7");
    //        map.put("address","E盘7");
    //        map.put("sex","男7");
    //        map.put("birthday","2023-1-17");
    //        map.put("ID_CARD","123456789987654327");
    //        map.put("PHONE","18047300227");
    //        map.put("EMAIL","123456787@qq.com");
    userMap.put("user", map);
    //        map2.put("id","6");
    //        map2.put("username","pdf对象3");
    //        map2.put("address","E盘3");
    //        map2.put("sex","男3");
    //        map2.put("birthday","2023-1-14");
    //        map2.put("ID_CARD","123456789987654323");
    //        map2.put("PHONE","18047300223");
    //        map2.put("EMAIL","123456783@qq.com");
    //        userMap.put("user",map2);
    PdfUtil.pdfOut(userMap);
  }

  /***
   * @Author: aiYunS
   * @Description: 测试纯JDBC流式查询一次性读取400w数据量: 优化数据处理和分批插入
   * 耗时主要在分析400万数据量,采取分片多线程处理,分批插入.
   * @Date: 2023年2月2日, 0002 下午 3:08:34
   * @return: void
   */
  @Test
  public void test22() throws Exception {
    Connection connection = DBUtil.getConnection();
    Statement statement =
        connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    statement.setFetchSize(Integer.MIN_VALUE);
    List<ProjbaseException> projbaseExceptionList = new ArrayList<>();
    long start = System.currentTimeMillis();
    ResultSet rs = statement.executeQuery("SELECT jsonObj FROM projbase");
    System.out.println(
        "  🚀🚀🚀 400万数据量查询耗时 :: {} " + (System.currentTimeMillis() - start) / 1000 + " 秒");

    // java8 Stream

    // 总数据量
    long count = projBaseDao.selectCarArchivesList_COUNT();
    // 每片的数据量
    long piece = 200000;
    // 循环次数
    long num = count % piece == 0 ? count / piece : count / piece + 1;
    // 结果集分片
    long start2 = System.currentTimeMillis();
    for (int i = 1; i <= num; i++) {
      junitTest junitTest = new junitTest();
      list = new ArrayList();
      while (rs.next()) {
        if (list.size() != 0 && list != null && list.size() % piece == 0) {
          // 启用多线程,且分批插入
          junitTest.setList(list);
          Thread t = new Thread(junitTest);
          t.start();
          break;
        }
        list.add(rs.getString("jsonObj"));
      }
    }
    System.out.println(
        "  🚀🚀🚀 400万数据量分片耗时 :: {} " + (System.currentTimeMillis() - start2) / 1000 + " 秒");
  }

  @Override
  public void run() {
    int pc = 1;
    ArrayList<ProjbaseException> projbaseExceptionList = null;
    for (Object jsonObj : list) {
      projbaseExceptionList = new ArrayList<>();
      ProjbaseException projbaseException = new ProjbaseException();
      JSONObject obj = JSON.parseObject(jsonObj.toString());
      // 获取登录人名称
      String recvUserName = "";
      if (obj.getString("recvUserName") != null) {
        recvUserName = obj.getString("recvUserName");
      }
      // 获取查询人姓名
      JSONObject affFormInfo = JSON.parseObject(obj.getString("affFormInfo"));
      String sqrname = "";
      if (affFormInfo.getString("sqrname") != null) {
        sqrname = affFormInfo.getString("sqrname");
      }
      // 判断不一致信息
      if (!recvUserName.equals(sqrname) && sqrname.length() < 7) {
        projbaseException.setProjId(obj.getString("projId"));
        if (obj.getString("gmtApply") != null && !"".equals(obj.getString("gmtApply"))) {
          projbaseException.setCjsj(obj.getString("gmtApply"));
        } else {
          if (obj.getString("cjsj") != null && !"".equals(obj.getString("cjsj"))) {
            projbaseException.setCjsj(obj.getString("cjsj"));
          } else {
            projbaseException.setCjsj(null);
          }
        }
        projbaseException.setProjectName(obj.getString("projectName"));
        projbaseException.setApplyName(affFormInfo.getString("sqrname"));
        projbaseException.setApplyCardNo(affFormInfo.getString("zjh"));
        projbaseException.setQxdm(affFormInfo.getString("xzqbm"));
        projbaseException.setRecvUserName(obj.getString("recvUserName"));
        projbaseException.setRecvDeptCode(obj.getString("recvDeptName"));
        projbaseException.setRecvUserId(obj.getString("recvUserId"));
        projbaseException.setFaceValidationResult(obj.getString("faceValidationResult"));
        projbaseExceptionList.add(projbaseException);

        if (projbaseExceptionList != null && projbaseExceptionList.size() > 0) {
          if (projbaseExceptionList.size() == 2500) {
            System.out.println("第" + pc + "批次,开始插入: " + projbaseExceptionList.size() + "条数据...");
            long start1 = System.currentTimeMillis();
            projBaseDao.writeProjbaseException(projbaseExceptionList);
            System.out.println(" 批量插入数据耗时: " + (System.currentTimeMillis() - start1) / 1000 + " 秒");
            // 插入5w条数据后,清空list
            projbaseExceptionList.clear();
            projbaseExceptionList = null;
            ++pc;
          }
        }
      }
    }
  }

  // 测试springboot框架集成rabbitmq消息中间件
  //    @Autowired
  //    DirectProvider directProvider;
  //    @Test
  //    public void test10() throws InterruptedException {
  //        for(int i=1;i<10000;i++){
  //            directProvider.send(i);
  //        }
  //    }

  // 测试多数据源配置
  //    @Test
  //    public void test9() {
  //        List<User> users = userRepository2.findAllTwo();
  //        System.out.println(users);
  //    }

  // 测试ResultSetToList工具类
  @Test
  public void test24() {
    Connection conn = null;
    Statement sta = null;
    ResultSet rs = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn =
          DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/tinkerplay?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC",
              "root",
              "19930218");
      sta = conn.createStatement();
      rs = sta.executeQuery("select * from user");
      List list = ResultSetToListUtil.resultSetTolist(rs);
      System.out.println(list);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.release(conn, sta, null, rs);
    }
  }

  // 测试redis service
  @Test
  public void test25() {
    redisService.set("A", "a");
    redisService.set("B", "b", 30);
    redisService.set("C", "c");
    System.out.println(redisService.get("A"));
    redisService.expire("C", 30);
    System.out.println(redisService.getExpire("C"));
    redisService.del("A");
  }

  // 加密
  @Test
  public void testEncrypt() throws Exception {
    StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
    EnvironmentPBEConfig config = new EnvironmentPBEConfig();

    config.setAlgorithm("PBEWithMD5AndDES");
    config.setPassword("xxxxx");
    standardPBEStringEncryptor.setConfig(config);
    String plainText = "xxxxxxx";
    String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
    System.out.println("密码：" + encryptedText);
  }

  // 解密
  @Test
  public void testDe() throws Exception {
    StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
    EnvironmentPBEConfig config = new EnvironmentPBEConfig();

    config.setAlgorithm("PBEWithMD5AndDES");
    config.setPassword("xxxxx");
    standardPBEStringEncryptor.setConfig(config);
    String encryptedText = "dcIi+uEmTIuWfrJ1vpbk5sadABUbXpMz";
    String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
    System.out.println(plainText);
  }

  // 测试ThreadLocal
  @Test
  public void test26() {
    // 创建一个ThreadLocal对象
    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    // 创建两个线程，并启动
    Thread thread1 =
        new Thread(
            () -> {
              // 在线程1中设置ThreadLocal的值为1
              threadLocal.set(1);
              System.out.println("Thread 1: " + threadLocal.get()); // 输出：Thread 1: 1
            });

    Thread thread2 =
        new Thread(
            () -> {
              // 在线程2中设置ThreadLocal的值为2
              threadLocal.set(2);
              System.out.println("Thread 2: " + threadLocal.get()); // 输出：Thread 2: 2
            });

    thread1.start();
    thread2.start();

    // 在主线程中获取ThreadLocal的值（主线程不受影响）
    System.out.println("Main Thread: " + threadLocal.get()); // 输出：Main Thread: null

    // 清除ThreadLocal的值
    threadLocal.remove();
  }

  // 测试文件压缩工具类
  @Test
  public void test27() {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String sourceFolderPath = "E:\\aiyuns\\DatabaseBackup\\20230626";
    String zipFilePath = sourceFolderPath + "\\" + sdf.format(new Date()) + ".zip";

    try {
      FileOutputStream fos = new FileOutputStream(zipFilePath);
      ZipOutputStream zos = new ZipOutputStream(fos);

      File sourceFolder = new File(sourceFolderPath);
      FileCompressionUtil.compressFolder(sourceFolder, sourceFolder.getName(), zos);

      zos.close();
      fos.close();

      System.out.println("文件压缩成功！");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 测试解析SQL
  @Test
  public void test28() {
    String sql1 =
        "ALTER TABLE alterSQL ADD COLUMN column_1 int DEFAULT null COMMENT 'comment_1', ADD COLUMN"
            + " column_2 varchar DEFAULT null COMMENT 'comment_2'";
    System.out.print(AnalyzeSqlUtil.analyze(sql1));
    System.out.println();

    String sql2 =
        "CREATE TABLE `account`  (\n"
            + "  `id` int(11) NOT NULL,\n"
            + "  `uid` int(11) NULL DEFAULT NULL,\n"
            + "  `money` double NULL DEFAULT NULL,\n"
            + "  PRIMARY KEY (`id`) USING BTREE\n"
            + ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ="
            + " '某某表' ROW_FORMAT = Dynamic";
    System.out.print(AnalyzeSqlUtil.analyze(sql2));
    System.out.println();

    String sql3 =
        "update p_item_serv_pub set col_nm_en = 'bthAdr', col_nm_en_2 = 'bthAdr_2' where sys_no ="
            + " 'MI' AND tb_nm_en = 'c_cus_priv_ext' and col_nm_en = 'dbthAdr'";
    System.out.print(AnalyzeSqlUtil.analyze(sql3));
    System.out.println();

    String sql4 =
        "INSERT INTO p_tran_auth_config(trd_cd, chnl_no, sys_no) VALUES('divisionQry', '', '11')";
    System.out.print(AnalyzeSqlUtil.analyze(sql4));
    System.out.println();

    String sql5 =
        "REPLACE INTO p_tran_auth_config(trd_cd, chnl_no, sys_no) VALUES ('divisionQry', 'MI', '')";
    System.out.print(AnalyzeSqlUtil.analyze(sql5));
    System.out.println();

    String sql6 = "delete from p_tran_auth_config where trd_cd = 'divisionQry' or sys_no = '11'";
    System.out.print(AnalyzeSqlUtil.analyze(sql6));
    System.out.println();

    String sql7 = "drop table if exists p_tran_auth_config";
    System.out.print(AnalyzeSqlUtil.analyze(sql7));
    System.out.println();

    String sql8 =
        "ALTER TABLE alterSQL modify column_1 char(64) character set utf8mb4 collate utf8mb4_bin"
            + " NOT NULL comment '字段说明'";
    System.out.print(AnalyzeSqlUtil.analyze(sql8));
    System.out.println();

    String sql9 =
        "CREATE INDEX uk_c_cus_rel_ptu_user_id_idx USING BTREE ON eicc.c_cus_rel_ptu (user_id)";
    System.out.print(AnalyzeSqlUtil.analyze(sql9));
    System.out.println();

    String sql10 = "CREATE UNIQUE INDEX index_name ON table_name (indexColumn1, indexColumn2)";
    System.out.print(AnalyzeSqlUtil.analyze(sql10));
    System.out.println();

    String sql11 = "CREATE UNIQUE INDEX idx_username ON users (username, age) USING BTREE";
    System.out.print(AnalyzeSqlUtil.analyze(sql11));
    System.out.println();
  }
}
