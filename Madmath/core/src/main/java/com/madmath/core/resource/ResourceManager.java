/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:58
*/
package com.madmath.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.madmath.core.map.AnimTile;
import com.madmath.core.map.StaticTile;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResourceManager {
    public static ResourceManager defaultManager;
    public AssetManager assetManager;

    public TextureAtlas atlas;
    //public TextureAtlas back;

    public Array<TextureRegion[][]> MonsterLoad;
    public Array<TextureRegion> EquipmentLoad;
    public Array<TextureRegion[][]> ObstacleLoad;
    public Array<Sound> weaponSound;
    public Array<Sound> punchSound;
    public Sound defaultWeaponSound;
    public Sound dscream;
    public Sound click;
    public Sound button;
    public Sound moveSlow;
    public Sound moveFast;

    public Music[] levelMusic;
    public Music titleMusic;
    public Music creditMusic;

    //public TextureRegion[][] big_demon_idle_anim16x28;
    //public TextureRegion[][] big_demon_run_anim16x28;
    public TextureRegion[][] knight_f_idle_anim16x28;
    public TextureRegion[][] knight_f_run_anim16x28;
    public TextureRegion[][] fountain_red16x48;
    public TextureRegion[][] floor_spikes_anim16x16;

    public TextureRegion[][] startbutton100x50;
    public TextureRegion[][] exitbutton100x50;
    public TextureRegion[][] loadbutton100x50;
    public TextureRegion[][] menubutton100x50;
    public TextureRegion[][] cancelbutton50x50;
    public ImageButton.ImageButtonStyle startButtonStyle;
    public ImageButton.ImageButtonStyle exitButtonStyle;
    public ImageButton.ImageButtonStyle loadButtonStyle;
    public ImageButton.ImageButtonStyle menuButtonStyle;
    public ImageButton.ImageButtonStyle cancelButtonStyle;

    public TextureRegion[] tiles16x16;


    public TextureRegion scorebackground;
    public TextureRegion gamebackground700x128;
    public TextureRegion flzgbackground700x128;
    public TextureRegion background700x400;
    public TextureRegion tablebackground300x300;
    public TextureRegion gametitle200x100;
    public TextureRegion emptybutton100x50;
    public TextureRegion ui_heart_empty16x16;
    public TextureRegion ui_heart_half16x16;
    public TextureRegion ui_heart_full16x16;
    public TextureRegion contain_box_144x144;
    public TextureRegion arrow21x21;

    public Skin skin;
    public Skin dialogSkin;

    public BitmapFont font;

    public TextureRegion[][] LoadMonsterAssetsByName(String name, int width, int height){
        MonsterLoad.add(atlas.findRegion(name).split(width,height));
        return MonsterLoad.get(MonsterLoad.size-1);
    }

    public TextureRegion[][] LoadMonsterAssetsByPath(String path, int width, int height){
        MonsterLoad.add(new TextureRegion(new Texture(path)).split(width,height));
        return MonsterLoad.get(MonsterLoad.size-1);
    }

    public TextureRegion LoadEquipmentAssetsByName(String name){
        EquipmentLoad.add(atlas.findRegion(name));
        System.out.println("Equipment Load : "+name);
        return EquipmentLoad.get(EquipmentLoad.size-1);
    }

    public TextureRegion[][] LoadObstacleAssetsByName(String name,int width, int height){
        //System.out.println(name);
        ObstacleLoad.add(atlas.findRegion(name).split(width,height));
        System.out.println("Obstacle Load : "+name);
        return ObstacleLoad.get(ObstacleLoad.size-1);
    }

    public Sound getSoundByName(String name){
        try {
            assetManager.load("sfx/"+name+"_sound.wav",Sound.class);
            assetManager.finishLoading();
            weaponSound.add(assetManager.get("sfx/"+name+"_sound.wav",Sound.class));
        }catch (GdxRuntimeException e){
            //System.out.println("sfx/weapon_knight_sword_sound.wav".equals("sfx/"+name+"_sound.wav"));
            //System.out.println("sfx/"+name+"_sound.wav"+" can not be loaded!");
            return defaultWeaponSound;
        }
        System.out.println("Sound Load : sfx/"+name+"_sound.wav");
        return weaponSound.get(weaponSound.size-1);
    }

    @Nullable
    public Object getAssetsByName(String name) {
        try{
            Field field = this.getClass().getField(name);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Not Such Field"+name);
            e.printStackTrace();
            return null;
        }
    }

    public ResourceManager(){
        defaultManager = this;

        System.out.println(Gdx.files.getLocalStoragePath());

        assetManager = new AssetManager();

        assetManager.load("Texture.atlas",TextureAtlas.class);
        //assetManager.load("back.atlas",TextureAtlas.class);
        assetManager.load("skins/ui.atlas", TextureAtlas.class);
        assetManager.load("skins/dialog.atlas", TextureAtlas.class);
        assetManager.load("sfx/default_melee_sound.wav",Sound.class);
        assetManager.load("music/Juhani Junkala TitleScreen.mp3",Music.class);
        assetManager.load("music/Juhani Junkala Ending.mp3",Music.class);
        assetManager.load("music/Juhani Junkala Level 1.mp3",Music.class);
        assetManager.load("music/Juhani Junkala Level 2.mp3",Music.class);
        assetManager.load("music/Juhani Junkala Level 3.mp3",Music.class);
        assetManager.load("scorebackground.png",Texture.class);
        assetManager.load("tablebackground.png",Texture.class);
        for (int i = 0; i < 4; i++) {
            assetManager.load("sfx/sfx_wpn_punch"+(i+1)+".wav",Sound.class);
        }
        assetManager.load("sfx/sfx_deathscream_android8.wav",Sound.class);
        assetManager.load("sfx/sfx_movement_footstepsloop4_slow.wav",Sound.class);
        assetManager.load("sfx/sfx_movement_footstepsloop4_fast.wav",Sound.class);
        assetManager.load("sfx/button.wav",Sound.class);
        assetManager.load("sfx/click.wav",Sound.class);

        assetManager.finishLoading();

        punchSound = new Array<>();
        for (int i = 0; i < 4; i++) {
            punchSound.add(assetManager.get("sfx/sfx_wpn_punch"+(i+1)+".wav",Sound.class));
        }
        dscream = assetManager.get("sfx/sfx_deathscream_android8.wav",Sound.class);
        moveSlow = assetManager.get("sfx/sfx_movement_footstepsloop4_slow.wav",Sound.class);
        moveFast = assetManager.get("sfx/sfx_movement_footstepsloop4_fast.wav",Sound.class);
        button = assetManager.get("sfx/button.wav",Sound.class);
        click = assetManager.get("sfx/click.wav",Sound.class);

        weaponSound = new Array<>();
        MonsterLoad = new Array<>();
        EquipmentLoad = new Array<>();
        ObstacleLoad = new Array<>();

        defaultWeaponSound = assetManager.get("sfx/default_melee_sound.wav",Sound.class);
        titleMusic = assetManager.get("music/Juhani Junkala TitleScreen.mp3",Music.class);
        creditMusic = assetManager.get("music/Juhani Junkala Ending.mp3",Music.class);
        levelMusic = new Music[3];
        for (int i = 0; i < 3; i++) {
            levelMusic[i] = assetManager.get("music/Juhani Junkala Level "+(i+1)+".mp3",Music.class);
        }

        atlas = assetManager.get("Texture.atlas",TextureAtlas.class);
        //back = assetManager.get("back.atlas",TextureAtlas.class);

        background700x400 = atlas.findRegion("background");
        gametitle200x100 = atlas.findRegion("gametitle");
        emptybutton100x50 = atlas.findRegion("emptybutton");
        gamebackground700x128 = atlas.findRegion("hud_background");
        flzgbackground700x128 = atlas.findRegion("FLZG");
        ui_heart_empty16x16 = atlas.findRegion("ui_heart_empty");
        ui_heart_half16x16 = atlas.findRegion("ui_heart_half");
        ui_heart_full16x16 = atlas.findRegion("ui_heart_full");
        contain_box_144x144 = atlas.findRegion("contain_box");
        arrow21x21 = atlas.findRegion("arrow");

        tablebackground300x300 = new TextureRegion(assetManager.get("tablebackground.png",Texture.class));
        scorebackground = new TextureRegion(assetManager.get("scorebackground.png",Texture.class));
        /*
        gamebackground192x176 = new TextureRegion[30];
        for (int i = 0; i < 30; i++) {
            gamebackground192x176[i] = back.findRegion(Integer.toString(1+i));
        }

         */

        tiles16x16 = new TextureRegion[StaticTile.TileSort.values().length];
        int tilesIndex = 0;
        for (StaticTile.TileSort k: StaticTile.TileSort.values()) {
            String s = k.name().split("_")[k.name().split("_").length-1];
            if(s.matches("\\d+")){
                tiles16x16[tilesIndex++] = atlas.findRegion(k.name().substring(0,k.name().lastIndexOf('_')),Integer.parseInt(s));
            }else {
                tiles16x16[tilesIndex++] = atlas.findRegion(k.name());
            }
        }

        knight_f_idle_anim16x28 = atlas.findRegion("knight_f_idle_anim").split(16,28);
        knight_f_run_anim16x28 = atlas.findRegion("knight_f_run_anim").split(16,28);
        startbutton100x50 = atlas.findRegion("startbutton").split(100,50);
        exitbutton100x50 = atlas.findRegion("exitbutton").split(100,50);
        loadbutton100x50 = atlas.findRegion("loadbutton").split(100,50);
        menubutton100x50 = atlas.findRegion("menubutton").split(100,50);
        cancelbutton50x50 = atlas.findRegion("cancelbutton").split(50,50);
        fountain_red16x48 = atlas.findRegion("wall_fountain_red_anim").split(16,48);
        floor_spikes_anim16x16 = atlas.findRegion(AnimTile.TileSort.floor_spikes_anim.name()).split(16,16);

        startButtonStyle = new ImageButton.ImageButtonStyle(null,null,null,new TextureRegionDrawable(startbutton100x50[1][0]),new TextureRegionDrawable(startbutton100x50[0][0]),null);
        startButtonStyle.imageOver = new TextureRegionDrawable(startbutton100x50[0][0]);
        startButtonStyle.pressedOffsetY = -5;
        exitButtonStyle = new ImageButton.ImageButtonStyle(null,null,null,new TextureRegionDrawable(exitbutton100x50[1][0]),new TextureRegionDrawable(exitbutton100x50[0][0]),null);
        exitButtonStyle.imageOver = new TextureRegionDrawable(exitbutton100x50[0][0]);
        exitButtonStyle.pressedOffsetY = -5;
        loadButtonStyle = new ImageButton.ImageButtonStyle(null,null,null,new TextureRegionDrawable(loadbutton100x50[1][0]),new TextureRegionDrawable(loadbutton100x50[0][0]),null);
        loadButtonStyle.imageOver = new TextureRegionDrawable(loadbutton100x50[0][0]);
        loadButtonStyle.pressedOffsetY = -5;
        menuButtonStyle = new ImageButton.ImageButtonStyle(null,null,null,new TextureRegionDrawable(menubutton100x50[1][0]),new TextureRegionDrawable(menubutton100x50[0][0]),null);
        menuButtonStyle.imageOver = new TextureRegionDrawable(menubutton100x50[0][0]);
        menuButtonStyle.pressedOffsetY = -5;
        cancelButtonStyle = new ImageButton.ImageButtonStyle(null,null,null,new TextureRegionDrawable(cancelbutton50x50[1][0]),new TextureRegionDrawable(cancelbutton50x50[0][0]),null);
        cancelButtonStyle.imageOver = new TextureRegionDrawable(cancelbutton50x50[0][0]);
        cancelButtonStyle.pressedOffsetY = -5;

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), atlas.findRegion("font/font.fnt"), false);
        font.setUseIntegerPositions(false);

        skin = new Skin(assetManager.get("skins/ui.atlas", TextureAtlas.class));
        skin.add("default-font", font);
        skin.load(Gdx.files.internal("skins/ui.json"));

        dialogSkin = new Skin(assetManager.get("skins/dialog.atlas", TextureAtlas.class));
        dialogSkin.add("default-font", font);
        dialogSkin.load(Gdx.files.internal("skins/dialog.json"));
    }

    public void dispose() {
        assetManager.dispose();
        atlas.dispose();
        font.dispose();
    }
}
