package org.wildfly.tutorial.ejb.client;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.sasl.SaslMechanismSelector;
import org.wildfly.tutorial.ejb.ejb1.HelloWorld;
import org.wildfly.tutorial.ejb.ejb2.HelloUniverse;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.Properties;

public class Client {
    public static void main(String[] args) throws Exception {

        AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.empty()
                        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("DIGEST-MD5"))
                        .useRealm("ApplicationRealm")
                        .useName("user")
                        .usePassword("user")
                ).run(() -> {

            try {

                Properties properties = new Properties();
                properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
                properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                properties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
                Context context = new InitialContext(properties);

                HelloUniverse helloUniverse = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
                System.out.println(helloUniverse.sayHi());

                // ejb:/ejb1ear-1.0-SNAPSHOT/ejb1/HelloWorldBean!org.wildfly.tutorial.ejb.ejb1.HelloWorld
                HelloWorld bean = (HelloWorld) context.lookup("ejb:ejb1ear-1.0-SNAPSHOT/ejb1/HelloWorldBean!org.wildfly.tutorial.ejb.ejb1.HelloWorld");
                System.out.println(bean.sayHello());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }
}
