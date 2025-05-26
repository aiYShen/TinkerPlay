package com.aiyuns.tinkerplay.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aiyuns.tinkerplay.Entity.ClientMsg;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author: aiYunS
 * @Date: 2022-9-14 下午 04:49
 * @Description: 获取IP和Mac地址工具类
 */
@Slf4j
public class MacUtil {

    private static final String LOCAL_IP = "127.0.0.1";
    /**
     * 获取IP地址
     *
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ( "0:0:0:0:0:0:0:1".equals(ip)) {
            return MacUtil.getLocalHostExactAddress();
        }
        return ip;
        // return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }


    /**
     * 根据ip获取归属地
     * @param ip
     * @return
     */
    public static String getAddress(String ip) {
        // IP地址查询
        final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip="+ip;

        // 未知地址
        final String UNKNOWN = "未知";
        // 内网不查询
        if (internalIp(ip)) {
            return "内网";
        }
        try {
            String rspStr = HttpUtil.doGet(IP_URL);
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }
            String substring = rspStr.substring(rspStr.lastIndexOf("{"), rspStr.indexOf("}")+1);
            JSONObject jsonObject = JSON.parseObject(substring);
            String addr = jsonObject.getString("addr");
            return addr;
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return UNKNOWN;
    }

//    public static void main(String[] args) throws Exception {
//        InetAddress ia = null;
//        try {
//            ia = InetAddress.getLocalHost();
//            String localname = ia.getHostName();
//            String localip = ia.getHostAddress();
//            System.out.println("本机名称是：" + localname);
//            System.out.println("本机的ip是 ：" + localip);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        InetAddress ia1 = InetAddress.getLocalHost();// 获取本地IP对象
//        System.out.println("本机的MAC是 ：" + getMACAddress(ia1));
//    }

    // 获取MAC地址的方法
    public static String getMACAddress(InetAddress ia) throws Exception {
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        if (!ia.toString().startsWith("/") && ia.toString().length() > 16) {
            return "";
        }
        NetworkInterface in = NetworkInterface.getByInetAddress(ia);
        if (in == null) {
            return "无法获取网卡信息，NetworkInterface 为空";
        }
        byte[] mac = in.getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            // System.out.println("--------------");
            // System.err.println(s);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }


    /**
     * 查看是否内部ip
     * @param ip
     * @return
     */
    public static boolean internalIp(String ip) {
        boolean res = false;
        byte[] addr = textToNumericFormatV4(ip);
        if (addr != null && ip != null) {
            res = internalIp(addr) || LOCAL_IP.equals(ip);
        }
        return res;
    }

    private static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        boolean flag = false;
        switch (b0) {
            case SECTION_1:
                flag = true;
                break;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    flag = true;
                }
                break;
            case SECTION_5:
                if (b1 == SECTION_6) {
                    flag = true;
                }
                break;
            default:
                break;
        }
        return flag;
    }

    /**
     * 将IPv4地址转换成字节
     *IPv4地址
     * @param text
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            log.error("数字格式化异常",e);
            return null;
        }
        return bytes;
    }

    /**
     * 获取本地ip
     * @return
     */
    public static String getLocalIP() {
        String ip = "";
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                ip = addr.getHostAddress();
            } catch (Exception e) {
                log.error("获取失败",e);
            }
            return ip;
        } else {
            try {
                Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
                        .getNetworkInterfaces();
                while (e1.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) e1.nextElement();
                    if (!"eth0".equals(ni.getName())) {
                        continue;
                    } else {
                        Enumeration<?> e2 = ni.getInetAddresses();
                        while (e2.hasMoreElements()) {
                            InetAddress ia = (InetAddress) e2.nextElement();
                            if (ia instanceof Inet6Address) {
                                continue;
                            }
                            ip = ia.getHostAddress();
                            return ip;
                        }
                        break;
                    }
                }
            } catch (SocketException e) {
                log.error("获取失败",e);
            }
        }
        return "";
    }

    public static ClientMsg getRequestMsg(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        ClientMsg ipVo =  new ClientMsg();

        //客户端类型  手机、电脑、平板
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        ipVo.setClientType(clientType);

        //操作系统类型
        String os = userAgent.getOperatingSystem().getName();
        ipVo.setSystemType(os);

        //请求ip
        /*  String ip = getIpAddr(request);
        ipVo.setIp(ip);*/
        //浏览器类型
        String browser = userAgent.getBrowser().toString();
        ipVo.setBrowserType(browser);

        return ipVo;

    }

    public static String getLocalHostExactAddress() {
        try {
            InetAddress candidateAddress = null;

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了 就是我们要找的
                            // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                            if (inetAddr.toString().contains("/")) {
                                return inetAddr.toString().replaceAll("/","");
                            }
                            return inetAddr.toString();
                        }

                        // 若不是site-local地址 那就记录下该地址当作候选
                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
            if (candidateAddress == null) {
                if (InetAddress.getLocalHost().toString().contains("/")) {
                    return InetAddress.getLocalHost().toString().replaceAll("/","");
                } else {
                    return InetAddress.getLocalHost().toString();
                }
            } else {
                if (candidateAddress.toString().contains("/")) {
                    return candidateAddress.toString().replaceAll("/","");
                } else {
                    return candidateAddress.toString();
                }
            }
            // return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
