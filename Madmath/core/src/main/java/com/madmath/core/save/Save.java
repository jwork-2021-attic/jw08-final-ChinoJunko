package com.madmath.core.save;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.entity.obstacle.Obstacle;
import com.madmath.core.inventory.Item;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.inventory.equipment.OddSword;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.screen.GameScreen;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 14/12/2021 下午5:50
 */
public class Save {
    //public Kryo kryo;
    public MadMath game;
    public Save(MadMath game){
        this.game = game;
    }

    public Object read(Input input, String className){
        if(className.equals("GameMap")){
            return readGameMap(input);
        }else if(className.equals("Player")){
            return readPlayer(input);
        }
        return null;
    }

    public void writeTitle(Output output, GameMap map, Player player){
        output.writeString(map.name);
        output.writeInt(map.mapLevel);
        output.writeFloat(map.difficultyFactor);
        output.writeInt(player.score);
    }

    public String[] readTitle(Input input){
        String map = "";
        map+=input.readString();
        map+=" maplevel: "+input.readInt();
        map+=" dif: "+input.readFloat();
        String player = "";
        player+="score: "+input.readInt();
        return new String[]{map, player};
    }

    public void write(Output output, GameMap map) {
        output.writeString(map.name);
        output.writeInt(map.mapLevel);
        output.writeFloat(map.difficultyFactor);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.tiledMap.getLayers().get(0);
        output.writeInt(layer.getWidth());
        output.writeInt(layer.getHeight());
        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                if(layer.getCell(i,j)!=null){
                    output.writeInt(layer.getCell(i,j).getTile().getId());
                }else{
                    output.writeInt(-666);
                }
            }
        }
        output.writeInt(game.gameScreen.monsterManager.monsters.size);
        for (Monster monster: game.gameScreen.monsterManager.monsters
        ) {
            output.writeString(monster.getClass().getName());
            output.writeFloat(monster.getPosition().x);
            output.writeFloat(monster.getPosition().y);
        }
        int obstacleSize = 0;
        for (Entity entity: map.livingEntity
        ) {
            if (entity instanceof Obstacle) {
                obstacleSize++;
            }
        }
        output.writeInt(obstacleSize);
        for (Entity entity: map.livingEntity
        ) {
            if(entity instanceof Obstacle){
                output.writeString(entity.getClass().getName());
                output.writeFloat(entity.getPosition().x);
                output.writeFloat(entity.getPosition().y);
            }
        }
        output.writeInt(game.gameScreen.map.livingItem.size);
        for (Item item: map.livingItem
        ) {
            if(item instanceof Equipment){
                output.writeString(item.getClass().getName());
                output.writeFloat(item.getX());
                output.writeFloat(item.getY());
            }
        }
    }

    public GameMap readGameMap(Input input) {
        String name = input.readString();
        System.out.println(name);
        int level = input.readInt();
        float factor = input.readFloat();
        GameMap map = new GameMap(game.gameScreen,name,level,factor);
        int width = input.readInt();
        int height = input.readInt();
        TiledMapTileLayer layer = new TiledMapTileLayer(width,height,16,16);
        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                int tileId = input.readInt();
                if(tileId == -666) continue;
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(map.tiledMap.getTileSets().getTile(tileId));
                layer.setCell(i,j,cell);
            }
        }
        map.getTiledMap().getLayers().add(layer);
        int monstersSize = input.readInt();
        for (int i = 0; i < monstersSize; i++) {
            Monster monster = game.gameScreen.generateMonster(input.readString());
            Vector2 vector2 = new Vector2();
            vector2.x = input.readFloat();
            vector2.y = input.readFloat();
            monster.setPosition(vector2);
        }
        int obstacleSize = input.readInt();
        for (int i = 0; i < obstacleSize; i++) {
            String obstacleName = input.readString();
            float x = input.readFloat();
            float y = input.readFloat();
            map.createObstacle(obstacleName,x,y);
        }
        int equipmentSize = input.readInt();
        for (int i = 0; i < equipmentSize; i++) {
            String equipmentName = input.readString();
            float x = input.readFloat();
            float y = input.readFloat();
            game.gameScreen.generateEquipment(equipmentName,x,y);
        }
        return map;
    }

    public void write(Output output, Player player) {
        output.writeInt(player.getId());
        output.writeInt(player.weapon.size);
        for (int i = 0; i < player.weapon.size; i++) {
            output.writeString(player.weapon.get(i).getClass().getName());
        }
        if(player.weapon.size>0)output.writeInt(player.weapon.indexOf(player.activeWeapon,true));
        output.writeInt(player.getMaxHp());
        output.writeInt(player.getHp());
        output.writeInt(player.score);
        output.writeFloat(player.getPosition().x);
        output.writeFloat(player.getPosition().y);
    }

    public Player readPlayer(Input input) {
        Player player = new Player(input.readInt(),Player.initPlayerAnim(game.manager),game.gameScreen);
        int wSize = input.readInt();
        for (int i = 0; i < wSize; i++) {
            player.addWeapon(game.gameScreen.createEquipmentByName(input.readString()));
        }
        if(wSize>0)    player.setWeapon(input.readInt());
        player.setMaxHp(input.readInt());
        player.setHp(input.readInt());
        player.score = input.readInt();
        Vector2 vector2 = new Vector2();
        vector2.x = input.readFloat();
        vector2.y = input.readFloat();
        player.setPosition(vector2);
        return player;
    }
    /*
    void kryoRegister(){
        kryo.setRegistrationRequired(false);
        kryo.setReferences(true);
        kryo.register(GameMap.class, new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object o) {
                GameMap map = (GameMap) o;
                output.writeString(map.name);
                output.writeInt(map.mapLevel);
                output.writeFloat(map.difficultyFactor);
                TiledMapTileLayer layer = (TiledMapTileLayer) map.tiledMap.getLayers().get(0);
                output.writeInt(layer.getWidth());
                output.writeInt(layer.getHeight());
                for (int i = 0; i < layer.getWidth(); i++) {
                    for (int j = 0; j < layer.getHeight(); j++) {
                        if(layer.getCell(i,j)!=null){
                            output.writeInt(layer.getCell(i,j).getTile().getId());
                        }else{
                            output.writeInt(-666);
                        }
                    }
                }
                output.writeInt(game.gameScreen.monsterManager.monsters.size);
                for (Monster monster: game.gameScreen.monsterManager.monsters
                     ) {
                    output.writeString(monster.getClass().getName());
                    output.writeFloat(monster.getPosition().x);
                    output.writeFloat(monster.getPosition().y);
                }
                output.writeInt(game.gameScreen.map.livingEntity.size-game.gameScreen.monsterManager.monsters.size-1);
                for (Entity entity: map.livingEntity
                     ) {
                    if(entity instanceof Obstacle){
                        output.writeString(entity.getClass().getName());
                        output.writeFloat(entity.getPosition().x);
                        output.writeFloat(entity.getPosition().y);
                    }
                }
                output.writeInt(game.gameScreen.map.livingItem.size);
                for (Item item: map.livingItem
                ) {
                    if(item instanceof Equipment){
                        output.writeString(item.getClass().getName());
                        output.writeFloat(item.getPosition().x);
                        output.writeFloat(item.getPosition().y);
                    }
                }
            }

            @Override
            public Object read(Kryo kryo, Input input, Class aClass) {
                System.out.println(2);
                String name = input.readString();
                int level = input.readInt();
                float factor = input.readFloat();
                GameMap map = new GameMap(GameScreen.getCurrencyGameScreen(),name,level,factor);
                GameScreen.getCurrencyGameScreen().map = map;
                int width = input.readInt();
                int height = input.readInt();
                TiledMapTileLayer layer = new TiledMapTileLayer(width,height,16,16);
                for (int i = 0; i < layer.getWidth(); i++) {
                    for (int j = 0; j < layer.getHeight(); j++) {
                        int tileId = input.readInt();
                        if(tileId == -666) continue;
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        cell.setTile(map.tiledMap.getTileSets().getTile(tileId));
                        layer.setCell(i,j,cell);
                    }
                }
                int monstersSize = input.readInt();
                for (int i = 0; i < monstersSize; i++) {
                    Monster monster = game.gameScreen.generateMonster(input.readString());
                    Vector2 vector2 = new Vector2();
                    vector2.x = input.readFloat();
                    vector2.y = input.readFloat();
                    monster.setPosition(vector2);
                }
                int obstacleSize = input.readInt();
                for (int i = 0; i < obstacleSize; i++) {
                    String obstacleName = input.readString();
                    float x = input.readFloat();
                    float y = input.readFloat();
                    map.createObstacle(obstacleName,x,y);
                }
                int equipmentSize = input.readInt();
                for (int i = 0; i < equipmentSize; i++) {
                    String equipmentName = input.readString();
                    float x = input.readFloat();
                    float y = input.readFloat();
                    game.gameScreen.generateEquipment(equipmentName,x,y);
                }
                return map;
            }
        });
        kryo.register(Player.class, new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object o) {
                Player player = (Player) o;
                output.writeInt(player.getId());
                output.writeInt(player.weapon.size);
                for (int i = 0; i < player.weapon.size; i++) {
                    output.writeString(player.weapon.get(i).getClass().getName());
                }
                if(player.weapon.size>0)output.writeInt(player.weapon.indexOf(player.activeWeapon,true));
                output.write(player.getMaxHp());
                output.write(player.getHp());
                output.write(player.score);
                output.writeFloat(player.getPosition().x);
                output.writeFloat(player.getPosition().y);
            }

            @Override
            public Object read(Kryo kryo, Input input, Class aClass) {
                Player player = new Player(input.readInt(),Player.initPlayerAnim(game.manager),game.gameScreen);
                int wSize = input.readInt();
                for (int i = 0; i < wSize; i++) {
                    player.addWeapon(game.gameScreen.createEquipmentByName(input.readString()));
                }
                if(wSize>0)    player.setWeapon(input.readInt());
                player.setMaxHp(input.readInt());
                player.setHp(input.readInt());
                player.score = input.readInt();
                Vector2 vector2 = new Vector2();
                vector2.x = input.readFloat();
                vector2.y = input.readFloat();
                player.setPosition(vector2);
                return player;
            }
        });
        kryo.register(Texture.class, new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object o) {
                output.writeString(game.manager.assetManager.getAssetFileName((Texture)o));
            }

            @Override
            public Object read(Kryo kryo, Input input, Class aClass) {
                return game.manager.assetManager.get(input.readString(),aClass);
            }
        });
        kryo.register(com.badlogic.gdx.backends.lwjgl.audio.Wav.Sound.class, new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object o) {
                output.writeString(game.manager.assetManager.getAssetFileName((Sound)o));
            }

            @Override
            public Object read(Kryo kryo, Input input, Class aClass) {
                return game.manager.assetManager.get(input.readString(),aClass);
            }
        });
        kryo.register(Array.class, new Serializer<Array>() {
            {
                setAcceptsNull(true);
            }

            private Class genericType;

            public void setGenerics (Kryo kryo, Class[] generics) {
                if (generics != null && kryo.isFinal(generics[0])) genericType = generics[0];
                else genericType = null;
            }

            public void write (Kryo kryo, Output output, Array array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) {
                    genericType = null;
                    return;
                }
                if (genericType != null) {
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (Object element : array)
                        kryo.writeObjectOrNull(output, element, serializer);
                } else {
                    for (Object element : array)
                        kryo.writeClassAndObject(output, element);
                }
            }

            public Array read (Kryo kryo, Input input, Class<? extends Array> type) {
                Array array = new Array();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                if (genericType != null) {
                    Class elementClass = genericType;
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readObjectOrNull(input, elementClass, serializer));
                } else {
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readClassAndObject(input));
                }
                return array;
            }
        });
        kryo.register(IntArray.class, new Serializer<IntArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, IntArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeInt(array.get(i), true);
            }

            public IntArray read (Kryo kryo, Input input, Class<? extends IntArray> type) {
                IntArray array = new IntArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readInt(true));
                return array;
            }
        });

        kryo.register(FloatArray.class, new Serializer<FloatArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, FloatArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeFloat(array.get(i));
            }

            public FloatArray read (Kryo kryo, Input input, Class<? extends FloatArray> type) {
                FloatArray array = new FloatArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readFloat());
                return array;
            }
        });

        kryo.register(Color.class, new Serializer<Color>() {
            public Color read (Kryo kryo, Input input, Class<? extends Color> type) {
                Color color = new Color();
                Color.rgba8888ToColor(color, input.readInt());
                return color;
            }

            public void write (Kryo kryo, Output output, Color color) {
                output.writeInt(Color.rgba8888(color));
            }
        });
    }

     */
}
