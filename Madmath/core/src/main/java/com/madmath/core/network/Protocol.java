package com.madmath.core.network;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 18/12/2021 上午4:49
 */
public enum Protocol {
    Receive(0),
    MapCreate(1), PlayerCreate(2), MonsterCreate(3),
    PlayerAct(4), MonsterAct(5),
    NewConnect(50);

    public final int protocolId;

    Protocol(int protocolId) {
        this.protocolId = protocolId;
    }
}
