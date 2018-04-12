## Core

#### <a name="Bundle_System"></a>Bundle System

* Provides to configure bundles when system is starting.

* Configuration Example

```java
import FileAssetServlet;
import HttpAssetServlet;
import EncodingUtil;
import Bundle;
import BundleType;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.List;

/**
 * Created by kamilbukum on 09/03/2017.
 */

public class AssetBundle extends Bundle<List<AssetProperties>, ServletContext> {
     private BundleType type = new BundleType(AssetProperties.class, true);
        
        @Override
        public String getPropertyName() {
            return "assets";
        }
    
        @Override
        public BundleType getType() {
            return type;
        }
   
        public void onStart(P configuration, ServletContext context) {
        }
          
        public void onStop(ServletContext context) {
            
         }
                
        @Override
        public void onStart(List<AssetProperties> properties, ServletContext context) {
            for (AssetProperties asset: properties) {
                AssetServlet servlet = null;
                switch (asset.getType()) {
                    case filesystem:
                        servlet = new FileAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                        break;
                    case http:
                        servlet =  new HttpAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                        break;
                }
                if(servlet != null) {
                    ServletRegistration.Dynamic serviceServlet = context.addServlet(asset.getAssetsName(), servlet);
                    serviceServlet.addMapping(servlet.getUriPath());
                    serviceServlet.setAsyncSupported(true);
                    serviceServlet.setLoadOnStartup(2);
                }
            }
        }
}
```
