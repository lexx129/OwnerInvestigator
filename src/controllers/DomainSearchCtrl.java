package controllers;

import methods.LDAPConnection;
import classes.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

public class domainSearchCtrl {

    @FXML
    TextField login;
    @FXML
    PasswordField passwd;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    private Main main;

    private void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        this.cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void handleOk() throws UnknownHostException {
        Main.username = "CN=" + login.getText() + ", CN=Users";
        Main.password = passwd.getText();
        try {
            findOwnerInfo(Main.chosenUser.getSid(), Main.chosenUser.getName());
        } catch (AuthenticationException e) {
            popUpError(1);
        } catch (CommunicationException e) {
            popUpError(3);
        } catch (NamingException e) {
            popUpError(2);
        }
    }
    private void popUpError(int type) {
        Alert alert;
        switch (type) {
            case 1:
                alert = new Alert(Alert.AlertType.ERROR, "Неверный логин/пароль для входа в домен");
                alert.showAndWait();
                break;
            case 2:
                alert = new Alert(Alert.AlertType.ERROR, "Необходимо ввести логин/пароль для входа в домен");
                alert.showAndWait();
                break;
            case 3:
                alert = new Alert(Alert.AlertType.WARNING, "Не удалось найти ни одного контроллера домена в сети");
                alert.showAndWait();

                break;
        }
    }

    private void findOwnerInfo(String sid, String name) throws NamingException, UnknownHostException {
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
            ownerAttributes.add("Имя владельца: " + name);
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
                    ownerAttributes.add("Имя владельца: " + name);
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
