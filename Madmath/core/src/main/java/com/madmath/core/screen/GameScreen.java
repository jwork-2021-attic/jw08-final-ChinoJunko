/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Server;
import com.madmath.core.control.PlayerInputProcessor;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.entity.creature.MonsterFactory;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.inventory.equipment.EquipmentFactory;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.entity.obstacle.ObstacleFactory;
import com.madmath.core.network.MyClient;
import com.madmath.core.network.listener.PlayerActListener;
import com.madmath.core.network.listener.PlayerConnectListener;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.serializer.GameTitle;
import com.madmath.core.serializer.MySerializer;
import com.madmath.core.thread.MonsterThread;
import com.madmath.core.thread.PlayerThread;
import com.madmath.core.ui.HUD;
import com.madmath.core.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.*;

public class GameScreen extends AbstractScreen{

    static private GameScreen CurrencyGameScreen;

    HUD hud;

    public GameMap map;

    public InputMultiplexer multiplexer;

    ExecutorService executorService;

    public Semaphore playerSemaphore;
    public Semaphore monsterSemaphore;

    public Player player;
    public MonsterThread monsterManager;

    private MonsterFactory monsterFactory;
    private EquipmentFactory equipmentFactory;
    private ObstacleFactory obstacleFactory;

    public Map<Integer,Player> teammate;
    public boolean isOnline = false;
    public MyClient myClient;
    public Server myServer;


    Label currencyMapMessage;


    //collision detection

    float stateTime;
    float currencyDelta;
    float factor=1f;


    public GameScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        CurrencyGameScreen = this;

        teammate = new HashMap<>();

        monsterFactory = new MonsterFactory(manager,this);
        monsterManager = new MonsterThread();

        equipmentFactory = new EquipmentFactory(manager, this);

        obstacleFactory = new ObstacleFactory(manager, this);

        multiplexer = new InputMultiplexer();
        hud = new HUD(this, manager);
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(stage);

        playerSemaphore = new Semaphore(0);
        monsterSemaphore = new Semaphore(0);

        executorService = Executors.newCachedThreadPool();
        executorService.execute(new PlayerThread());
        executorService.execute(monsterManager);
        //executorService.shutdown();

        setBattleLevel(1);
    }

    public void setBattleLevel(int battleLevel) {
        music = manager.levelMusic[battleLevel-1];
    }

    @Override
    public void show() {
        if(isOnline&&myServer!=null){
            myServer.addListener(new PlayerConnectListener(myServer,game));
            myServer.addListener(new PlayerActListener(myServer,game));
        }
        if(state==State.READY){
            if(player==null)    createPlayer();
            resetMap();
            new GameMap(this,"PRIMARY",1,factor);
            map.initTileMap();
        }
        super.show();
        hud.show();
        updateMapTitle();
        currencyMapMessage.setPosition(stage.getViewport().getWorldWidth()/2-50, MadMath.V_HEIGHT-20);
        currencyMapMessage.setZIndex(1000);
        state = State.RUNING;
        CurrencyGameScreen = this;
        Gdx.input.setInputProcessor(multiplexer);
        stateTime = 0;
        camera.zoom =  0.7f;
    }

    public void nextMap(){
        int tlevel = map.mapLevel;
        resetMap();
        String s = "PRIMARY";
        if(tlevel>5) s = "ADVANCED";
        if(tlevel>10) s = "CRAZY";
        new GameMap(this,s,tlevel+1,factor);
        map.initTileMap();
        updateMapTitle();
        currencyMapMessage.setPosition(stage.getViewport().getWorldWidth()/2-50, MadMath.V_HEIGHT-20);
        currencyMapMessage.setZIndex(1000);
        player.setPosition(map.getPlayerSpawnPoint());
        try (Output output = new Output(new FileOutputStream("./save/autosave.bin"),50<<10);){
            MySerializer.defaultKryo.writeObject(output,new GameTitle(map.name,map.mapLevel,map.difficultyFactor, player.score));
            MySerializer.defaultKryo.writeObject(output,player);
            MySerializer.defaultKryo.writeObject(output,map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void nextMap(GameMap map){
        updateMapTitle();
        currencyMapMessage.setPosition(stage.getViewport().getWorldWidth()/2-50, MadMath.V_HEIGHT-20);
        currencyMapMessage.setZIndex(1000);
        player.setPosition(map.getPlayerSpawnPoint());
    }

    public void resetMap(){
        if(map!=null){
            map.dispose();
            map = null;
        }
    }

    public void updateMapTitle(){
        if(currencyMapMessage!=null)    stage.getActors().removeValue(currencyMapMessage,true);
        currencyMapMessage = new Label("LEVEL "+map.mapLevel+"  "+map.name, new Label.LabelStyle(manager.font, Color.YELLOW ));
        currencyMapMessage.setFontScale(0.5f);
        currencyMapMessage.setVisible(true);
        currencyMapMessage.addAction(Actions.sequence(Actions.delay(5f),Actions.run(() -> currencyMapMessage.setText("   GOOD LUCK!   "))));
        stage.addActor(currencyMapMessage);
    }

    public void resetGame(){
        state = State.READY;
        stage.clear();
        if(player!=null){
            multiplexer.removeProcessor(player.inputProcessor);
            player.dispose();
        }
        player = null;
        hud.reset();
    }

    public void updateCamera(){
        if(Math.abs(camera.zoom-1f)>0.01f) {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*camera.zoom*stage.getViewport().getWorldWidth(), 0.5f * camera.zoom * stage.getViewport().getWorldWidth()),map.playAreaSize.x-0.5f* camera.zoom*stage.getViewport().getWorldWidth());
            camera.position.y = Math.min(Math.max(player.getY()-(0.5f-0.382f)*camera.zoom*stage.getViewport().getWorldHeight(), 0.5f * camera.zoom * stage.getViewport().getWorldHeight()),(1-0.5f* camera.zoom)*stage.getViewport().getWorldHeight());
        }else {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*stage.getViewport().getWorldWidth(), (float) stage.getViewport().getWorldWidth()/2),map.startPosition.x+map.playAreaSize.x-stage.getViewport().getWorldWidth()/2);
            camera.position.y = viewport.getWorldHeight()/2f;
        }
        camera.update();
        //System.out.println("Playerx:"+player.getX()+"    viewportwith:"+camera.viewportWidth+"    playerArs:"+map.playAreaSize.x);
    }


    @Override
    public void resize(int i, int i1) {
        super.resize(i, i1);
        hud.resize(i,i1);
    }

    @Override
    public void update(float v) {
        super.update(v);
        updateCamera();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stateTime += v;
        currencyDelta = v;
        if(state == State.RUNING){
            checkDie();
            playerSemaphore.release();
            monsterSemaphore.release();
            Sort.instance().sort(stage.getRoot().getChildren(), (o2, o1) -> (int)(o1.getUserObject()==null?
                    (o2.getUserObject()==null?
                            (Float.compare(o1.getY(), o2.getY())): (Float.compare(o1.getY(), o2.getY() + (int) o2.getUserObject()))):
                    o2.getUserObject()==null?
                            (Float.compare(o1.getY()+(int)o1.getUserObject(),o2.getY())):(Float.compare(o1.getY()+(int)o1.getUserObject(),o2.getY()+(int)o2.getUserObject()))));
            stage.act(v);
        }
        update(v);
        map.render(v);
        stage.draw();
        hud.render(v);
    }

    public Player createPlayer(){
        return addPlayer(new Player(0,Player.initPlayerAnim(game.manager),this));
    }

    public Player addPlayer(Player player){
        if(this.player!=null){
            removeInputProcessor(player);
            stage.getActors().removeValue(this.player,true);
        }
        this.player = player;
        stage.addActor(player);
        addInputProcessor(new PlayerInputProcessor(player));
        return player;
    }

    public Player addTeammate(Player mate){
        if((player!=null&&player.getId()==mate.getId())||teammate.get(mate.getId())!=null)  return mate;
        map.livingEntity.add(mate);
        teammate.put(mate.getId(),mate);
        stage.addActor(mate);
        return mate;
    }

    public void checkDie(){
        Array<Monster> toDie = new Array<>();
        monsterManager.monsters.forEach((id,monster) -> {
            if(monster.getHp()<=0){
                toDie.add(monster);
            }
        });
        toDie.forEach(monster -> {
            monster.Die();
        });
        if(player.getHp()<=0) player.Die();
        teammate.forEach((id,mate)->{
            if(mate.getHp()<=0) mate.Die();
        });
    }

    public void createMonsters(float factor) {
        if(map==null)   return;
        int totalLevel = Math.round((map.mapLevel*16 + 32) * factor);
        int capacity = 500;
        Random random = new Random(System.currentTimeMillis());
        int monsterId = random.nextInt(Monster.monsterSort.size);
        for (int i = 0; i < capacity && totalLevel>0; monsterId = random.nextInt(Monster.monsterSort.size)) {
            try{
                if(Monster.monsterSort.get(monsterId).level>(2+totalLevel)/3)  continue;
                totalLevel -= Objects.requireNonNull(generateMonsterWithPosition((String) Monster.monsterSort.get(monsterId).getClass().getField("alias").get(null))).level;
                i++;
            } catch (NoSuchFieldException | IllegalAccessException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public Monster generateMonster(int mId, String name) {//alias
        Monster monster = monsterFactory.generateMonsterByName(name, mId);
        monsterManager.addMonster(monster);
        stage.addActor(monster);
        map.livingEntity.add(monster);
        return monster;
    }

    @Nullable
    public Monster generateMonster(String name){//alias
        Monster monster = monsterFactory.generateMonsterByName(name);
        monsterManager.addMonster(monster);
        stage.addActor(monster);
        map.livingEntity.add(monster);
        return monster;
    }

    @Nullable
    public Monster generateMonsterWithPosition(String name) throws TimeoutException {//alias
        Monster monster = generateMonster(name);
        monster.setPosition(map.getAvailablePosition(monster));
        return monster;
    }

    public EquipmentFactory getEquipmentFactory() {
        return equipmentFactory;
    }

    public ObstacleFactory getObstacleFactory(){
        return obstacleFactory;
    }

    public void createEquipment(){
        for (int i = 0; i < Utils.AllDefaultEquipmentSort.length; i++) {
            generateEquipment(Utils.AllDefaultEquipmentSort[i],map.getPlayerSpawnPoint().x+50+50*(i/2),map.getPlayerSpawnPoint().y+50-100*(i%2));
        }
    }

    @Nullable
    public Player selectPlayer(int id){
        if(id==player.getId())return player;
        return teammate.getOrDefault(id,null);
    }

    public Equipment generateEquipment(String name,float x,float y){
        Equipment equipment = equipmentFactory.generateEquipmentByName(name);
        equipment.setPosition(x,y);
        stage.addActor(equipment);
        map.livingItem.add(equipment);
        return equipment;
    }

    public Equipment createEquipmentByName(String name){
        Equipment equipment = equipmentFactory.generateEquipmentByName(name);
        stage.addActor(equipment);
        //map.livingItem.add(equipment);
        return equipment;
    }

    public GameMap getMap() {
        return map;
    }

    public float getCurrencyDelta() {
        return currencyDelta;
    }

    public void changeDifficulty(float factor){
        this.factor = factor;
    }

    @Override
    public void switchScreen(Screen screen) {
        super.switchScreen(screen);
        // getViewport().update();
        if(myServer!=null){
            myServer.close();
            myServer = null;
        }else if(myClient!=null){
            myClient.close();
            myClient = null;
        }
    }

    @Override
    public void pause() {
        super.pause();
        player.sfxUpdate();
        music.pause();
    }

    @Override
    public void resume() {
        super.resume();
        music.play();
    }

    public void addInputProcessor(InputProcessor inputProcessor){
        multiplexer.addProcessor(0,inputProcessor);
    }

    public void removeInputProcessor(Player player){
        for (int i = multiplexer.size()-1; i >= 0; i--) {
            InputProcessor inputProcessor = multiplexer.getProcessors().get(i);
            if(inputProcessor instanceof PlayerInputProcessor && ((PlayerInputProcessor) inputProcessor).player == player){
                multiplexer.removeProcessor(i);
                break;
            }
        }
    }

    public static GameScreen getCurrencyGameScreen() {
        return CurrencyGameScreen;
    }

}
