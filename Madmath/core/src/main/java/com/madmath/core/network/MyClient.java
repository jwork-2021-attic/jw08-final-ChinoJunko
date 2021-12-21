package com.madmath.core.network;


import com.esotericsoftware.kryonet.Client;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.dto.Dto;
import com.madmath.core.network.listener.GameInitializerListener;
import com.madmath.core.serializer.MySerializer;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 17/12/2021 下午5:33
 */
public class MyClient {

    private final Client client;

    private MadMath game;

    public String Ip;
    public int Port=4396;

    public MyClient(MadMath game) {
        this.game = game;
        client = new Client(65536, 32768);
        MySerializer.register(client);
    }

    public Client getClient() {
        return client;
    }

    public void connect() {
        client.addListener(new GameInitializerListener(game, client));
        client.start();
        try {
            tryConnection(500, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tryConnection(long ms, boolean retry) throws InterruptedException {
        if (!retry) {
            return;
        }
        if (ms > 500) {
            Thread.sleep(ms);
        }
        try {
            client.connect(15000, Ip, Port);
        } catch (IOException ex) {
            tryConnection(ms * 2, ms > 3000);
            ex.printStackTrace();
        }
    }

    public void close(){
        client.close();
    }

    public <T extends Dto> void sendTCP(T dto) {
        client.sendTCP(dto);
    }

}
