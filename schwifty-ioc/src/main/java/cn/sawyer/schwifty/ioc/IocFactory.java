package cn.sawyer.schwifty.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-01 19:12
 **/
public class IocFactory {

    public static final Map<String, Object> container = new ConcurrentHashMap<>(32);

    public static void addBean(Object obj) {
        container.put(obj.getClass().getName(), obj);
    }

    public static <T> T getBean(Class<T> clazz) {
        Object bean = getBean(clazz.getName());
        return clazz.cast(bean);
    }

    public static Object getBean(String name) {
        return container.get(name);
    }
}
