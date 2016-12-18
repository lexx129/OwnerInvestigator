package methods;

import acl.ISetACLCOMServer;
import acl.events._ISetACLCOMServerEvents;
import com4j.EventCookie;
import com4j.util.ComObjectCollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.attribute.UserPrincipal;


public class AclManager {

    private static ISetACLCOMServer serverObj;
    private EventCookie cookie;

    int getDirectoryAccess;

    int getFileAccess(String username, File file) throws NoSuchMethodException, IllegalAccessException, InstantiationException {

        serverObj = acl.ClassFactory.createSetACLCOMServer();

//        cookie = serverObj.advise(_ISetACLCOMServerEvents.class, new _ISetACLCOMServerEvents() {
//            @Override
//            public void messageEvent(String message) {
////                super.messageEvent(message);
//                System.out.println(message);
//            }
//        });

        int returnCode = serverObj.sendMessageEvents(true);
        if (returnCode != 0) {                                   // успешно
            System.err.println("Can't initialize Message Events");
        }

        System.out.println("**Begin dumping owner info**");
        returnCode = serverObj.setObject(file.getAbsolutePath(), 1);    // указываем объект типа 1 - "файл"
        if (returnCode != 0) {
            System.err.println("Can't set object");
            return -1;
        }

        returnCode = serverObj.addAction(2);                    // 2 - режим вывода ACL
        if (returnCode != 0) {
            System.err.println("Can't set action");
            return -1;
        }

        returnCode = serverObj.setListOptions(0, 4, false, 3);  // настройка формата вывода:
        // 0 - формат SDDL; 4 - информация о владельце
        // false - пропускаем наследованные правила
        // 3 - SID + расшифровка
        if (returnCode != 0) {
            System.err.println("Can't set listing format");
            return -1;
        }

        returnCode = serverObj.setBackupRestoreFile(System.getProperty("user.home") + "\\bckp.file");
        // сохраняем текущего владельца в файл в домашней директории пользователя
        if (returnCode != 0) {
            System.err.println("Can't set backup file");
            return -1;
        }

        returnCode = serverObj.run();
        if (returnCode != 0) {
            System.err.println("Can't run phase 1");
            return -1;
        }

        System.out.println("**Owner successfully saved. \nSetting " + username + " as owner**");
        returnCode = serverObj.setAction(4); // Режим установки владельца
        if (returnCode != 0) {
            System.err.println("Can't set action (set owner)");
            return -1;
        }

        returnCode = serverObj.setOwner(username);
        if (returnCode != 0) {
            System.err.println("Can't set + " + username + " as owner");
            return -1;
        }

        returnCode = serverObj.run();
        if (returnCode != 0) {
            System.err.println("Can't run phase 2");
            return -1;
        }

        System.out.println("**Owner changed. \nAdding rights for reading**");
        returnCode = serverObj.setAction(1); // Режим добавления нового ACE
        if (returnCode != 0) {
            System.err.println("Can't set action (addAce)");
            return -1;
        }

        returnCode = serverObj.addACE(username, "read", 0, false, 1, 1);
        // даем пользователю "username" право "read";
        // правило не наследуется (0, false); правило дописывается (1) в DACL (1)
        if (returnCode != 0) {
            System.err.println("Can't add ACE");
            return -1;
        }

        returnCode = serverObj.run();
        if (returnCode != 0) {
            System.err.println("Can't run phase 3");
            return -1;
        }

        System.out.println("**'Read' granted. \nRestoring info about previous owner**");
        returnCode = serverObj.setAction(2048); // режим восстановления ACL
        if (returnCode != 0) {
            System.err.println("Can't set action (restore)");
            return -1;
        }

        returnCode = serverObj.run();
        if (returnCode != 0) {
            System.err.println("Can't run setACL with such settings!");
            return -1;
        }
        System.out.println("**Previous owner is restored**");
//        cookie.close();
        ComObjectCollector collector = new ComObjectCollector();
        collector.disposeAll();
        //   serverObj.dispose();
        return 0;

//         SetACL -on <name> -ot <type> -actn restore -bckp <backup_file_name> -rec cont_obj
    }

    int removeForbiddance(String username, File file) {

        serverObj = acl.ClassFactory.createSetACLCOMServer();

        cookie = serverObj.advise(_ISetACLCOMServerEvents.class, new _ISetACLCOMServerEvents() {
            @Override
            public void messageEvent(String message) {
//                super.messageEvent(message);
                System.out.println(message);
            }
        });

        int returnCode = serverObj.sendMessageEvents(true);
        if (returnCode != 0) {                                   // успешно
            System.err.println("Can't initialize Message Events");
        }

        System.out.println("**Removing all permissions for user " + username + " **");
        returnCode = serverObj.setObject(file.getAbsolutePath(), 1);
        if (returnCode != 0) {
            System.err.println("Can't set object");
            return -1;
        }

        returnCode = serverObj.addAction(4096); // 4096 - режим работы с правами по пользователям
        if (returnCode != 0) {
            System.err.println("Can't set action");
            return -1;
        }

        returnCode = serverObj.addTrustee(username, username, 512, 1); // 512 - режим удаления всех прав для пользователя
        if (returnCode != 0) {
            System.err.println("Can't delete permissions");
            return -1;
        }

        returnCode = serverObj.run();
        if (returnCode != 0) {
            System.err.println("Can't run phaze 0 (delete permissions)");
        } else System.out.println("**Access rigths for " + username + " were cleaned out.**");

        ComObjectCollector collector = new ComObjectCollector();
        collector.disposeAll();
        return 0;
    }
//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException {
//
//        UserPrincipal user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(System.getProperty("user.name"));
//        System.out.println(user.getName());
//        getFileAccess(user.getName());
//    }
}
