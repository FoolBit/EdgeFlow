package utils;

import java.io.*;

// 用来将类序列化/反序列化
public class ObjectUtils {

    // Serialize object to string
    // 将对象序列化，并存储为字符串
    // 当一个类 implements Serializable 之后，就可以进行序列化了
    public static String objectToString(Object obj) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        String str = byteOut.toString("ISO-8859-1");
        return str;
    }

    // Deserialize string to object
    // 将字符串反序列化为对象，返回值为Object类型，强制转化一下类型即可
    public static Object stringToObject(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        return object;
    }
}
