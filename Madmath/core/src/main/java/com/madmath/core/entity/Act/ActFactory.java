package com.madmath.core.entity.Act;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 上午5:54
 */
public class ActFactory {
    static final public ActFactory actFactory = new ActFactory();
    Map<Integer,Act> map = new HashMap<>();
    Map<String,Integer> stringMap = new HashMap<>();
    public ActFactory(){
        map.put(0,new End());
        map.put(1,new Swing());
        map.put(2,new Switch());
        map.forEach((id,act)->{
            stringMap.put(act.getClass().getName(),id);
        });
    }

    public int getActId(Act act){
        return stringMap.get(act.getClass().getName());
    }

    public Act getAct(int id){
        return map.get(id);
    }
}
