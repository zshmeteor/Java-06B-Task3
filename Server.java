package glimmer.zsh.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("服务器已打开 端口：5000 等待连接中...");
            //监听
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接");
            //键盘读入
            InputStreamReader sysin = new InputStreamReader(System.in);
            BufferedReader sysbuf = new BufferedReader(sysin);
            //Socket读入
            InputStreamReader socin = new InputStreamReader(socket.getInputStream());
            BufferedReader socbuf = new BufferedReader(socin);
            //写出
            PrintWriter socout = new PrintWriter(socket.getOutputStream());

            String str = null;
            String str1 = null;
            String readline = socbuf.readLine();
            while (!readline.equals("quit")){//如果为quit,关闭连接并退出程序
                switch (readline){
                    case "pwd"://显示远程工作目录
                        System.out.println(readline);
                        socout.println(str);
                        socout.flush();
                        break;
                    case "cd"://改变远程工作目录
                        str = socbuf.readLine();
                        System.out.println(readline+" "+str);
                        break;
                    case "ls"://显示远程工作目录包含的文件名
                        System.out.println(readline);
                        str1 = str.replaceAll("/",File.separator);
                        File file = new File(str1);
                        String[] list = file.list();
                        for(String s:list){
                            socout.println(s);
                            socout.flush();
                        }
                        socout.println("#");
                        socout.flush();
                        break;
                    case "get"://下载指定文件
                        System.out.println(readline);
                        String s2 = socbuf.readLine();
                        System.out.println(s2);
                        System.out.println("打开数据端口：5001 等待连接中...");
                        ServerSocket ss1 = new ServerSocket(5001);
                        System.out.println("客户端连接成功 等待客户端下载文件...");
                        Socket socket2 = ss1.accept();
                        OutputStream os = socket2.getOutputStream();
                        FileInputStream fis = new FileInputStream(str1+File.separator+s2);
                        byte[] buffer1 = new byte[1024];
                        int len1;
                        while((len1 = fis.read(buffer1))!=-1){
                            os.write(buffer1,0,len1);
                        }
                        System.out.println("文件下载完毕 Socket已关闭");
                        fis.close();
                        os.close();
                        socket2.close();
                        ss1.close();
                        break;
                    case "put"://上传指定文件
                        System.out.println(readline);
                        String s = socbuf.readLine();
                        System.out.println(s);
                        int index = s.lastIndexOf("\\");
                        String s1 = s.substring(index+1,s.length());
                        System.out.println("打开数据端口：5001 等待连接中...");
                        ServerSocket ss = new ServerSocket(5001);
                        System.out.println("客户端连接成功 等待客户端上传文件...");
                        Socket socket1 = ss.accept();
                        InputStream is = socket1.getInputStream();
                        String str2 = str.replaceAll("/", File.separator);
                        FileOutputStream fos = new FileOutputStream(str2+File.separator+s1);
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = is.read(buffer))!= -1){
                            fos.write(buffer,0,len);
                        }
                        fos.close();
                        is.close();
                        socket1.close();
                        ss.close();
                        System.out.println("传输成功 Socket已关闭");
                        break;
                    default:
                        System.out.println(readline);
                }
                readline = socbuf.readLine();
            }
            System.out.println(readline);
            System.out.println("客户端已断开连接 Socket关闭");
            //关闭IO和Socket
            sysbuf.close();
            socbuf.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
        }

    }
}