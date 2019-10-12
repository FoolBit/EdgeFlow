package functionTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.*;

public class FuncTest implements Serializable{
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Integer> a = new ArrayList<>();
        a.add(123);
        System.out.println(a.get(0));
        a.set(0, 12);
        System.out.println(a.get(0));


    }
}

