package org.wildfly.tutorial.ejb.ejb2;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
@Remote(HelloUniverse.class)
@PermitAll // need to use any security annotation to obtain caller principal
public class HelloUniverseBean implements HelloUniverse {

    @Resource
    private SessionContext ejbContext;

    public HelloUniverseBean() {
    }

    public String sayHi() {
        return "Hello Universe caller=" + ejbContext.getCallerPrincipal();
    }

}