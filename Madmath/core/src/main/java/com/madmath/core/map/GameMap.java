/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.map;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.entity.obstacle.Obstacle;
import com.madmath.core.inventory.Item;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.util.Utils;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class GameMap {
    GameScreen gameScreen;
    ResourceManager manager;
    public TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;

    public String name;
    public int mapLevel;
    public float difficultyFactor;

    public Vector2 startPosition;
    public Vector2 playAreaSize;
    private Vector2 playerSpawnPoint;

    public Array<Entity> livingEntity;
    public Array<Item> livingItem;

    Image[] background;

    //Array

    public GameMap(GameScreen gameScreen,String name , int mapLevel, float difficultyFactor){
        this.gameScreen = gameScreen;
        gameScreen.map = this;
        livingEntity = new Array<>();
        livingItem = new Array<>();
        livingEntity.add(gameScreen.player);
        this.name = name;
        this.mapLevel = mapLevel;
        this.difficultyFactor = difficultyFactor;
        manager = gameScreen.getGame().manager;
        tiledMap = new TiledMap();
        tiledMap.getTileSets().addTileSet(getNewTileSet());
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        startPosition = new Vector2(0,8*16);
        playAreaSize = new Vector2(120*16,14*16);
        playerSpawnPoint = new Vector2(1+16,15*16+7);
        int backgroundNum = 4;
        background = new Image[backgroundNum];
        for (int i = 0; i < backgroundNum; i++) {
            background[i] = new Image(manager.gamebackground700x128);
            float moveTime = 200f;
            float moveDistance = 690f;
            background[i].addAction(Actions.sequence(Actions.moveTo(moveDistance *i,0),Actions.moveTo(-moveDistance,0,(i+1)* moveTime / backgroundNum),Actions.forever(Actions.sequence(Actions.moveTo((backgroundNum -1)* moveDistance,0),Actions.moveTo(-moveDistance,0, moveTime)))));
            getGameScreen().getStage().addActor(background[i]);
        }
    }

    public TiledMapTileSet getNewTileSet(){
        TiledMapTileSet tileSet = new TiledMapTileSet();
        for (int i = 0; i < StaticTile.TileSort.values().length; i++) {
            StaticTile staticTile = new StaticTile(manager.tiles16x16[i]);
            staticTile.setId(i);
            tileSet.putTile(i,staticTile);
        }
        int stickFrameCount = 18;
        StaticTile[] spikes = new StaticTile[stickFrameCount*2+4];
        for (int i = 0; i < manager.floor_spikes_anim16x16[0].length; i++) {
            StaticTile staticTile = new StaticTile(manager.floor_spikes_anim16x16[0][i]);
            staticTile.setId(i+StaticTile.TileSort.values().length);
            tileSet.putTile(staticTile.getId(),staticTile);
            if(i==0){
                for (int j = 0; j < stickFrameCount/2; j++) {
                    spikes[j] = staticTile;//7
                    spikes[stickFrameCount*2+3-j] = staticTile;//32
                }
            } else if(i==manager.floor_spikes_anim16x16[0].length-1){
                for (int j = 0; j < stickFrameCount/2; j++) {
                    spikes[j+stickFrameCount/2+4] = staticTile;
                    spikes[stickFrameCount*2-stickFrameCount/2-j]=staticTile;
                }
            }
            spikes[stickFrameCount/2+i]=staticTile;//
            spikes[stickFrameCount*2+4-stickFrameCount/2-i]=staticTile;
        }
        Array<StaticTiledMapTile> spikeFrameArray = new Array<>(spikes);
        TrapTile trapTile = new TrapTile(0.1f,spikeFrameArray);
        trapTile.setId(Utils.idMap.get("floor_spikes_anim"));
        tileSet.putTile(trapTile.getId(),trapTile);
        return tileSet;
    }

    public void initTileMap(){
        gameScreen.player.setPosition(playerSpawnPoint);
        initButtomLayer();
        initObstacle();
        initEntities();
        initItem();
    }

    public void initItem(){
        getGameScreen().createEquipment();
    }

    public void initButtomLayer(){
        TiledMapTileLayer tiledMapTileLayer = new TiledMapTileLayer(120,25,16,16);
        tiledMapTileLayer.setName("Buttom");
        Callable<Float> floorCallable = Utils.ProbabilityGenerator(Utils.FloorSortPro);
        Callable<Float> wallCallable = Utils.ProbabilityGenerator(Utils.WallSortPro);
        Callable<Float> bannerCallable = Utils.ProbabilityGenerator(Utils.WallWithBannerSortPro);
        for (int i = 0; i < tiledMapTileLayer.getWidth(); i++) {
            for (int j = 8; j < tiledMapTileLayer.getHeight()-3; j++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                try{
                    cell.setTile(tiledMap.getTileSets().getTile(floorCallable.call().intValue()));
                }catch (Exception e){
                    System.out.println("Some Erorr caused by Tile Loading");
                    cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_1.ordinal()));
                }
                tiledMapTileLayer.setCell(i,j,cell);
            }
            for (int j = tiledMapTileLayer.getHeight()-3; j < tiledMapTileLayer.getHeight(); j++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                try{
                    cell.setTile(tiledMap.getTileSets().getTile(j==tiledMapTileLayer.getHeight()-2?bannerCallable.call().intValue():wallCallable.call().intValue()));
                }catch (Exception e){
                    System.out.println("Some Erorr caused by Tile Loading");
                    cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.wall_mid.ordinal()));
                }
                tiledMapTileLayer.setCell(i,j,cell);
            }
        }
        int spikes_num = 80;
        Random random = new Random();
        for (int i = 0; i < spikes_num; i++) {
            TiledMapTileLayer.Cell cell=new TiledMapTileLayer.Cell();
            cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.values().length+4+AnimTile.TileSort.floor_spikes_anim.ordinal()));
            int spikeX = random.nextInt(119)+1, spikeY = random.nextInt(14) + 8;
            tiledMapTileLayer.setCell(spikeX,spikeY,cell);
        }
        Callable<Float> enterCallable = Utils.ProbabilityGenerator(Utils.EnterSortPro);
        try{
            TiledMapTileLayer.Cell cell=new TiledMapTileLayer.Cell();
            if(enterCallable.call()==StaticTile.TileSort.floor_ladder.ordinal()){
                cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_ladder.ordinal()));
                tiledMapTileLayer.setCell(random.nextInt(20)+100,random.nextInt(12)+9,cell);
            }else {
                cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_stair.ordinal()));
                tiledMapTileLayer.setCell(random.nextInt(5)+115,random.nextInt(12)+9,cell);
            }
        }catch (Exception e){
            System.out.println("Some Erorr caused by Tile Loading");
        }
        for (int i = (int)playerSpawnPoint.x/16; i <= (int) playerSpawnPoint.x/16+1; i++) {
            for (int j = (int) playerSpawnPoint.y/16-1; j <= (int) playerSpawnPoint.y/16+1; j++){
                try {
                    int tileId = floorCallable.call().intValue();
                    tileId = tileId==Utils.idMap.get("hole")?Utils.idMap.get("floor_1"):tileId;
                    tiledMapTileLayer.getCell(i,j).setTile(tiledMap.getTileSets().getTile(tileId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        tiledMap.getLayers().add(tiledMapTileLayer);
    }

    public Vector2 getAvailablePosition(Monster monster) throws TimeoutException {
        Random random = new Random(monster.hashCode() + System.currentTimeMillis());
        for (int i = 0; i < 5000; i++) {
            Vector2 position = new Vector2(random.nextInt((int) playAreaSize.x-400)+startPosition.x+400,random.nextInt((int) playAreaSize.y)+startPosition.y);
            if(monster.isCanMove(position)) return position;
        }
        throw new TimeoutException("search too long!");
    }

    public Vector2 getAvailablePosition(Equipment equipment) throws TimeoutException {
        Random random = new Random(equipment.hashCode() + System.currentTimeMillis());
        for (int i = 0; i < 5000; i++) {
            Vector2 position = new Vector2(random.nextInt((int) playAreaSize.x-400)+startPosition.x+400,random.nextInt((int) playAreaSize.y)+startPosition.y);
            if(getGameScreen().player.isCanMove(position)) return position;
        }
        throw new TimeoutException("search too long!");
    }

    public void initObstacle(){
        Random random = new Random();
        int y = (int) (playAreaSize.y/16 - 1);
        for (int x = random.nextInt(8); x < playAreaSize.x/16; x+=random.nextInt(12)+3) {
            setDefaultTile(x, (int) (1+y+(startPosition.y/16)));
            createObstacle(Utils.AllDefaultObstacleSort[random.nextInt(2)],16*x+startPosition.x,16*y+startPosition.y);
        }
        for (y = 0; y < playAreaSize.y/16-1; y++) {
            int x = random.nextInt((int) (playAreaSize.x/16)-3)+3;
            setDefaultTile(x,(int) (y+(startPosition.y/16)));
            createObstacle(Utils.AllDefaultObstacleSort[2],16*x+startPosition.x,16*y+startPosition.y);
        }
    }

    public void createObstacle(String name,float x,float y){
        Obstacle obstacle = gameScreen.getObstacleFactory().generateObstacleByName(name);
        obstacle.setPosition(new Vector2(x,y+obstacle.boxOffset.y));
        livingEntity.add(obstacle);
        gameScreen.getStage().addActor(obstacle);
    }

    public void initEntities(){
        getGameScreen().createMonsters(difficultyFactor);
    }

    public Vector2 getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    private void setDefaultTile(int x,int y){
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)tiledMap.getLayers().get(0)).getCell(x,y);
        cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_1.ordinal()));
    }

    public void render(float v){
        renderer.setView(gameScreen.getCamera());
        renderer.render();
    }

    public void dispose(){
        renderer.dispose();
        tiledMap.dispose();
        gameScreen.getStage().getActors().clear();
        gameScreen.monsterManager.reset();
        if(gameScreen.player!=null) gameScreen.getStage().addActor(gameScreen.player);
        livingEntity.clear();
        livingItem.clear();
    }
}
