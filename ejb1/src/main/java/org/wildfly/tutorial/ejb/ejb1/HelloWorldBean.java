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
import java.util.Properties;

@Stateless
@Remote(HelloWorld.class)
@PermitAll // need to use any security annotation to obtain caller principal
public class HelloWorldBean implements HelloWorld {

    @Resource
    private SessionContext ejbContext;

    @EJB(lookup = "java:global/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse")
    private HelloUniverse helloUniverse;

    public String sayHello() {
        try {
            return "Hello World caller=" + ejbContext.getCallerPrincipal() + ", " + helloUniverse.sayHi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
