package com.madmath.core.entity.Act;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.inventory.equipment.Equipment;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 21/12/2021 上午3:13
 */
public class Pick implements Act{
    @Override
    public void write(Output output, Player player) {
        output.writeInt(player.activeWeapon.getId());
    }

    @Override
    public void read(Input input, Player player) {
        int eid = input.readInt();
        Equipment equipment = (Equipment) player.gameScreen.map.selectItem(eid);
        if(equipment!=null){
            if(equipment.owner==null){
                player.addWeapon(equipment);
            }else {
                if(equipment.owner.getId()>player.getId()){
                    equipment.owner.throwWeapon(equipment);
                    player.addWeapon(equipment);
                }
            }
        }
    }
}
