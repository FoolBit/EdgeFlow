package utils;

import functionTest.FuncTest;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;

public class FileUtils {

    public static boolean mkdir(String path)
    {
        File file = new File(path);
        System.out.println("Exists: "+file.exists());
        System.out.println("Is Directory: "+file.isDirectory());
        if(!file.exists() && !file.isDirectory())
        {
            boolean result = file.mkdirs();
            System.out.println("Result: "+result);
        }
        return true;
    }

    public static String[] getFileList(String path)
    {
        File file = new File(path);
        if(file.isDirectory())
        {
            System.out.println("Read successfully");

        }
        return file.list();
    }

    // 读取配置文件
    public static Properties getConfig(String filename) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filename);
        properties.load(inputStream);
        return properties;
    }

    // get full file path in dir resources
    public static String getFullFilename(String filename)
    {
        return String.valueOf(Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(filename)).getPath());
    }

    // read file data & convert data to string
    // filename here is full path
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
        fos.close();
    }

}
