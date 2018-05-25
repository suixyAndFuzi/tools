package com.example.demo.common;

import java.io.*;


/**
 * io 读写操作
 */
public class WriteStreamAppend {


    public static void main(String[] args) {
        String fileName = "C:\\suixy\\time1\\sql_table.txt";
        folderIsExists(fileName);
        writeMethod(fileName);

    }



    /**
     * 36          * 使用FileWriter类往文本文件中追加信息
     * 37
     */
    public static void writeMethod(String fileName) {
        int count=1000;//写文件行数
        try {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则直接往kuka.txt中追加字符串
            FileWriter writer = new FileWriter(fileName, true);
            for (int i = 0; i < count; i++) {
                writer.write("测试java 文件操作\r\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //注意：上面的例子由于写入的文本很少，使用FileWrite类就可以了。但如果需要写入的
    //内容很多，就应该使用更为高效的缓冲器流类BufferedWriter。



    /**
     * 78          * 使用FileReader类读文本文件
     * 79
     */
    public static void readMethod1() {
        String fileName = "C:/kuka.txt";
        int c = 0;
        try {
            FileReader reader = new FileReader(fileName);
            c = reader.read();
            while (c != -1) {
                System.out.print((char) c);
                c = reader.read();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 100          * 使用BufferedReader类读文本文件
     * 101
     */
    public static void readMethod2() {
        String fileName = "c:/kuka.txt";
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * 判断文件夹为空 空生成
     *
     * @param path
     */
    public static void folderIsExists(String path) {
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
