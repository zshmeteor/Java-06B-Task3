package glimmer.zsh.ftp;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        try {
            //创建Socket连接
            Socket socket = new Socket("192.168.159.129",5000);
            System.out.println("客户端已打开，正在连接服务器中...");
            System.out.println("IP:192.168.159.129 Port:5000");
            System.out.println("服务器连接成功");
            //键盘读入
            InputStreamReader sysin = new InputStreamReader(System.in);
            BufferedReader sysbuf = new BufferedReader(sysin);
            //Socket读入
            InputStreamReader socin = new InputStreamReader(socket.getInputStream());
            BufferedReader socbuf = new BufferedReader(socin);
            //写出
            PrintWriter socout = new PrintWriter(socket.getOutputStream());
            System.out.print(">>>");
            String str = null;
            //输入指令及执行
            String readline = sysbuf.readLine();
            while(!readline.equals("quit")){//如果为quit,关闭连接并退出程序
                socout.println(readline);
                socout.flush();
                switch (readline){
                    case "pwd"://显示远程工作目录
                        str =socbuf.readLine();
                        System.out.println(str);
                        break;
                    case "cd"://改变远程工作目录
                        str = sysbuf.readLine();
                        socout.println(str);
                        socout.flush();
                        break;
                    case "ls"://显示远程工作目录包含的文件名
                        String s= socbuf.readLine();
                        while(!(s.equals("#"))){
                            System.out.println(s);
                            s = socbuf.readLine();
                        }
                        break;
                    case "get"://下载指定文件
                        str = sysbuf.readLine();
                        socout.println(str);
                        socout.flush();
                        Socket socket2 = new Socket("192.168.159.129",5001);
                        System.out.println("数据端口已连接 Port:5001");
                        System.out.println("开始下载文件...");
                        InputStream is = socket2.getInputStream();
                        FileOutputStream fos = new FileOutputStream(str);
                        byte[] buffer2 = new byte[1024];
                        int len2;
                        while((len2 = is.read(buffer2))!= -1){
                            fos.write(buffer2,0,len2);
                        }
                        fos.close();
                        is.close();
                        socket2.close();
                        System.out.println("下载成功 Socket已关闭");
                        break;
                    case "put"://上传指定文件
                        str = sysbuf.readLine();
                        socout.println(str);
                        socout.flush();
                        Socket socket1 = new Socket("192.168.159.129",5001);
                        System.out.println("数据端口已连接 Port:5001");
                        System.out.println("开始上传文件...");
                        OutputStream os = socket1.getOutputStream();
                        FileInputStream fis = new FileInputStream(str);
                        byte[] buffer1 = new byte[1024];
                        int len1;
                        while((len1 = fis.read(buffer1))!=-1){
                            os.write(buffer1,0,len1);
                        }
                        fis.close();
                        os.close();
                        socket1.close();
                        System.out.println("文件上传完毕 Socket已关闭");
                        break;
                    default:
                        System.out.println("没有该指令");
                }
                System.out.print(">>>");
                readline = sysbuf.readLine();
            }
            socout.println(readline);
            socout.flush();
            System.out.println("命令Socket已关闭");
            System.out.println("Bye:)");
            //关闭IO和Socket
            sysbuf.close();
            socbuf.close();
            socket.close();
        } catch (Exception e) {

        }
    }
}
