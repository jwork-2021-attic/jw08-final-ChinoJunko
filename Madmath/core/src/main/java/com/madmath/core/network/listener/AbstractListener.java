package com.madmath.core.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.madmath.core.network.dto.Dto;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午7:01
 */
public abstract class AbstractListener<T extends Dto> extends Listener implements ListenerConsumer<T> {

    private Class<T> clazz;

    public AbstractListener(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (this.clazz.isAssignableFrom(object.getClass())) {
            accept(connection, (T) object);
        }
    }

}
