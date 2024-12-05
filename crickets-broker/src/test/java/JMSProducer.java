import jakarta.jms.*;

import javax.naming.*;
import java.util.*;

public class JMSProducer {

    private static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";

    public static void main(String[] args) throws Exception {
        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);

        Context namingContext = new InitialContext(env);
        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(DEFAULT_CONNECTION_FACTORY);

        Destination destination = (Destination) namingContext.lookup(DEFAULT_DESTINATION);

        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(destination, "Hello, JMS!");
            System.out.println("Nachricht gesendet.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
