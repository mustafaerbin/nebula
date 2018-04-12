package com.tr.nebula.security.ldap.service;

import com.tr.nebula.security.api.ldap.NebulaLdapService;
import com.tr.nebula.security.ldap.domain.User;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

/**
 * Created by Mustafa Erbin on 1.06.2017.
 */
@Service
public class NebulaLdapServiceImpl implements NebulaLdapService {

    final static Logger logger = LoggerFactory.getLogger(NebulaLdapServiceImpl.class);

    private static LdapConnectionPool pool;

    private static String domain = "";

    private static String host;

    private static String port;

    private static String filterDn;

    private static String filterFieldName;

    private static String userNameAttribute;

    private static String otherFilter;

    public static void init(String host, String port, String domain, String filterDn, String filterFieldName, String userNameAttribute, String otherFilter) {
        try {

            NebulaLdapServiceImpl.domain = domain;
            NebulaLdapServiceImpl.host = host;
            NebulaLdapServiceImpl.port = port;
            NebulaLdapServiceImpl.filterDn = filterDn;
            NebulaLdapServiceImpl.filterFieldName = filterFieldName;
            NebulaLdapServiceImpl.userNameAttribute = userNameAttribute;
            NebulaLdapServiceImpl.otherFilter = otherFilter;

            //String adminStr = adminUser + "," + domain;
//            String adminStr = adminUser;// + "," + domain;
//
//            logger.info("adminStr : " + adminStr);
//
//            logger.info("host : " + host);
//            logger.info("port : " + port);
//            logger.info("adminPass : " + adminPass);
//            logger.info("domain : " + domain);
//
//            LdapShaPasswordEncoder ldapShaPasswordEncoder = new LdapShaPasswordEncoder();
//            String adminPassHash = ldapShaPasswordEncoder.encodePassword(adminPass,null);
//
//            logger.info("adminPassHash : " + adminPassHash);
//
//            LdapConnectionConfig lconfig = new LdapConnectionConfig();
//
//            lconfig.setLdapHost(host);
//            lconfig.setLdapPort(Integer.valueOf(port));
//            lconfig.setName(adminStr);
//            lconfig.setCredentials(adminPass);
//
//            // Create connection pool
//            PoolableLdapConnectionFactory factory = new PoolableLdapConnectionFactory(lconfig);
//            pool = new LdapConnectionPool(factory);
//            pool.setTestOnBorrow(true);
//            pool.setMaxActive(100);
//            pool.setMaxWait(3000);
//            pool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
        } catch (Exception ex){
            logger.info("HATA OLUSTU");
            logger.info(ex.getMessage());
        }
    }

    private TrustManager createCustomTrustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        };
    }

    private KeyManager[] createCustomKeyManagers() {
        KeyManager[] bypassKeyManagers = new KeyManager[]{new X509KeyManager() {

            @Override
            public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
                return null;
            }

            @Override
            public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
                return null;
            }

            @Override
            public X509Certificate[] getCertificateChain(String arg0) {
                return null;
            }

            @Override
            public String[] getClientAliases(String arg0, Principal[] arg1) {
                return null;
            }

            @Override
            public PrivateKey getPrivateKey(String arg0) {
                return null;
            }

            @Override
            public String[] getServerAliases(String arg0, Principal[] arg1) {
                return null;
            }

        }};
        return bypassKeyManagers;
    }

    public static void destroy() {

        try {
            pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return new LDAP connection
     * @throws LdapException
     */
    public LdapConnection getConnection() throws LdapException {
        LdapConnection connection = null;
        try {
            logger.info("getConnection mehtodu calisacak......");
            connection = pool.getConnection();
            logger.info("getConnection mehtodu calisti......");
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new LdapException(e);
        }
        return connection;
    }

    /**
     * Try to release specified connection
     *
     * @param ldapConnection
     */
    public void releaseConnection(LdapConnection ldapConnection) {
        try {
            pool.releaseConnection(ldapConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find user LDAP entry from given DN parameter. Use this method only if you
     * want to <b>read his/her (task and report) privileges</b>, otherwise use
     * getEntry() or search() methods since they are more efficient.
     *
     * @param userDn
     * @return
     * @throws LdapException
     */
    public User getUser(String userDn, String password) {

//        LdapConnection connection = null;
//        User user = null;
//        if(null != NebulaLdapServiceImpl.uidAttribute)
//            userDn = NebulaLdapServiceImpl.uidAttribute + "="  + userDn + "," + NebulaLdapServiceImpl.domain;
//        //user = (User) SecurityCache.get("ldap:getuser:" + userDn);
//        logger.info("userDn : " + userDn);
//        logger.info("password : " + password);
////        if (user != null) {
////            return user;
////        }
//
//        try {
//
//            LdapConnectionConfig lconfig = new LdapConnectionConfig();
//
//            lconfig.setLdapHost(host);
//            lconfig.setLdapPort(Integer.valueOf(port));
//            lconfig.setName(userDn);
//            lconfig.setCredentials(password);
//
//            // Create connection pool
//            PoolableLdapConnectionFactory factory = new PoolableLdapConnectionFactory(lconfig);
//            pool = new LdapConnectionPool(factory);
//            pool.setTestOnBorrow(true);
//            pool.setMaxActive(100);
//            pool.setMaxWait(3000);
//            pool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
//
//            connection = getConnection();
//            Entry resultEntry = connection.lookup(userDn);
//            if (null != resultEntry) {
//                logger.info("****************************************************************");
//                for (Attribute attribute : resultEntry.getAttributes()) {
//                    logger.info(attribute.getId() + " : " + attribute.get());
//                }
//                logger.info("****************************************************************");
//
//                user = new User();
//
//                if (null != resultEntry.get(NebulaLdapServiceImpl.uidAttribute)) {
//                    user.setUid(resultEntry.get(NebulaLdapServiceImpl.uidAttribute).getString());
//                }
//
//                if (null != resultEntry.get(NebulaLdapServiceImpl.userNameAttribute)) {
//                    user.setUserName(resultEntry.get(NebulaLdapServiceImpl.userNameAttribute).getString());
//                }
//
//                if (null != resultEntry.get("userPassword")) {
//                    user.setPassword(new String(resultEntry.get("userPassword").getBytes()));
//                } else
//                    user.setPassword("");
////                SecurityCache.put("ldap:getuser:" + userDn, user);
//
//                return user;
//            }
//
//            return null;
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            releaseConnection(connection);
//        }
//        return null;

        return authenticate(userDn, password);
    }

    private void addUserPrivilege(User user, String privilege) {

    }
    public static User authenticate(String user, String password) {
        // microsoft logon name kullanılarak giriş yapılıyor
        // user ismi ile+@+serverDomain olarak yazılıyor
        String userLogonName = user + "@" + NebulaLdapServiceImpl.domain;
        if (password == null || password.isEmpty() || password.equals(""))
            return null;
        try {
            // Set up the environment for creating the initial context
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://"+ NebulaLdapServiceImpl.host + ":" + NebulaLdapServiceImpl.port);//TODO
            env.put(Context.SECURITY_AUTHENTICATION, "simple");

            env.put(Context.SECURITY_PRINCIPAL, userLogonName); //we have 2 \\ because it's a escape char
            env.put(Context.SECURITY_CREDENTIALS, password);

            // Create the initial context

            DirContext ctx = new InitialDirContext(env);

            String domain = NebulaLdapServiceImpl.filterDn;
            String filter = "(&((&(objectCategory=Person)(objectClass=User)))";
            if(NebulaLdapServiceImpl.otherFilter != null && !NebulaLdapServiceImpl.otherFilter.equals(""))
                filter += NebulaLdapServiceImpl.otherFilter;


            userLogonName = user;
            filter+= "(" + NebulaLdapServiceImpl.filterFieldName + "=" + userLogonName + ")";

            filter += ")";
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = ctx.search( domain, filter, searchControls );

            User userObj =  new User();

            if (results.hasMore()){
                SearchResult result = results.next();
                if(result.getAttributes().get(NebulaLdapServiceImpl.userNameAttribute) != null)
                    userObj.setUserName(result.getAttributes().get(NebulaLdapServiceImpl.userNameAttribute).get().toString());
                if(result.getAttributes().get("name") != null)
                    userObj.setName(result.getAttributes().get("name").get().toString());
                userObj.setPassword("");
            }

            if (ctx != null)
                ctx.close();

            return userObj;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }


//    public static void main(String[] args) {
//        /*host: 10.0.3.248
//        port: 389
//        domain: tgs.port
//        filterDn: dc=Users,dc=tgs,dc=port
//        filterFieldName: userPrincipalName
//        userNameAttribute: samaccountname*/
//
//        NebulaLdapServiceImpl.host = "10.0.3.248";
//        NebulaLdapServiceImpl.port = "389";
//        NebulaLdapServiceImpl.domain = "tgs.port";
//        NebulaLdapServiceImpl.filterDn = "DC=tgs,DC=port";
//        NebulaLdapServiceImpl.filterFieldName = "samaccountname";
//        NebulaLdapServiceImpl.userNameAttribute = "samaccountname";
//        NebulaLdapServiceImpl.otherFilter = "(memberOf=CN=Oprdocadminscr,CN=Users,DC=tgs,DC=port)";
////ilk b
//        //NebulaLdapServiceImpl.authenticate("LDAPuser","A123456+-s");
//        NebulaLdapServiceImpl.authenticate("bo013505","2412asmbo36+-kars34?");
//    }

//    public static void main(String[] args) throws Exception {
//
//        NebulaLdapServiceImpl.init("ldap.forumsys.com", "389", "ldap.forumsys.com", "dc=example,dc=com", "uid","sn");
//        NebulaLdapServiceImpl ldapService = new NebulaLdapServiceImpl();
//        //SecurityCache.init();
//        User user = ldapService.getUser("euclid","password");
//        System.out.println(user.toString());
//        ldapService.destroy();
//    }
}
