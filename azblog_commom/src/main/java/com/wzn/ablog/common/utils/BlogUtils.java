package com.wzn.ablog.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzn.ablog.common.entity.Admin;
import com.wzn.ablog.common.vo.Payload;
import com.wzn.ablog.common.vo.PortRoleData;
import com.wzn.ablog.common.vo.Result;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.*;

public class BlogUtils {

    public static void respMsg(HttpServletResponse response, Integer status, String msg, Object data) {
        try {
            Result result = new Result(status, msg, data);
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(result);
            response.setContentType("text/html; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(res);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String code() {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<6;i++) {
            stringBuffer.append(Integer.toHexString(new Random().nextInt(16)));
        }
        String code = stringBuffer.toString().toUpperCase();
        return code;
    }


    //转换实体类
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz){
        List<T> returnList = new ArrayList<T>();
        if(CollectionUtils.isEmpty(list)){
            return returnList;
        }
        Object[] co = list.get(0);
        Class[] c2 = new Class[co.length];
        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            if(co[i]!=null){
                c2[i] = co[i].getClass();
            }else {
                c2[i]=String.class;
            }
        }
        for (Object[] o : list) {
            Constructor<T> constructor = null;
            try {
                constructor = clazz.getConstructor(c2);
                returnList.add(constructor.newInstance(o));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return returnList;
    }

    public static String getWeek(DayOfWeek dayOfWeek) {
        String week = dayOfWeek.toString().toLowerCase();
        if (week.equals("monday")) {
            return "周一";
        } else if (week.equals("tuesday")) {
            return "周二";
        } else if (week.equals("tuesday")) {
            return "周三";
        } else if (week.equals("thursday")) {
            return "周四";
        } else if (week.equals("friday")) {
            return "周五";
        } else if (week.equals("saturday")) {
            return "周六";
        } else if (week.equals("weekday")) {
            return "周日";
        } else {
            return null;
        }

    }

    public static String getWeekNum(String weekDay) {
        if (weekDay.equals("周一")) {
            return "1";
        } else if (weekDay.equals("周二")) {
            return "2";
        } else if (weekDay.equals("周三")) {
            return "3";
        } else if (weekDay.equals("周四")) {
            return "4";
        } else if (weekDay.equals("周五")) {
            return "5";
        } else if (weekDay.equals("周六")) {
            return "6";
        } else if (weekDay.equals("周日")) {
            return "7";
        } else {
            return null;
        }
    }

    /**
     * 把对象转成Map
     */
    public static <T> Map<String, Object> objectToMap(T t) {
        if (t == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 格式化接口url
     * @param portUrl 接口url
     * @param urlType 接口类型
     * @return
     */
    public static String formatPort(String portUrl,String urlType){
        return portUrl + " -" + urlType;
    }

    //获取表中格式化的接口权限
    public static List<String> getPortRole(List<Object[]> objects) {
        List<PortRoleData> portRoleDataList = BlogUtils.castEntity(objects, PortRoleData.class);
        List<String> portRole = new ArrayList<>();
        for (PortRoleData portRoleData : portRoleDataList) {
            String role = formatPort(portRoleData.getPort_url(), portRoleData.getUrl_type());
            portRole.add(role);
        }
        return portRole;
    }
}
