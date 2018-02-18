package com.xianjielee.qrcode;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QRCodeReceiver {

    private static Frame frame;
    private static JTextField serverIp;
    private static JButton launche;
    private static JTextField serverPort;
    private static JButton copy;
    private static JLabel scanResult;

    public static void main(String[] args) {
        frame = new Frame("条形码扫描结果");
        frame.setSize(450, 400);
        frame.setLocation(300, 200);
        frame.setLayout(new FlowLayout(3, 20, 20));
        JLabel serverIpLabel = new JLabel("服务端Ip:");
        serverIp = new JTextField("192.168.1.6");
        frame.add(serverIpLabel);
        frame.add(serverIp);

        JLabel serverPortLabel = new JLabel("服务端端口号");
        serverPort = new JTextField("1991");
        frame.add(serverPortLabel);
        frame.add(serverPort);

        launche = new JButton("启动");
        frame.add(launche);

        frame.add(new JLabel("扫描结果:"));
        scanResult = new JLabel("1234567890");
        frame.add(scanResult);

        copy = new JButton("复制");
        frame.add(copy);
        initEvent();
        frame.setVisible(true);
    }

    private static void initEvent() {
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("复制条形码:" + scanResult.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(scanResult.getText()), null);
            }
        });

        launche.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showInternalMessageDialog(launche, "客户端已经启动了...", "", JOptionPane.PLAIN_MESSAGE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = null;
                            while (socket == null) {
                                try {
                                    socket = new Socket(serverIp.getText(), Integer.valueOf(serverPort.getText()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("连接失败... 3秒后尝试重新连接...");
                                    try {
                                        Thread.sleep(3000);
                                    } catch (Exception ignore) {
                                    }
                                }
                            }
                            System.out.println("连接服务成功...");
                            InputStream inputStream = socket.getInputStream();
                            byte[] bytes = new byte[20];
                            while (inputStream.read(bytes) != -1) {
                                String qrcode = new String(bytes).trim();
                                System.out.println("qrcode = " + qrcode);
                                scanResult.setText(qrcode);
                                bytes = new byte[20];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

//    public static void main(String[] args) {
//        try {
////            channel.configureBlocking(false);
//            SocketChannel channel = null;
//            InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.1.3", 1991);
//            boolean connectSuccess = false;
//            while (!connectSuccess) {
//                try {
//                    channel = SocketChannel.open();
//                    channel.configureBlocking(false);
//                    channel.connect(inetSocketAddress);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("连接失败... 3秒后尝试重新连接...");
//                    try {
//                        Thread.sleep(3000);
//                    } catch (Exception ignore) {
//                    }
//                    continue;
//                }
//                connectSuccess = true;
//            }
//
//            Selector selector = Selector.open();
//            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT, ByteBuffer.allocate(1024));
//            while (true) {
//                selector.select();
//                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
//                while (keyIterator.hasNext()) {
//                    SelectionKey selectionKey = keyIterator.next();
//                    if (selectionKey.isReadable()) {
//                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
//                        String s = new String(buffer.array());
//                        System.out.println("qrcode = " + s);
//                        buffer.clear();
//                    } else {
//                        if (selectionKey.isValid() && selectionKey.isConnectable()) {
//                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//                            if (socketChannel.finishConnect()) {
//                                System.out.println("连接成功...");
//                            } else {
//                                System.out.println("连接未成功...");
//                            }
//                        }
//                    }
//                    keyIterator.remove();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public static final void main(String[] args) {
////        try {
////            // adb 指令
////            Runtime.getRuntime().exec("adb forward tcp:12580 tcp:18888"); // 端口转换
////            Thread.sleep(3000);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        Socket socket = null;
//        try {
//            while (socket == null) {
//                try {
//                    InetAddress serveraddr = null;
//                    serveraddr = InetAddress.getByName("192.168.1.3");
//                    System.out.println("TCP 111111" + "C: Connecting...");
//                    socket = new Socket(serveraddr, 1991);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    try {
//                        Thread.sleep(3000);
//                    } catch (Exception ignored) {
//                    }
//                    System.out.println("连接失败... 3 秒后尝试重新连接...");
//                }
//            }
//            System.out.println("TCP 221122" + "C: Receive");
//            BufferedOutputStream out = new BufferedOutputStream(
//                    socket.getOutputStream());
//            BufferedInputStream in = new BufferedInputStream(
//                    socket.getInputStream());
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    System.in));
//            boolean flag = true;
//            while (flag) {
//                System.out.print("请输入数字0进行文件传输,退出输入-1。");
//                String strWord = br.readLine();// 从控制台输入
//                if (strWord.equals("0")) {
//                    /* 准备接收文件数据 */
//                    out.write("service receive OK".getBytes());
//                    out.flush();
//
//                    Thread.sleep(300);//等待服务端回复
//                    /* 接收文件数据，4字节文件长度，4字节文件格式，其后是文件数据 */
//                    byte[] filelength = new byte[4];
//                    byte[] fileformat = new byte[4];
//                    byte[] filebytes = null;
//
//					/* 从socket流中读取完整文件数据 */
//                    filebytes = receiveFileFromSocket(in, out, filelength,
//                            fileformat);
//                    System.out.println("文件大小：" + (float) filebytes.length / 1024.0 + "KB");
//                    try {
//                        /* 生成文件 */
//                        File file = FileHelper.newFile("Receive.png");
//                        FileHelper.writeFile(file, filebytes, 0,
//                                filebytes.length);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (strWord.equalsIgnoreCase("EXIT")) {
//                    out.write("EXIT".getBytes());
//                    out.flush();
//                    System.out.println("EXIT!");
//                    String strFormsocket = readFromSocket(in);
//                    System.out.println("the data sent by server is:/r/n"
//                            + strFormsocket);
//                    flag = false;
//                    System.out
//                            .println("=============================================");
//                }
//            }
//
//        } catch (UnknownHostException e1) {
//            System.out.println("TCP 331133" + "ERROR:" + e1.toString());
//        } catch (Exception e2) {
//            System.out.println("TCP 441144" + "ERROR:" + e2.toString());
//        } finally {
//            try {
//                if (socket != null) {
//                    socket.close();
//                    System.out.println("socket.close()");
//                }
//            } catch (IOException e) {
//                System.out.println("TCP 551155" + "ERROR:" + e.toString());
//            }
//        }
//    }

    /**
     * 功能：从socket流中读取完整文件数据
     * <p>
     * InputStream in：socket输入流
     * <p>
     * byte[] filelength: 流的前4个字节存储要转送的文件的字节数
     * <p>
     * byte[] fileformat：流的前5-8字节存储要转送的文件的格式（如.apk）
     */
    public static byte[] receiveFileFromSocket(InputStream in,
                                               OutputStream out, byte[] filelength, byte[] fileformat) {
        byte[] filebytes = null;// 文件数据
        try {
            in.read(filelength);// 读文件长度
            int filelen = MyUtil.bytesToInt(filelength);// 文件长度从4字节byte[]转成Int
            String strtmp = "read file length ok:" + filelen;
            out.write(strtmp.getBytes("utf-8"));
            out.flush();

            filebytes = new byte[filelen];
            int pos = 0;
            int rcvLen = 0;
            while ((rcvLen = in.read(filebytes, pos, filelen - pos)) > 0) {
                pos += rcvLen;
            }
            out.write("read file ok".getBytes("utf-8"));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filebytes;
    }

    /* 从InputStream流中读数据 */
    public static String readFromSocket(InputStream in) {
        int MAX_BUFFER_BYTES = 4000;
        String msg = "";
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
            msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");

            tempbuffer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
}
