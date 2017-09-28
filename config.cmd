
# server-side
/socket-binding-group=standard-sockets/socket-binding=remoting:add(port=4447)
/subsystem=remoting/connector=remoting-connector:add(socket-binding=remoting, sasl-authentication-factory=application-sasl-authentication)

# client-side
/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=remote-ejb:add(host=localhost, port=4447)

# local auth
/subsystem=elytron/authentication-configuration=admin-cfg:add(sasl-mechanism-selector="JBOSS-LOCAL-USER", protocol=remote)

# digest auth
#/subsystem=elytron/authentication-configuration=admin-cfg:add(sasl-mechanism-selector=(!JBOSS-LOCAL-USER && DIGEST-MD5), credential-reference={clear-text="admin123+"}, authentication-name=admin, protocol=remote)

/subsystem=elytron/authentication-context=admin-ctx:add(match-rules=[{authentication-configuration=admin-cfg}])
/subsystem=remoting/remote-outbound-connection=remote-ejb-connection:add(authentication-context=admin-ctx, outbound-socket-binding-ref=remote-ejb)

# to set as default
#/subsystem=elytron:write-attribute(name=default-authentication-context,value=admin-ctx)

