package com.madmath.core.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.nio.ByteBuffer;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 18/12/2021 上午8:56
 */
public class MyInput extends Input {

    ByteBuffer byteBuffer;

    public MyInput(ByteBuffer byteBuffer){
        super(byteBuffer.array());
        this.byteBuffer = byteBuffer;
    }

    public void update(){
        limit = byteBuffer.limit();
    }

    public ByteBuffer getByteBuffer(){
        return byteBuffer;
    }

    public ByteBuffer clear(){
        byteBuffer.clear();
        position = byteBuffer.position();
        limit = byteBuffer.limit();
        return byteBuffer;
    }

    public void flip(){
        byteBuffer.flip();
        position = byteBuffer.position();
        limit = byteBuffer.limit();
    }

    public ByteBuffer compact(){
        byteBuffer.compact();
        position = byteBuffer.position();
        limit = byteBuffer.limit();
        return byteBuffer;
    }

    public int remaining(){
        return byteBuffer.remaining();
    }

    public void limit(int tolimit){
        limit = tolimit;
        byteBuffer.limit(tolimit);
    }

    @Override
    public void setPosition(int position) {
        super.setPosition(position);
        byteBuffer.position(position);
        limit = byteBuffer.limit();
    }

    @Override
    public int readInt() throws KryoException {
        try {
            return super.readInt();
        }finally {
            byteBuffer.position(position());
        }
    }
    @Override
    public boolean readBoolean() throws KryoException {
        try {
            return super.readBoolean();
        }finally {
            byteBuffer.position(position());
        }
    }
    @Override
    public float readFloat() throws KryoException {
        try {
            return super.readFloat();
        }finally {
            byteBuffer.position(position());
        }
    }
    @Override
    public String readString() throws KryoException {
        try {
            /*
            System.out.print("string: po:"+position);
            System.out.print(" lim: "+limit);
            System.out.print(" blim: "+byteBuffer.limit());
            System.out.print(" bpo: "+byteBuffer.position());

             */
            return super.readString();
        }finally {
            byteBuffer.position(position());
        }
    }

    @Override
    public byte[] readBytes(int length) throws KryoException {
        try {
            return super.readBytes(length);
        }finally {
            byteBuffer.position(position());
        }
    }

    public void inToOut(Output output, int pos, int len){
        readBytes(output.getBuffer(),pos,len);
        output.setPosition(output.position()+len);
    }
}
