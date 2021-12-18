package com.madmath.core.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.network.Protocol;

import java.nio.ByteBuffer;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 18/12/2021 上午8:56
 */
public class MyOutput extends Output {

    ByteBuffer byteBuffer;

    public MyOutput(ByteBuffer byteBuffer){
        super(byteBuffer.array(),50<<10);
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer(){
        return byteBuffer;
    }

    public ByteBuffer clear(){
        byteBuffer.clear();
        position = byteBuffer.position();
        return byteBuffer;
    }

    public void flip(){
        byteBuffer.flip();
        position = byteBuffer.position();
    }

    public ByteBuffer compact(){
        byteBuffer.compact();
        position = byteBuffer.position();
        return byteBuffer;
    }

    public int remaining(){
        return byteBuffer.remaining();
    }


    @Override
    public void setPosition(int position) {
        super.setPosition(position);
        byteBuffer.position(position);
    }

    @Override
    public void writeInt(int value) throws KryoException {
        super.writeInt(value);
        byteBuffer.position(position());
    }
    @Override
    public void writeFloat(float value) throws KryoException {
        super.writeFloat(value);
        byteBuffer.position(position());
    }

    @Override
    public void writeString(String value) throws KryoException {
        super.writeString(value);
        byteBuffer.position(position());
    }

    @Override
    public void writeBytes(byte[] value) throws KryoException {
        super.writeBytes(value);
        byteBuffer.position(position());
    }

    public void write(int id, Runnable runnable){
        writeInt(id);
        int startPosition = position();
        writeInt(0);

        runnable.run();

        int lastPosition = position();
        setPosition(startPosition);
        writeInt(lastPosition-startPosition-4);
        setPosition(lastPosition);
    }
}
