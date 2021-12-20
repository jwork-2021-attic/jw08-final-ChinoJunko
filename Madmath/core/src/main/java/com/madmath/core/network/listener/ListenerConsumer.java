package com.madmath.core.network.listener;

import com.esotericsoftware.kryonet.Connection;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午7:00
 */
public interface ListenerConsumer<T> {
    void accept(Connection conncetion, T elem);
}
