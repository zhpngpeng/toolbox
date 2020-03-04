package com.tool.toolbox.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Administrator
 */
public class FileUtils {

    public static List<File> readFile(String filepath) {
        List<File> files = new ArrayList<>();
        File file = new File(filepath);
        if (!file.isDirectory()) {
            files.add(file);
        } else{
            String[] fileList = file.list();
            if(fileList!=null){
                for (String s : fileList) {
                    File readFile = new File(filepath + File.separator + s);
                    if (!readFile.isDirectory()) {
                        files.add(readFile);
                    } else {
                        readFile(filepath + File.separator + s);
                    }
                }
            }
        }
        return files;
    }

    public static void downLoadFiles(List<File> files,
                                     HttpServletRequest request, HttpServletResponse response){
        try {
            File file = new File(request.getSession().getServletContext().getRealPath("mybatis.zip"));
            if (!file.exists()){
                file.createNewFile();
            }
            response.reset();
            FileOutputStream ous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(ous);
            zipFile(files, zipOut);
            zipOut.close();
            ous.close();
            downloadZip(file, response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFile (List<File> files, ZipOutputStream outputStream) {
        if(files != null){
            files.forEach(val -> {
                zipFile(val, outputStream);
                delete(val);
            });
        }
    }
    private static void downloadZip(File file, HttpServletResponse response) {
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void zipFile(File inputFile, ZipOutputStream outputStream) {
        try {
            if(inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        if(files!=null){
                            for (File file : files) {
                                zipFile(file, outputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            if(files == null){
                return file.delete();
            }else {
                for (File f : files) {
                    delete(f);
                }
            }
            return file.delete();
        }
    }

}
