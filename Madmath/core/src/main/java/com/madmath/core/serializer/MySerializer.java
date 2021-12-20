package com.madmath.core.serializer;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.entity.obstacle.Obstacle;
import com.madmath.core.inventory.Item;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.network.dto.*;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 14/12/2021 下午5:50
 */
public class MySerializer {

    static public MadMath game;

    static public Kryo defaultKryo = new Kryo();

    static public void register(EndPoint endPoint){
        register(endPoint.getKryo());
    }

    static public void register(Kryo kryo){
        kryo.register(PlayerConnectDto.class);
        kryo.register(PlayerCreateDto.class);
        kryo.register(GameInitializationDto.class, new Serializer<GameInitializationDto>() {
            @Override
            public void write(Kryo kryo, Output output, GameInitializationDto gameInitializationDto) {
                kryo.writeObject(output,gameInitializationDto.player);
                output.writeInt(gameInitializationDto.bufferSize);
                output.writeBytes(gameInitializationDto.buffer,0,gameInitializationDto.bufferSize);
                kryo.writeObject(output,gameInitializationDto.teammates);
            }

            @Override
            public GameInitializationDto read(Kryo kryo, Input input, Class aClass) {
                GameInitializationDto gameInitializationDto = new GameInitializationDto();
                gameInitializationDto.player = kryo.readObject(input,Player.class);
                gameInitializationDto.bufferSize = input.readInt();
                gameInitializationDto.buffer = input.readBytes(gameInitializationDto.bufferSize);
                gameInitializationDto.teammates = kryo.readObject(input,Array.class);
                return gameInitializationDto;
            }
        });
        kryo.register(byte[].class);
        kryo.register(Array.class, new com.esotericsoftware.kryo.Serializer<Array>() {
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
                    com.esotericsoftware.kryo.Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (Object element : array)
                        kryo.writeObjectOrNull(output, element, serializer);
                } else {
                    for (Object element : array)
                        kryo.writeClassAndObject(output, element);
                }
            }

            public Array read (Kryo kryo, Input input, Class<Array> type) {
                Array array = new Array();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                if (genericType != null) {
                    Class elementClass = genericType;
                    com.esotericsoftware.kryo.Serializer serializer = kryo.getSerializer(genericType);
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
        kryo.register(PlayerActDto.class, new com.esotericsoftware.kryo.Serializer<PlayerActDto>() {
            @Override
            public void write(Kryo kryo, Output output, PlayerActDto playerActDto) {
                output.writeInt(playerActDto.id);
                output.writeInt(playerActDto.bufferSize);
                output.writeBytes(playerActDto.buffer,0,playerActDto.bufferSize);
            }

            @Override
            public PlayerActDto read(Kryo kryo, Input input, Class aClass) {
                PlayerActDto playerActDto = new PlayerActDto();
                playerActDto.id = input.readInt();
                playerActDto.bufferSize = input.readInt();
                playerActDto.buffer = input.readBytes(playerActDto.bufferSize);
                return playerActDto;
            }
        });
        kryo.register(MapCreateDto.class, new com.esotericsoftware.kryo.Serializer<MapCreateDto>() {
            @Override
            public void write(Kryo kryo, Output output, MapCreateDto mapCreateDto) {
                output.writeInt(mapCreateDto.id);
                output.writeInt(mapCreateDto.bufferSize);
                output.writeBytes(mapCreateDto.buffer,0,mapCreateDto.bufferSize);
            }

            @Override
            public MapCreateDto read(Kryo kryo, Input input, Class aClass) {
                MapCreateDto mapCreateDto = new MapCreateDto();
                mapCreateDto.id = input.readInt();
                mapCreateDto.bufferSize = input.readInt();
                mapCreateDto.buffer = input.readBytes(mapCreateDto.bufferSize);
                return mapCreateDto;
            }
        });
        kryo.register(GameMap.class, new com.esotericsoftware.kryo.Serializer<GameMap>() {
            @Override
            public void write(Kryo kryo, Output output, GameMap map) {
                map.writeMap(output);
            }

            @Override
            public GameMap read(Kryo kryo, Input input, Class aClass) {
                return GameMap.readMap(input,game);
            }
        });

        kryo.register(Player.class, new com.esotericsoftware.kryo.Serializer<Player>() {
            @Override
            public void write(Kryo kryo, Output output, Player player) {
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

            @Override
            public Player read(Kryo kryo, Input input, Class aClass) {
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

        kryo.register(GameTitle.class, new com.esotericsoftware.kryo.Serializer<GameTitle>() {
            @Override
            public void write(Kryo kryo, Output output, GameTitle gameTitle) {
                output.writeString(gameTitle.name);
                output.writeInt(gameTitle.mapLevel);
                output.writeFloat(gameTitle.factor);
                output.writeInt(gameTitle.score);
            }

            @Override
            public GameTitle read(Kryo kryo, Input input, Class aClass) {
                String name = input.readString();
                int mapLevel = input.readInt();
                float factor = input.readFloat();
                int score = input.readInt();
                return new GameTitle(name,mapLevel,factor,score);
            }
        });
    }
}
