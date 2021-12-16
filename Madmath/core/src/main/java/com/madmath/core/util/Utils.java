/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:58
*/
package com.madmath.core.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.map.AnimTile;
import com.madmath.core.map.StaticTile;
import com.madmath.core.resource.ResourceManager;

import java.util.*;
import java.util.concurrent.Callable;

public class Utils {

    static public String[] DifficultyName = new String[]{
            "Todwise",
            "Elementary Mathematics",
            "Advanced Mathematics",
            "P=NP?",
    };

    static public String[] AllDefaultMonsterSort = new String[]{
            "BigDemon",
            "BigZombie",
            "Chort",
            "IceZombie",
            "Imp",
            "Ogre",
            "OrcShaman",
            "OrcWarrior",
            "Skelet",
            "Swampy",
    };

    static public String[] AllDefaultEquipmentSort = new String[]{
            "OddSword",
            "EvenSword",
            "SevenSword",
            "PrimeHammer",
    };

    static public String[] AllDefaultObstacleSort = new String[]{
            "RedFountain",
            "BlueFountain",
            "Column",
    };

    static public Vector2[] EnterSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.floor_ladder.ordinal(),50),
            new Vector2(StaticTile.TileSort.floor_stair.ordinal(),50),
    };

    static public Vector2[] FloorSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.floor_1.ordinal(),270),
            new Vector2(StaticTile.TileSort.floor_2.ordinal(),25),
            new Vector2(StaticTile.TileSort.floor_3.ordinal(),50),
            new Vector2(StaticTile.TileSort.floor_4.ordinal(),2),
            new Vector2(StaticTile.TileSort.floor_5.ordinal(),250),
            new Vector2(StaticTile.TileSort.floor_6.ordinal(),3),
            new Vector2(StaticTile.TileSort.floor_7.ordinal(),3),
            new Vector2(StaticTile.TileSort.floor_8.ordinal(),3),
            new Vector2(StaticTile.TileSort.hole.ordinal(),20),
    };

    static public Vector2[] WallSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.wall_left.ordinal(),2),
            new Vector2(StaticTile.TileSort.wall_mid.ordinal(),110),
            new Vector2(StaticTile.TileSort.wall_right.ordinal(),2),
            new Vector2(StaticTile.TileSort.wall_hole_1.ordinal(),4),
            new Vector2(StaticTile.TileSort.wall_hole_2.ordinal(),3),
    };
    static public Vector2[] WallWithBannerSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.wall_left.ordinal(),6),
            new Vector2(StaticTile.TileSort.wall_mid.ordinal(),238),
            new Vector2(StaticTile.TileSort.wall_right.ordinal(),6),
            new Vector2(StaticTile.TileSort.wall_hole_1.ordinal(),10),
            new Vector2(StaticTile.TileSort.wall_hole_2.ordinal(),7),
            new Vector2(StaticTile.TileSort.wall_banner_blue.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_green.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_red.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_yellow.ordinal(),12),
    };

    static public Map<String,Integer> idMap = new HashMap<>(1000);

    static private String[] aG = new String[]{
            StaticTile.TileSort.floor_1.name(),
            StaticTile.TileSort.floor_2.name(),
            StaticTile.TileSort.floor_3.name(),
            StaticTile.TileSort.floor_4.name(),
            StaticTile.TileSort.floor_5.name(),
            StaticTile.TileSort.floor_6.name(),
            StaticTile.TileSort.floor_7.name(),
            StaticTile.TileSort.floor_8.name(),
            StaticTile.TileSort.floor_ladder.name(),
            StaticTile.TileSort.floor_stair.name(),
            AnimTile.TileSort.floor_spikes_anim.name(),
    };

    static private String[] eG = new String[]{
            StaticTile.TileSort.floor_ladder.name(),
            StaticTile.TileSort.floor_stair.name(),
    };

    static public LinkedList<String> ModLoadMonsterPath = new LinkedList<>();

    static public LinkedList<String> ModLoadMonsterTexture = new LinkedList<>();

    static public Set<Integer> accessibleG = new HashSet<>(100);

    static public Set<Integer> entryG = new HashSet<>(10);

    static public void initUtils(ResourceManager manager){
        int id = 0;
        for (; id < StaticTile.TileSort.values().length; id++) {
            idMap.put(StaticTile.TileSort.values()[id].name(),id);
        }
        for (int i = 0; i < AnimTile.TileSort.values().length; i++) {
            id += ((TextureRegion[][]) Objects.requireNonNull(manager.getAssetsByName(AnimTile.TileSort.floor_spikes_anim.name()+"16x16")))[0].length;
            idMap.put(AnimTile.TileSort.values()[i].name(),id);
            id++;
        }
        for (int i = 0; i < aG.length; i++) {
            accessibleG.add(idMap.get(aG[i]));
        }
        for (int i = 0; i < eG.length; i++) {
            entryG.add(idMap.get(eG[i]));
        }
    }

    //sample: ((1,30),(2,20),(3,30),(4,100)) means 0|..1..|30|..2..|50|..3..|80|..4..|180
    static public  Callable<Float> ProbabilityGenerator(final Vector2...vet) {
        return () -> {
            float total = 0;
            for (Vector2 v: vet) {
                total+=v.y;
            }
            total *= new Random().nextFloat();
            float boundary = 0;
            for (Vector2 v: vet) {
                boundary+=v.y;
                if(total<boundary)  return v.x;
            }
            return vet[vet.length-1].y;
        };
    }

}
