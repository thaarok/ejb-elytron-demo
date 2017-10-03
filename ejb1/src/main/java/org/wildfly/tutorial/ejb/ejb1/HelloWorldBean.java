package org.wildfly.tutorial.ejb.ejb1;
import org.wildfly.tutorial.ejb.ejb2.HelloUniverse;

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

    @EJB(lookup = "ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse")
    private HelloUniverse helloUniverse;

    public String sayHello() {

        // Alternative 1: working
        /*
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory"); // not necessary
            InitialContext context = new InitialContext(properties);
            helloUniverse = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        */

        // Alternative 2: does not work for remote EJBs (lookups within the java: namespace only)
        //helloUniverse = (HelloUniverse) ejbContext.lookup("java:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
        
        try {
            return "Hello World caller=" + ejbContext.getCallerPrincipal() + ", " + helloUniverse.sayHi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
