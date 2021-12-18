package com.madmath.core.network;

import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.io.MyInput;
import com.madmath.core.io.MyOutput;
import com.madmath.core.main.MadMath;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 17/12/2021 下午5:33
 */
public class Client implements Runnable {

    SocketChannel socketChannel;

    ByteBuffer writeBuffer;
    ByteBuffer readBuffer;

    ByteBuffer tempBuffer;

    MyInput input;
    MyOutput output;

    MadMath game;

    int id;


    public Client(MadMath game) {
        this.game = game;
        id = -1;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(null);
        new Thread(client).start();
        Scanner scanner = new Scanner(System.in);
        while (true){
            client.writeAct(scanner.next());
        }
    }

    public void writeAct(String s){
        output.write(id,()->{
            output.writeInt(Protocol.PlayerAct.protocolId);
            //System.out.print(s+"   ");
            output.writeString(s);
            //System.out.println(output.position());
        });
    }

    public void loadMap(){
        System.out.println("Map-"+input.readString());
    }

    public void loadPlayer(){
        System.out.println("Player-"+input.readString());
    }

    public void run() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 4397));
            System.out.println("Connect Successfully!");

            writeBuffer = ByteBuffer.allocate(50<<10);
            readBuffer = ByteBuffer.allocate(50<<10);
            output = new MyOutput(writeBuffer);
            input = new MyInput(readBuffer);

            socketChannel.read(input.getByteBuffer());
            input.flip();


            //TimeUnit.MILLISECONDS.sleep(1000);

            id = input.readInt();
            System.out.print("assigned ID :"+id);
            System.out.print("    InitMap: ");
            loadMap();

            output.write(id,()->{
                output.writeInt(Protocol.PlayerCreate.protocolId);

                output.writeString("AYAYA"+Thread.currentThread().getId());
            });

            while (true) {
                output.flip();
                if(output.getByteBuffer().limit()>0) {
                    socketChannel.write(output.getByteBuffer());
                }
                output.clear();

                input.clear();
                socketChannel.read(input.getByteBuffer());
                input.flip();
                while (input.remaining()>0){
                    int socketId = input.readInt();
                    //System.out.print("From id:"+socketId+"   Message: ");
                    if(socketId==id){
                        System.out.println("Pass");
                        input.readBytes(input.readInt());
                        continue;
                    }
                    input.readInt();
                    //System.out.print("Size-"+input.readInt()+"    ");
                    switch (Protocol.values()[input.readInt()]){
                        case MapCreate:
                            System.out.print("Map Create: ");
                            loadMap();
                            break;
                        case PlayerCreate:
                            System.out.print("Player"+socketId+" Create: ");
                            loadPlayer();
                            break;
                        case PlayerAct:
                            System.out.println("Player"+socketId+" act:" + input.readString());
                            break;
                        case Receive:
                            if(input.readInt()==id){
                                //System.out.println("Accept Receive!");
                            }
                            break;
                        case NewConnect:
                        default:
                            System.out.println("Unkown Message!");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}