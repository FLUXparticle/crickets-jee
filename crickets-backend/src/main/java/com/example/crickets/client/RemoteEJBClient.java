package com.example.crickets.client;

import javax.naming.*;
import java.util.*;

public class RemoteEJBClient {

    private final Properties jndiProperties;

    private final String jndiName;

    public RemoteEJBClient(String hostname, int remotingPort, String appName, String moduleName, String beanName, String interfaceName) {
        this.jndiProperties = new Properties();
        this.jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        this.jndiProperties.put(Context.PROVIDER_URL, "http-remoting://" + hostname + ":" + remotingPort);
        this.jndiProperties.put("jboss.naming.client.ejb.context", true);

        this.jndiName = "ejb:" + appName + "/" + moduleName + "/" + beanName + "!" + interfaceName;
    }

    @SuppressWarnings("unchecked")
    public <T> T lookup() throws NamingException {
        Context context = new InitialContext(jndiProperties);
        return (T) context.lookup(jndiName);
    }

}
