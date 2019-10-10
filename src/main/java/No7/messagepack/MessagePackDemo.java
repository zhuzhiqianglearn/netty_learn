package No7.messagepack;

import org.json.simple.JSONObject;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessagePackDemo {
    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");
        MessagePack messagePack=new MessagePack();
        byte[] write = messagePack.write(src);
        List<String> read = messagePack.read(write, Templates.tList(Templates.TString));
        for (String s : read) {
            System.out.println(s);
        }

    }
}
