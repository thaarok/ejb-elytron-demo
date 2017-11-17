package org.wildfly.tutorial.ejb.client;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.sasl.SaslMechanismSelector;
import org.wildfly.security.ssl.SSLConfigurator;
import org.wildfly.security.ssl.SSLContextBuilder;
import org.wildfly.tutorial.ejb.ejb2.HelloUniverse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Properties;

public class Client {
    public static void main(String[] args) throws Exception {

        AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.empty()
                        //.setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("SCRAM-SHA-1-PLUS"))
                        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("SCRAM-SHA-1"))
                        .useName("user")
                        .usePassword("user")
                ).run(() -> {
            try {
                Properties properties = new Properties();
                properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
                properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                properties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
                Context context = new InitialContext(properties);

                HelloUniverse helloUniverse = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
                System.out.println(helloUniverse.sayHi());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("/home/jkalina/work/wildfly/build/target/wildfly-12.0.0.Alpha1-SNAPSHOT/standalone/configuration/keystore.jks"), "secret1".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.empty()
                        //.setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("SCRAM-SHA-1-PLUS"))
                        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("SCRAM-SHA-1"))
                        .useName("user")
                        .usePassword("user")
                ).withSsl(MatchRule.ALL, new SSLContextBuilder()
                        .setTrustManager((X509TrustManager) tmf.getTrustManagers()[0])
                        .setClientMode(true)
                        .build()
                ).run(() -> {
            try {
                Properties properties = new Properties();
                properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
                properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                properties.put(Context.PROVIDER_URL, "remote+https://localhost:8443");
                Context context = new InitialContext(properties);

                HelloUniverse helloUniverse = (HelloUniverse) context.lookup("ejb:/ejb2-1.0-SNAPSHOT/HelloUniverseBean!org.wildfly.tutorial.ejb.ejb2.HelloUniverse");
                System.out.println(helloUniverse.sayHi());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
