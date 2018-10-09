package com.tr.nebula.core.bundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.Global;
import com.tr.nebula.common.converter.PropertyConverter;
import com.tr.nebula.common.converter.PropertySource;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mustafa Erbin on 09/03/2017.
 */

public class BundleStarter {

    private ConcurrentLinkedQueue<Bundle> startedBundles = new ConcurrentLinkedQueue<>();

    /**
     * Bundle Finder
     *
     * @param reflections
     */
    public List<Bundle> getBundles(Reflections reflections) {
        Reflections bundleReflection = new Reflections(Global.class.getPackage().getName());
        if (reflections != null) {
            reflections = reflections.merge(bundleReflection);
        } else {
            reflections = bundleReflection;
        }
        List<Class<? extends Bundle>> bundles = BundleScanner.scan(reflections);
        List<Bundle> bundleInstances = new LinkedList<>();
        for (Class<? extends Bundle> bundleClass : bundles) {
            try {
                Bundle instance = bundleClass.newInstance();
                bundleInstances.add(instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return bundleInstances;
    }

    public <T> TypeReference<T> getTypeReference(final Class<T> tClass) {
        return new TypeReference<T>() {
        };
    }

    public void onStart(PropertySource<InputStream> propertySource, BundleContext context, Reflections reflections) {
        List<Bundle> bundles = getBundles(reflections);
        Map<String, Object> map = null;
        if (propertySource == null) {
            map = new LinkedHashMap<>();
        } else {
            try {
                map = PropertyConverter.convert(propertySource, Map.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        for (Bundle bundle : bundles) {
            if (bundle.getPropertyName() != null) {
                String[] props = bundle.getPropertyName().split("\\.");
                int i = 0;
                Object property = map;
                do {
                    Object child = ((Map<String, Object>) property).get(props[i]);
                    if (child != null) {
                        property = child;
                    } else {
                        property = null;
                        break;
                    }
                    i++;
                } while (i < props.length);
                if (bundle.getTypeReference() != null) {
                    property = PropertyConverter.convert(property, bundle.getTypeReference());
                } else if (bundle.getType() != null) {
                    property = PropertyConverter.convert(property, bundle.getType());
                }
                bundle.onStart(property, context);
            }
        }
    }

    public <P, S> void onStartBundle(BundleContext<S> context, Bundle<P, S> bundle, P property) {
        bundle.onStartBundle(property, context);
        startedBundles.add(bundle);
    }

    public synchronized void onStop(BundleContext context) {
        for (Bundle bundle : startedBundles) {
            bundle.onStop(context);
        }
        startedBundles.clear();
    }
}
