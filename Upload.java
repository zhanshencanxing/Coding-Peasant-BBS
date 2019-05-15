package com.mengruo.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

public final class Upload {

    private Upload(){}


    /*指定了两个参数：
    request>必要，指HttpServlet的请求对象
    pathname>必要，指要存放的文件夹名称，方法会在target文件夹中搜索该文件夹名称
    由于没有指定限制的文件类型，默认为适应所有类型文件，可能会引起恶意传递，谨慎使用！
    由于没有指定最大的文件大小，方法会接受任意大小的文件，传递超大文件时，可能发生崩溃*/
    public static JSONObject uploadone(HttpServletRequest request, String pathname){
        //创建request的字节输入流
        ServletInputStream reader = null;
        try {
            reader = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取指定文件的真实地址
        String realPath = request.getSession().getServletContext().getRealPath(pathname);
        //创建字节输出流向指定文件输出内容
        FileOutputStream write= null;
        //定义b存储获取字节的长度
        int b=0;
        //定义字节数组存储获取到的一组字节
        byte by[]=new byte[1024];
        try {
            write = new FileOutputStream(new File(realPath+"\\temp.txt"));
            //循环将内容复制到临时文件
            while ((b=reader.read(by))>0){
                write.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                reader.close();
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //创建随机访问文件对象对临时文件以可读写模式进行访问
        RandomAccessFile randomAccessFile= null;
        String firstline=null,secondline=null,thirdline=null;
        try {
            randomAccessFile = new RandomAccessFile(new File(realPath+"\\temp.txt"),"rw");
            //获取第一行内容并移动游标
            firstline = randomAccessFile.readLine();
            //获取第二行内容并移动游标
            secondline = new String(randomAccessFile.readLine().getBytes("iso-8859-1"),"UTF-8");
            //获取第三行内容并移动游标
            thirdline = randomAccessFile.readLine();
            //读取第四行内容并移动游标
            randomAccessFile.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建json对象
        JSONObject formdatajson1=new JSONObject();
        //将第二行内容分割，用以获取formdata
        String[] split = secondline.split(";");
        //遍历每一组内容
        for (String s : split) {
            //去除空格
            s = s.trim();
            //将每一组再次分割，形成（K，V）形式
            String[] split1 = s.split("=");
            if (split1.length==2){
                //如果成功，存储到json对象中
                formdatajson1.put(split1[0],split1[1].substring(1,split1[1].length()-1));
            }
        }

        //使用UUID生成唯一字串
        String uuid = UUID.randomUUID().toString();
        //使用RandomAccessFile对象的设置长度方法的截取效果去除最后一行尾部信息
        try {
            randomAccessFile.setLength(randomAccessFile.length()-(firstline+"--\r\n\r\n").getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从json对象中获取filename的value值
        String filename = formdatajson1.getString("filename");
        //使用分隔方法会造成错误，弃用
        //String[] split1 = filename.split(".");
        //截取后缀名
        String suffix=filename.substring(filename.lastIndexOf("."),filename.length());
        //使用唯一字串与后缀名拼接形成新的文件名
        String newfilename=uuid+suffix;
        //创建字节输出流向指定位置输出内容
        FileOutputStream outputStream= null;
        try {
            outputStream = new FileOutputStream(new File(realPath+"\\"+newfilename));
            //循环读写操作
            while ((b=randomAccessFile.read(by))>0){
                outputStream.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                randomAccessFile.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将新的文件名放入json对象
        formdatajson1.put("newfilename",newfilename);
        return formdatajson1;
    }



    /*指定了三个参数：
    request>必要，指HttpServlet的请求对象
    pathname>必要，指要存放的文件夹名称，方法会在target文件夹中搜索该文件夹名称
    accept>必要，指限制的文件类型，传参时，每个文件类型需加'.'并以','分隔，组成的字符串即为标准参数
    由于没有指定最大的文件大小，方法会接受任意大小的文件，传递超大文件时，可能发生崩溃*/
    public static JSONObject uploadone(HttpServletRequest request, String pathname, String accept){
        //创建request的字节输入流
        ServletInputStream reader = null;
        try {
            reader = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取指定文件的真实地址
        String realPath = request.getSession().getServletContext().getRealPath(pathname);
        //创建字节输出流向指定文件输出内容
        FileOutputStream write= null;
        //定义b存储获取字节的长度
        int b=0;
        //定义字节数组存储获取到的一组字节
        byte by[]=new byte[1024];
        try {
            write = new FileOutputStream(new File(realPath+"\\temp.txt"));
            //循环将内容复制到临时文件
            while ((b=reader.read(by))>0){
                write.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                reader.close();
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //创建随机访问文件对象对临时文件以可读写模式进行访问
        RandomAccessFile randomAccessFile= null;
        String firstline=null,secondline=null,thirdline=null;
        try {
            randomAccessFile = new RandomAccessFile(new File(realPath+"\\temp.txt"),"rw");
            //获取第一行内容并移动游标
            firstline = randomAccessFile.readLine();
            //获取第二行内容并移动游标
            secondline = new String(randomAccessFile.readLine().getBytes("iso-8859-1"),"UTF-8");
            //获取第三行内容并移动游标
            thirdline = randomAccessFile.readLine();
            //读取第四行内容并移动游标
            randomAccessFile.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       //创建json对象
        JSONObject formdatajson1=new JSONObject();
        //将第二行内容分割，用以获取formdata
        String[] split = secondline.split(";");
        //遍历每一组内容
        for (String s : split) {
            //去除空格
            s = s.trim();
            //将每一组再次分割，形成（K，V）形式
            String[] split1 = s.split("=");
            if (split1.length==2){
                //如果成功，存储到json对象中
                formdatajson1.put(split1[0],split1[1].substring(1,split1[1].length()-1));
            }
        }

        //使用UUID生成唯一字串
        String uuid = UUID.randomUUID().toString();
        //使用RandomAccessFile对象的设置长度方法的截取效果去除最后一行尾部信息
        try {
            randomAccessFile.setLength(randomAccessFile.length()-(firstline+"--\r\n\r\n").getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从json对象中获取filename的value值
        String filename = formdatajson1.getString("filename");
        //使用分隔方法会造成错误，弃用
        //String[] split1 = filename.split(".");
        //截取后缀名
        String suffix=filename.substring(filename.lastIndexOf("."),filename.length());
        //判断文件类型是否符合要求
        String[] accepts = accept.split(",");
        boolean isaccept=false;
        for (String s:accepts){
            if (suffix.equals(s)){
                isaccept=true;
                break;
            }
        }
        if (isaccept){
            formdatajson1.put("flag",true);
        }else{
            formdatajson1.put("flag",false);
            formdatajson1.put("error","格式错误，支持格式："+accept);
            return formdatajson1;
        }
        //使用唯一字串与后缀名拼接形成新的文件名
        String newfilename=uuid+suffix;
        //创建字节输出流向指定位置输出内容
        FileOutputStream outputStream= null;
        try {
            outputStream = new FileOutputStream(new File(realPath+"\\"+newfilename));
            //循环读写操作
            while ((b=randomAccessFile.read(by))>0){
                outputStream.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                randomAccessFile.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将新的文件名放入json对象
        formdatajson1.put("newfilename",newfilename);
        return formdatajson1;
    }


    /*指定了四个参数：
    request>必要，指HttpServlet的请求对象
    pathname>必要，指要存放的文件夹名称，方法会在target文件夹中搜索该文件夹名称
    accept>必要，指限制的文件类型，传参时，每个文件类型需加'.'并以','分隔，组成的字符串即为标准参数,任意类型请加“*”
    maxsize>必要，指限制的文件大小，参数为整数，单位为MB*/
    public static JSONObject uploadone(HttpServletRequest request, String pathname, String accept, int maxsize){
        //创建request的字节输入流
        ServletInputStream reader = null;
        try {
            reader = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取指定文件的真实地址
        String realPath = request.getSession().getServletContext().getRealPath(pathname);
        //创建字节输出流向指定文件输出内容
        FileOutputStream write= null;
        //定义b存储获取字节的长度
        int b=0;
        //定义字节数组存储获取到的一组字节
        byte by[]=new byte[1024];
        try {
            write = new FileOutputStream(new File(realPath+"\\temp.txt"));
            //循环将内容复制到临时文件
            while ((b=reader.read(by))>0){
                write.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                reader.close();
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //创建随机访问文件对象对临时文件以可读写模式进行访问
        RandomAccessFile randomAccessFile= null;
        String firstline=null,secondline=null,thirdline=null;
        long filelength=0;
        try {
            randomAccessFile = new RandomAccessFile(new File(realPath+"\\temp.txt"),"rw");
            filelength=randomAccessFile.length();
            //获取第一行内容并移动游标
            firstline = randomAccessFile.readLine();
            //获取第二行内容并移动游标
            secondline = new String(randomAccessFile.readLine().getBytes("iso-8859-1"),"UTF-8");
            //获取第三行内容并移动游标
            thirdline = randomAccessFile.readLine();
            //读取第四行内容并移动游标
            randomAccessFile.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建json对象
        JSONObject formdatajson1=new JSONObject();
        //判断文件大小是否达到最大
        //计算最大长度
        long maxlength=maxsize << 10 << 10;
        if (filelength<maxlength){
            formdatajson1.put("flag",true);
        }else{
            formdatajson1.put("flag",false);
            formdatajson1.put("error","该文件过大，请选择小于"+maxsize+"MB的文件");
            return formdatajson1;
        }
        //将第二行内容分割，用以获取formdata
        String[] split = secondline.split(";");
        //遍历每一组内容
        for (String s : split) {
            //去除空格
            s = s.trim();
            //将每一组再次分割，形成（K，V）形式
            String[] split1 = s.split("=");
            if (split1.length==2){
                //如果成功，存储到json对象中
                formdatajson1.put(split1[0],split1[1].substring(1,split1[1].length()-1));
            }
        }

        //使用UUID生成唯一字串
        String uuid = UUID.randomUUID().toString();
        //使用RandomAccessFile对象的设置长度方法的截取效果去除最后一行尾部信息
        try {
            randomAccessFile.setLength(randomAccessFile.length()-(firstline+"--\r\n\r\n").getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从json对象中获取filename的value值
        String filename = formdatajson1.getString("filename");
        //使用分隔方法会造成错误，弃用
        //String[] split1 = filename.split(".");
        //截取后缀名
        String suffix=filename.substring(filename.lastIndexOf("."),filename.length());
        //判断文件类型是否符合要求
        String[] accepts = accept.split(",");
        boolean isaccept=false;
        for (String s:accepts){
            if (suffix.equals(s)){
                isaccept=true;
                break;
            }
        }
        if ("*".equals(accept)){
            isaccept=true;
        }
        if (isaccept){
            formdatajson1.put("flag",true);
        }else{
            formdatajson1.put("flag",false);
            formdatajson1.put("error","格式错误，支持格式："+accept);
            return formdatajson1;
        }
        //使用唯一字串与后缀名拼接形成新的文件名
        String newfilename=uuid+suffix;
        //创建字节输出流向指定位置输出内容
        FileOutputStream outputStream= null;
        try {
            outputStream = new FileOutputStream(new File(realPath+"\\"+newfilename));
            //循环读写操作
            while ((b=randomAccessFile.read(by))>0){
                outputStream.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                randomAccessFile.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将新的文件名放入json对象
        formdatajson1.put("newfilename",newfilename);
        return formdatajson1;
    }

    /*
    指定了三个参数：
    request>必要，指HttpServlet的请求对象
    pathname>必要，指要存放的文件夹名称，方法会在target文件夹中搜索该文件夹名称
    由于没有指定限制的文件类型，默认为适应所有类型文件，可能会引起恶意传递，谨慎使用！
    由于没有指定最大的文件大小，方法会接受任意大小的文件，传递超大文件时，可能发生崩溃
    r>必要，是否生成唯一字串作为文件名*/
    public static JSONObject uploadone(HttpServletRequest request, String pathname,boolean r){
        //创建request的字节输入流
        ServletInputStream reader = null;
        try {
            reader = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取指定文件的真实地址
        String realPath = request.getSession().getServletContext().getRealPath(pathname);
        //创建字节输出流向指定文件输出内容
        FileOutputStream write= null;
        //定义b存储获取字节的长度
        int b=0;
        //定义字节数组存储获取到的一组字节
        byte by[]=new byte[1024];
        try {
            File file=new File(realPath+"\\temp.txt");
            write = new FileOutputStream(file);
            //循环将内容复制到临时文件
            while ((b=reader.read(by))>0){
                write.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                reader.close();
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //创建随机访问文件对象对临时文件以可读写模式进行访问
        RandomAccessFile randomAccessFile= null;
        String firstline=null,secondline=null,thirdline=null;
        long fileleng=0;
        try {
            randomAccessFile = new RandomAccessFile(new File(realPath+"\\temp.txt"),"rw");
            fileleng=randomAccessFile.length();
            //获取第一行内容并移动游标
            firstline = randomAccessFile.readLine();
            //获取第二行内容并移动游标
            secondline = new String(randomAccessFile.readLine().getBytes("iso-8859-1"),"UTF-8");
            //获取第三行内容并移动游标
            thirdline = randomAccessFile.readLine();
            //读取第四行内容并移动游标
            randomAccessFile.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建json对象
        JSONObject formdatajson1=new JSONObject();
        double filesize= (fileleng >> 10 >> 10);
        formdatajson1.put("filesize",filesize);
        //将第二行内容分割，用以获取formdata
        String[] split = secondline.split(";");
        //遍历每一组内容
        for (String s : split) {
            //去除空格
            s = s.trim();
            //将每一组再次分割，形成（K，V）形式
            String[] split1 = s.split("=");
            if (split1.length==2){
                //如果成功，存储到json对象中
                formdatajson1.put(split1[0],split1[1].substring(1,split1[1].length()-1));
            }
        }

        //使用UUID生成唯一字串
        String uuid = UUID.randomUUID().toString();
        //使用RandomAccessFile对象的设置长度方法的截取效果去除最后一行尾部信息
        try {
            randomAccessFile.setLength(randomAccessFile.length()-(firstline+"--\r\n\r\n").getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从json对象中获取filename的value值
        String filename = formdatajson1.getString("filename");
        //使用分隔方法会造成错误，弃用
        //String[] split1 = filename.split(".");
        //截取后缀名
        String suffix=filename.substring(filename.lastIndexOf("."),filename.length());
        //使用唯一字串与后缀名拼接形成新的文件名
        String newfilename="";
        if (r){
            newfilename=uuid+suffix;
        }else{
            newfilename=filename;
        }
        //创建字节输出流向指定位置输出内容
        FileOutputStream outputStream= null;
        try {
            outputStream = new FileOutputStream(new File(realPath+"\\"+newfilename));
            //循环读写操作
            while ((b=randomAccessFile.read(by))>0){
                outputStream.write(by,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                randomAccessFile.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将新的文件名放入json对象
        formdatajson1.put("newfilename",newfilename);
        return formdatajson1;
    }
}
