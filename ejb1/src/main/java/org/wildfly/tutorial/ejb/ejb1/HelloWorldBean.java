package org.wildfly.tutorial.ejb.ejb1;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
@PermitAll // need to use any security annotation to obtain caller principal
public class HelloWorldBean implements HelloWorld {

    @Resource
    private SessionContext ejbContext;

    public HelloWorldBean() {
    }

    public String sayHello() {
        return "Hello World caller=" + ejbContext.getCallerPrincipal();
    }

}