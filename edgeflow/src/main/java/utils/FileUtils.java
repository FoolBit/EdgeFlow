package utils;

import functionTest.FuncTest;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class FileUtils {
    public static String getFullFilename(String filename)
    {
        return String.valueOf(Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(filename)).getPath());
    }

    public static String readFile(String filename){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
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

    public static void writeFile(String filename, String filedata) throws IOException {
        byte[] data = Base64.getDecoder().decode(filedata);
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(data,0,data.length);
    }

}
