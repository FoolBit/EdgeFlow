package utils;

import functionTest.FuncTest;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtils {
    public static String readFile(String filename){
        String file = String.valueOf(FuncTest.class.getClassLoader().getResource(filename).getPath());
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(buffer);
    }

    public static void writeFile(String filename, String file) throws IOException {
        byte[] data = Base64.getDecoder().decode(file);
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(data,0,data.length);
    }

}
