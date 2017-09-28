package org.wildfly.tutorial.ejb.ejb2;
import javax.ejb.Remote;

@Remote
public interface HelloUniverse {
    String sayHi();
}