package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SysUtils {
    int n;

    public static Process execCmd(String cmd) throws IOException, InterruptedException {
        return execCmd(cmd, false);
    }

    private static Process execCmd(String cmd, boolean wait) throws IOException, InterruptedException {
        List<String> command = Arrays.asList(cmd.split(" "));
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        if(wait)
        {
            p.waitFor();
            return p;
        }
        return p;
    }

    public static String toHex(int num)
    {
        num = 1<<16 | num;
        String bs = Integer.toHexString(num);
        return bs.substring(1);
    }

    public static void initTcQueue(String Dev) throws IOException, InterruptedException {
        String cmd = String.format("tc qdisc add dev %s root handle 1:  htb default 1", Dev);
        execCmd(cmd);
    }

    public static void addTcClass(String Dev, int classid, int speed) throws IOException, InterruptedException {
        String cmd = String.format("tc class add dev %s parent 1:1 classid 1:%d htb rate %d mbit",
                Dev, classid, speed);
        execCmd(cmd);
    }

    public static void createCgroupDir(int classid) throws IOException, InterruptedException {
        String dirname = "/sys/fs/cgroup/net_cls/MyClass"+classid;
        String cmd = "mkdir " + dirname;
        execCmd(cmd);

        String value = "0x"+toHex(1)+toHex(classid);
        cmd = String.format("echo %s > %s/net_cls.classid", value, dirname);
        execCmd(cmd);
    }

    public static void initSpeedLimit(String Dev, int classid, int speed) throws IOException, InterruptedException {
        addTcClass(Dev, classid, speed);
        createCgroupDir(classid);
    }

    public static void changeSpeedLimit(String Dev, int classid, int speed) throws IOException, InterruptedException {
        String cmd = String.format("tc class change dev %s parent 1:1 classid 1:%d htb rate %d mbit",
                Dev, classid, speed);
        execCmd(cmd);
    }

    public static void limitSpeedByPID(long pid, int classid) throws IOException, InterruptedException {
        String dirname = "/sys/fs/cgroup/net_cls/MyClass"+classid;
        String cmd = String.format("echo %d > %s/tasks", pid, dirname);
        execCmd(cmd);
    }

}
