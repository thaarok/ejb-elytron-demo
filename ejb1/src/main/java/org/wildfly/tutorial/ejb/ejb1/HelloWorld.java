package org.wildfly.tutorial.ejb.ejb1;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
    String sayHello();
}
