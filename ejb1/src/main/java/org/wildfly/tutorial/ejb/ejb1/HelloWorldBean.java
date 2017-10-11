package org.wildfly.tutorial.ejb.ejb1;

import org.wildfly.tutorial.ejb.ejb2.HelloUniverse;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.sasl.SaslMechanismSelector;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Stateless
@Remote(HelloWorld.class)
@PermitAll // need to use any security annotation to obtain caller principal
public class HelloWorldBean implements HelloWorld {

    @Resource
    private SessionContext ejbContext;

    //@EJB(lookup = "ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse")
    private HelloUniverse helloUniverse;

    private HelloUniverse helloUniverse2;

    public String sayHello() {

        String x = "Hello World caller=" + ejbContext.getCallerPrincipal();

        x += AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.empty()
                        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("DIGEST-MD5"))
                        .useName("user2")
                        .usePassword("user2b")
                ).runAction(() -> {
                    try {
                        Properties properties = new Properties();
                        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                        //properties.put(Context.PROVIDER_URL, "remote+http://localhost:8180");
                        InitialContext context = new InitialContext(properties);
                        helloUniverse = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
                        return ", 1:" + helloUniverse.sayHi();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ", 1:" + e.toString();
                    }
                });
/*
        x += AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.empty()
                        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("DIGEST-MD5"))
                        .useName("user2")
                        .usePassword("user2b")
                ).runAction(() -> {
                    try {
                        Properties properties = new Properties();
                        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                        InitialContext context = new InitialContext(properties);
                        helloUniverse2 = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
                        return ", 2:" + helloUniverse2.sayHi();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ", 2:" + e.toString();
                    }
                });
*/
        return x;
    }

}
