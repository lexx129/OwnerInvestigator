package classes;

import java.util.EventObject;

/**
 * Created by Lexx on 07.12.2016.
 */
public class SidFoundEvent extends EventObject {
    private String _sid;

    public SidFoundEvent(Object source, String sid) {
        super(source);
        _sid = sid;
    }

    public String get_sid(){
        return _sid;
    }
}
