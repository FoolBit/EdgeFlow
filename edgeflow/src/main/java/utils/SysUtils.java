package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SysUtils {
    int n;

    private static boolean execCmd(String cmd) throws IOException, InterruptedException {
        List<String> command = Arrays.asList(cmd.split(" "));
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        p.waitFor();
        return p.exitValue() == 0;

        /*
        InputStream is = p.getInputStream();
        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = bs.readLine()) != null) {
            System.out.println(line);
        }
        */
    }

    public static String toHex(int num)
    {
        num = 1<<16 | num;
        String bs = Integer.toHexString(num);
        return bs.substring(1);
    }

    public void init(String Dev) throws IOException, InterruptedException {
        String cmd = "tc qdisc add dev " + Dev +" root handle 1:  htb default 1";
        execCmd(cmd);
    }

    private void addTcClass(String Dev, int classid, int speed) throws IOException, InterruptedException {
        String cmd = "tc class add dev "+ Dev +" parent 1:1 classid 1:"+ classid
                +" htb rate "+ speed +" mbit";
        execCmd(cmd);
    }

    private void createCgroupDir(int classid) throws IOException, InterruptedException {
        String dirname = "/sys/fs/cgroup/net_cls/MyClass"+classid;
        String cmd = "mkdir " + dirname;
        execCmd(cmd);

        String value = "0x"+toHex(1)+toHex(classid);
        cmd = "echo " + value +" > " + dirname + "/net_cls.classid";
        execCmd(cmd);
    }

    public void initLimit(String Dev, int classid, int speed) throws IOException, InterruptedException {
        addTcClass(Dev, classid, speed);
        createCgroupDir(classid);
    }

    public void changeLimit(String Dev, int classid, int speed) throws IOException, InterruptedException {
        String cmd = "tc class change dev "+ Dev +" parent 1:1 classid 1:"+ classid
                +" htb rate "+ speed +" mbit";
        execCmd(cmd);
    }

    public void setLimit(long pid, int classid) throws IOException, InterruptedException {
        String dirname = "/sys/fs/cgroup/net_cls/MyClass"+classid;
        String cmd = "echo " + pid + " > " + dirname + "/tesks";
        execCmd(cmd);
    }

}
