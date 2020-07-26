package com.wzn.ablog.admin;

import com.wzn.ablog.admin.config.AdminRsaKeyConfig;
import com.wzn.ablog.admin.dao.*;
import com.wzn.ablog.common.entity.*;
import com.wzn.ablog.common.utils.BlogUtils;
import com.wzn.ablog.common.utils.JwtUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AdminTest {

    @Autowired
    private PortDao PortDao;

    @Autowired
    private PortRoleDao portRoleDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PortDao portDao;

    @Test
    public void test1() {
        List<Port> all = PortDao.findAll();
        Map<Object, Object> allRoleMap = new HashMap<>();
        all.stream().forEach(Port -> {
            List<PortRole> roleIds = portRoleDao.findByPortId(Port.getId());
            roleIds.stream().forEach(PortRole -> {
                Role role = roleDao.findById(PortRole.getRole_id()).get();
                allRoleMap.put(Port.getPort_url(), role.getComments());
            });
        });
    }

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void test2() {
        System.out.println(encoder.encode("123456"));
    }

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test3() {
        Admin admin = new Admin();
        admin.setUsername("1");
        admin.setId("1111111");
        admin.setSex("男");
        Admin save = adminDao.save(admin);

        System.out.println(save.getId());
    }

    @Test
    public void test4() {
        List<Port> ports = portDao.findAll();
        //选中该角色所拥有的的权限
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Port port : ports) {
            Map<String, Object> map = BlogUtils.objectToMap(port);
            map.put("LAY_CHECKED", "true");
            maps.add(map);
        }

    }


    @Test
    public void test5() {
        Admin admin = new Admin();
        admin.setId("1");
        admin.setUsername("297130962@qq.com");
        Role role = new Role();
        role.setRole_name("ROOT");
        Role role1 = new Role();
        role1.setRole_name("ADMIN");
//        admin.getRoles().add(role);
//        admin.getRoles().add(role1);
//        String s = JwtUtils.generateTokenExpireInMinutes(admin, rsaKeyConfig.getPrivateKey(), 60 * 24 * 30);
//        System.out.println(s);
    }

    @Autowired
    private MenuDao menuDao;

    @Test
    public void test6() {
        String roles = "ADMIN";
        String[] roleName = roles.split(",");
        List<Menu> roles1 = menuDao.findByRoles(roles);
        for (int i = 0; i < roleName.length && roleName.length > 1; i++) {
            roles1.addAll(menuDao.findByRoles(roleName[i]));
        }
        roles1.stream().sorted((a, b) -> a.getId().compareTo(b.getId())).forEach(System.out::println);
        System.out.println(roles1.size());
    }

    @Test
    public void test7() throws FileNotFoundException {


            String url = "http://127.0.0.1:9999/encrypt";
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> msg = template.postForEntity(url, "123456", String.class);
            System.out.println(msg.getBody());

    }

}
