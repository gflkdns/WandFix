package com.miqt.wand;


import java.util.HashMap;
import java.util.Map;

/**
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:35:40
 */
public class ClassInstall {
    private static final ObjectProvider activityProvider = new ObjectProvider();
    private static final Map<String, Inject> injectMap = new HashMap<>();

    public static void inject(Object o) {
        inject(o, null, activityProvider);
    }

    public static void inject(Object o, Map<String, Object[]> pramHouse) {
        inject(o, pramHouse, activityProvider);
    }

    private static void inject(Object host, Object object, Provider provider) {
        String className = host.getClass().getName();
        try {
            Inject inject = injectMap.get(className);

            if (inject == null) {
                Class<?> aClass = Wand.get().getContext().getClassLoader().loadClass(className + "$$ObjectInject");
                inject = (Inject) aClass.newInstance();
                injectMap.put(className, inject);
            }
            inject.inject(host, object, provider);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
