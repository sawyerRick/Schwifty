package cn.sawyer.schwifty.web.core;


import com.sun.org.apache.bcel.internal.util.ClassSet;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @program: schwifty
 * @description: 一个非常'SCRATCH'的启动类
 * @author: sawyer
 * @create: 2020-03-01 20:11
 **/

@Slf4j
public class SchwiftyApplication {

    Class primarySource;

    String[] args;

    public SchwiftyApplication(Class primarySource) {
        this.primarySource = primarySource;
    }

    public static void run(Class clazz, String... args) {
        new SchwiftyApplication(clazz).run(args);
    }

    private void runInside(Class clazz, String... args) {

    }

    private void echoBanner() {
        InputStream in = primarySource.getResourceAsStream("/banner.txt");
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        StringBuilder banner = new StringBuilder();
        int read = 0;
        try {
            while ((read = reader.read()) != -1) {
                banner.append((char) read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(banner.toString());
    }

    private void echoBootArgs() {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(",");
        }
        log.info("Boot args:" + sb.toString());
    }

    private void run(String... args) {
        this.args = args;
        echoBanner();
        echoBootArgs();
        // 启动信息
        log.info("banner:" + primarySource.getResource("/banner.txt"));
        log.info("SchwiftyApplication Boot !!! from:" + primarySource.getName());
        log.info("Resources:" + primarySource.getResource("/"));
        log.info("user.dir:" + System.getProperty("user.dir"));

        // 递归扫描用户类, 注册
        String basePath = primarySource.getResource("").getPath();
        String packageName = primarySource.getPackage().getName();
        Set<Class> set = new HashSet<>();
        recurScan(basePath, packageName, set);

        // 添加路由


        // ...
    }

    private void recurScan(String basePath, String packageName, Set<Class> classSet) {
        try {
            scanPath(basePath, packageName, classSet);
            log.info("Loaded [" + classSet.size() + "] classes...");
        } catch (Exception e) {
            log.error("用户类扫描失败...");
            e.printStackTrace();
        }
    }

    private void scanPath(String basePath, String packageName, Set<Class> classSet) {

        File file = new File(basePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    scanPath(f.getPath(), packageName + "." + f.getName(), classSet);
                }
            } else {
                try {
                    // 获得类的全限定名，去掉.class
                    String absClassName = packageName.substring(0, packageName.length() - 6);
                    // 加载类
                    Class clazz = Class.forName(absClassName);
                    log.info("Loading class:" + clazz.getName());
                    classSet.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
