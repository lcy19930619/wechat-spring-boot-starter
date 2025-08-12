package net.jlxxw.wechat.function.utils;


import java.io.File;
import java.util.Base64;

public class ImageUtils {
    public static  void saveImage(byte[] bytes, File file){
        if (file.exists()) {
            file.delete();
        }
        String base64 = new String(bytes);
        base64 = base64.replaceAll("data:image/jpeg;base64,", "");
        // 将 base64 字符串，转化为图片，并存储到指定文件中
        byte[] decode = Base64.getDecoder().decode(base64);
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(decode);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
