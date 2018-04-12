package com.tr.nebula.core.bundle;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by kamilbukum on 09/03/2017.
 */
public class BundleScanner {
    private static final int DEFAULT_ORDER = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(BundleScanner.class);

    public static List<Class<? extends Bundle>> scan(Reflections reflections) {
        Set<Class<? extends Bundle>> bundleClasses = reflections.getSubTypesOf(Bundle.class);
        List<Class<? extends Bundle>> bundles = new LinkedList<>();
        for (Class<? extends Bundle> bundleClass : bundleClasses) {
            LOGGER.info("Found Bundle " + bundleClass.getName());
            bundles.add(bundleClass);
        }

        bundles.sort(new Comparator<Class<? extends Bundle>>() {
            @Override
            public int compare(Class<? extends Bundle> o1, Class<? extends Bundle> o2) {
                int o1Order = getOrder(o1);
                int o2Order = getOrder(o2);
                return o1Order - o2Order;
            }
        });
        return bundles;
    }

    public static int getOrder(Class<? extends Bundle> o1) {
        int orderValue;
        BundleConfig bundleConfig = o1.getAnnotation(BundleConfig.class);
        orderValue = bundleConfig == null ? DEFAULT_ORDER : bundleConfig.order();
        return orderValue;
    }
}
