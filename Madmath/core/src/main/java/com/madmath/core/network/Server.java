package com.madmath.core.network;

import com.madmath.core.io.MyInput;
import com.madmath.core.io.MyOutput;
import com.madmath.core.main.MadMath;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 17/12/2021 下午5:33
 */
public class Server implements Runnable{

    public static Semaphore semaphore = new Semaphore(1);

    ServerSocketChannel[] ssc;
    Selector selector;
    ByteBuffer readBuffer;
    ByteBuffer writeBuffer;
    ByteBuffer tempBuffer;

    MyInput input;
    MyOutput output;
    MyOutput tempOutput;
    MyInput tempInput;

    MadMath game;

    int channelId;


    public Server(MadMath game) {
        this.game = game;
        try {
            channelId = 1;

            selector = Selector.open();

            ssc = new ServerSocketChannel[10];
            for (int i = 0; i < 10; i++) {
                ssc[i]=ServerSocketChannel.open();
                ssc[i].socket().bind(new InetSocketAddress("127.0.0.1", 4396+i));
                ssc[i].configureBlocking(false);
                ssc[i].register(selector, SelectionKey.OP_ACCEPT);
            }



            readBuffer = ByteBuffer.allocate(50<<10);
            writeBuffer = ByteBuffer.allocate(50<<10);
            tempBuffer = ByteBuffer.allocate(50<<10);

            output = new MyOutput(writeBuffer);
            input = new MyInput(readBuffer);
            tempOutput = new MyOutput(tempBuffer);
            tempInput = new MyInput(tempBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(null);
        new Thread(server).start();
        Scanner scanner = new Scanner(System.in);
        while (true){
            String s = scanner.next();
            semaphore.acquire();
            server.playerAct(s);
            semaphore.release();
        }
    }

    public void writeMap(){
        tempOutput.write(0,()->{
            tempOutput.writeInt(Protocol.MapCreate.protocolId);

            tempOutput.writeString("New Map");
        });
    }

    public void writePlayer(){
        tempOutput.write(0,()->{
            tempOutput.writeInt(Protocol.PlayerCreate.protocolId);

            tempOutput.writeString("New Player");
        });
    }

    public void InitClient(SocketChannel channel) throws IOException {
        int position = output.getByteBuffer().limit();
        output.clear();
        output.setPosition(position);
        output.writeInt(channelId);
        output.writeString("New Map");
        output.getByteBuffer().limit(output.position());
        output.setPosition(position);
        channel.write(output.getByteBuffer());
        output.clear();
        output.getByteBuffer().limit(position);
    }

    public void playerAct(String s){
        tempOutput.write(0,()->{
            tempOutput.writeInt(Protocol.PlayerAct.protocolId);

            tempOutput.writeString("Player0 act: " + s);
        });
    }

    public void run() {
        while (true){
            try {
                output.clear();
                semaphore.acquire();
                tempInput.getByteBuffer().flip();
                tempInput.update();
                tempInput.inToOut(output,output.position(),tempInput.remaining());
                output.flip();
                tempInput.clear();
                tempOutput.clear();
                semaphore.release();
                TimeUnit.MILLISECONDS.sleep(17);

                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    try {

                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            socketChannel.configureBlocking(false);
                            System.out.println("Client"+channelId+" register");
                            InitClient(socketChannel);
                            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,new Integer(channelId++));
                        }
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            input.clear();
                            try {
                                socketChannel.read(input.getByteBuffer());
                                input.flip();
                                while(input.remaining()>0){
                                    int lastP = input.position();
                                    int pId = input.readInt();
                                    System.out.print("From id:"+pId+"   Message: ");
                                    System.out.println("Size-"+input.readInt());
                                    switch (Protocol.values()[input.readInt()]){
                                        case PlayerAct:
                                            System.out.println("Player"+pId+" act:" + input.readString());
                                            break;
                                        case PlayerCreate:
                                            System.out.println("Player create: "+input.readString());
                                            break;
                                        case NewConnect:
                                        default:
                                            System.out.println("Unkown Message!");
                                            break;
                                    }
                                    int inputSize = input.position()-lastP;
                                    input.setPosition(lastP);
                                    semaphore.acquire();
                                    input.inToOut(tempOutput,tempOutput.position(),inputSize);
                                    input.setPosition(lastP+inputSize);
                                    semaphore.release();
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                                System.out.print(key.channel());
                                key.cancel();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (key.isWritable()) {
                            output.getByteBuffer().rewind();
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            socketChannel.write(output.getByteBuffer());
                            //System.out.println(output.getByteBuffer().position()+"   "+output.getByteBuffer().limit());
                            semaphore.acquire();
                            tempOutput.write(0,()->{
                                tempOutput.writeInt(Protocol.Receive.protocolId);
                                tempOutput.writeInt((Integer) key.attachment());
                            });
                            semaphore.release();
                        }
                    } catch (Exception e){
                        System.out.print(key.channel());
                        e.printStackTrace();
                        key.cancel();
                        it.remove();
                    }

                    //System.out.println("FUcfojeiofji----"+key.attachment());
                }
                keys.clear();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
