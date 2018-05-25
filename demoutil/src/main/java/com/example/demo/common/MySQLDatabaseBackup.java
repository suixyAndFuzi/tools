package com.example.demo.common;

import java.io.*;

/**
 * java 调用shell脚本
 *
 * @author
 */
public class MySQLDatabaseBackup {

    /**
     * Java代码实现MySQL数据库导出
     *
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static boolean exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) throws InterruptedException {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            Long time1 = System.currentTimeMillis();
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            System.out.println(" mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 --skip-extended-insert --no-create-info " + databaseName +" sf_ipt_patient_msg");
            Process process = Runtime.getRuntime().exec(" mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 --skip-extended-insert --no-create-info " + databaseName + " sf_ipt_patient_msg");
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                System.out.println((System.currentTimeMillis() - time1)/1000 );
                return true;
            }
        }catch (IOException e) {
            System.out.println("e" + e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                System.out.println("e" + e);
            }
        }
        return false;
    }


    public static void main(String[] args) {
        try {
            if (MySQLDatabaseBackup.exportDatabaseTool("10.1.1.32", "root", "ipharmacare", "/mnt/systemcenter/xy/2014-10-14", "2014-10-14.dump", "xy_sf")) {
                System.out.println("数据库成功备份！！！");
            } else {
                System.out.println("数据库备份失败！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("压缩");

            Process process = Runtime.getRuntime().exec("cd /mnt/systemcenter/xy;zip  -q -r  2014-10-14.zip 2014-10-14");
            process.waitFor();
//            System.out.println("删除");
//            process = Runtime.getRuntime().exec("rm -rf /mnt/systemcenter/xy/2014-10-14");
//            process.waitFor();


        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
