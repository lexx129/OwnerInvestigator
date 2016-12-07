package controllers;

import classes.LDAPConnection;
import classes.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static classes.Main.ownerAttributes;
import static classes.Main.password;
import static classes.Main.username;

public class DomainSearchCtrl {

    @FXML
    TextField login;
    @FXML
    PasswordField passw;
    @FXML
    Button okButton;


    @FXML
    private void handleOk() throws UnknownHostException {
        Main.username = "CN=" + login.getText() + ", CN=Users";
        Main.password = passw.getText();
        try {
            findOwnerInfo();
        } catch (AuthenticationException e) {
            popUpError(1);
        } catch (CommunicationException e) {
            popUpError(3);
        } catch (NamingException e) {
            popUpError(2);
        }

    }

    public void findOwnerInfo(String sid) throws NamingException, UnknownHostException {

        ownerAttributes = new ArrayList<>();

        LDAPConnection ldap = new LDAPConnection();
//        узнаем имя домена, в котором находится текущая машина
        InetAddress address = InetAddress.getLocalHost();
        String hostnameCanonical = address.getCanonicalHostName();
        String domainName = hostnameCanonical.substring(hostnameCanonical.indexOf(".") + 1);
        List<String> ldapServers;
        try {
            ldapServers = LDAPConnection.findLDAPServersInWindowsDomain(domainName);
        } catch (CommunicationException e) {
            ownerAttributes.add("Имя владельца: " + owner.getName());
            ownerAttributes.add("SID владельца: " + sid);
            throw e;
        }
        if (ldapServers.isEmpty())
            throw new NamingException("Can't locate an LDAP server");
        int count = 0;

        for (String ldapServer : ldapServers) {
            try {
                count++;
                final String ldapAdServer = "ldap://" + ldapServer + ":389";

                String[] parsed = ldapServer.split("\\.");
                String ldapSearchBase = "dc=" + parsed[parsed.length - 2] + ", dc=" + parsed[parsed.length - 1];
                System.out.println("search base: " + ldapSearchBase);
                username = username + ", " + ldapSearchBase;

                Hashtable<String, Object> env = new Hashtable<>();
                env.put(Context.SECURITY_AUTHENTICATION, "simple");
                env.put(Context.SECURITY_PRINCIPAL, username);
                env.put(Context.SECURITY_CREDENTIALS, password);
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, ldapAdServer);
                env.put("java.naming.ldap.attributes.binary", "objectSID");

                LdapContext ctx = new InitialLdapContext(env, null);
                ownerAttributes = ldap.findUserBySID(ctx, ldapSearchBase, sid);
                if (ownerAttributes.size() == 0)
                    ownerAttributes.add("Имя владельца: " + owner.getName());
                ownerAttributes.add("SID владельца: " + sid);

//                    return ownerAttributes;

            } catch (CommunicationException e) {
                if (count == ldapServers.size()) {
                    throw e;
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
            System.out.println(ldapServer);
        }


    }


}
