package com.tr.nebula.asset;


import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.asset.file.FileAssetServlet;
import com.tr.nebula.asset.http.HttpAssetServlet;
import com.tr.nebula.asset.util.EncodingUtil;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleBean;
import com.tr.nebula.core.bundle.BundleContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.List;
import java.util.UUID;

/**
 * Created by kamilbukum on 09/03/2017.
 */
public class AssetBundle extends Bundle<List<AssetProperties>, ServletContext> {
    private static final TypeReference<List<AssetProperties>> LIST_TYPE_REFERENCE = new TypeReference<List<AssetProperties>>() {
    };

    @Override
    public String getPropertyName() {
        return "nebula.assets";
    }


    @Override
    public TypeReference<List<AssetProperties>> getTypeReference() {
        return LIST_TYPE_REFERENCE;
    }

    @Override
    public void onStart(List<AssetProperties> properties, BundleContext<ServletContext> bundleContext) {
        for (AssetProperties asset : properties) {
            AssetServlet servlet = null;
            switch (asset.getType()) {
                case filesystem:
                    servlet = new FileAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                    break;
                case http:
                    servlet = new HttpAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                    break;
            }
            if (servlet != null) {
                BundleBean bundleBean = new BundleBean(asset.getAssetsName(), servlet);
                bundleContext.getListener().onCreate(bundleBean);
                //Tomcat deploy için değiştirildi. Düzeltilecek.
                ServletRegistration.Dynamic serviceServlet = bundleContext.getContext().addServlet(asset.getAssetsName() + UUID.randomUUID().toString(), servlet);
//                System.out.println(servlet.getUriPath());
//                System.out.println("----------------*---------------");
//                System.out.println(serviceServlet);
                serviceServlet.addMapping(servlet.getUriPath());
                serviceServlet.setAsyncSupported(true);
                serviceServlet.setLoadOnStartup(2);
            }
        }
    }
}
