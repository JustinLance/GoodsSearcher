package com.xianjielee.goodssearcher;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用于手机扫描条形码后发送条形码文本到 socket 客户端（如 PC）
 */
public class QRCodeSender extends Service {

    public static final String TAG = QRCodeSender.class.getSimpleName();
    public static final String ACTIION_SEND_QRCODE = "send_qrcode"; // 发送条形码扫描结果
    public static final String ACTIION_SEND_SOCKET_STATUS = "send_socket_status"; // 发送 socket 连接状态
    public static final String KEY_QRCODE = "qrcode";

    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    ServerSocket mServerSocket = null;

    private List<SocketChannel> mChannels = new ArrayList<>();
    private BroadcastReceiver receiver;
    private ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "QRCodeSender--->onCreate()");
        executorService = Executors.newSingleThreadExecutor();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: act = " + intent.getAction());
                if (ACTIION_SEND_QRCODE.equals(intent.getAction())) {
                    for (final SocketChannel channel : mChannels) {
                        if (channel != null && channel.isConnected()) {
                            final byte[] qrcodes = intent.getByteArrayExtra(KEY_QRCODE);
                            if (qrcodes != null && qrcodes.length > 0) {
                                try {
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO: 2018/2/17  增加4位byte用于支持区分发送数据类型
//                                            ByteBuffer allocate = ByteBuffer.allocate(qrcodes.length + 4);
//                                            allocate.putInt(0);
//                                            allocate.put(qrcodes);
                                            ByteBuffer buffer = ByteBuffer.wrap(qrcodes);
                                            try {
                                                channel.write(buffer);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    Log.e(TAG, "onReceive: 写入数据到Client");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (ACTIION_SEND_SOCKET_STATUS.equals(intent.getAction())) {

                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTIION_SEND_QRCODE);
        filter.addAction(ACTIION_SEND_SOCKET_STATUS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        new Thread() {
            public void run() {
                doListen();
            }
        }.start();
    }

    private void doListen() { //端口监听
//        try {
//            mServerSocket = new ServerSocket(10086);
//            while (mainThreadFlag) {
//                Socket socket = mServerSocket.accept();
//                new Thread(new ThreadReadWriterIOSocket(this, socket)).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.socket().bind(new InetSocketAddress(1991));
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (mainThreadFlag) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel channel = ((ServerSocketChannel) selectionKey.channel()).accept();
                        channel.configureBlocking(false);
                        channel.register(selectionKey.selector(), SelectionKey.OP_WRITE, ByteBuffer.allocate(1024));
                        Log.e(TAG, "doListen: 客户端连接成功..." + channel.socket().getInetAddress());
                        mChannels.add(channel);
                    }
                    keyIterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        // 关闭线程
        mainThreadFlag = false;
        ioThreadFlag = false;
        // 关闭服务器
        try {
            Log.e(TAG, Thread.currentThread().getName() + "---->"
                    + "mServerSocket.close()");
            if (mServerSocket != null)
                mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, Thread.currentThread().getName() + "---->"
                + "**************** onDestroy****************");
    }
}
