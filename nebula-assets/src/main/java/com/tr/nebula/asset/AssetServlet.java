package com.tr.nebula.asset;

import javax.servlet.http.HttpServlet;

/**
 * Created by Mustafa Erbin on 07/03/2017.
 */
public abstract class AssetServlet extends HttpServlet {
    /**
     * resource path is reference to resource by {@link AssetType}
     * @return
     */
    abstract public String getResourcePath();

    /**
     * uri path is alias path to get resource
     * @return
     */
    abstract public String getUriPath();
}
