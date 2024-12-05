package com.example.crickets;

import com.example.crickets.data.*;
import com.example.crickets.service.*;

import javax.naming.*;
import java.util.*;

public class EJBClient {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("java.naming.factory.initial", "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put("java.naming.provider.url", "http-remoting://localhost:8081");
        props.put("jboss.naming.client.ejb.context", true);

        try {
            InitialContext context = new InitialContext(props);
            TimelineRemoteService bean = (TimelineRemoteService) context.lookup("ejb:/ROOT/TimelineRemoteServiceImpl!com.example.crickets.service.TimelineRemoteService");
            List<Post> ll = bean.search("ll");
            System.out.println(ll);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}