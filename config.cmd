# start as bin/standalone.sh

# server-side - run on both servers to enable elytron remoting authentication
# ***************************************************************************
/subsystem=remoting/http-connector=http-remoting-connector:write-attribute(name=sasl-authentication-factory, value=application-sasl-authentication)
/subsystem=remoting/http-connector=http-remoting-connector:undefine-attribute(name=security-realm)
reload

# client-side - run on server where ejb1 is deploed to allow connection to second server
# **************************************************************************************

/subsystem=elytron/authentication-configuration=admin-cfg:add(sasl-mechanism-selector=DIGEST-MD5, protocol="http-remoting", authentication-name="user2", credential-reference={clear-text="user2"})
/subsystem=elytron/authentication-context=admin-ctx:add(match-rules=[{authentication-configuration=admin-cfg}])

/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=second-wildfly:add(host=localhost, port=8180)
/subsystem=remoting/remote-outbound-connection=remote-ejb-connection:add(authentication-context=admin-ctx, outbound-socket-binding-ref=second-wildfly)

