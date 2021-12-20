package com.madmath.core.network;

import com.badlogic.gdx.utils.Array;
import com.madmath.core.main.MadMath;
import com.madmath.core.screen.GameScreen;

import java.util.concurrent.Semaphore;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 上午4:14
 */
public class Task {
    static public Task taskExc;

    public MadMath game;
    private final Semaphore semaphore;
    private final Semaphore taskFinish;
    private Runnable task;

    public Task(MadMath game){
        this.game = game;
        semaphore = new Semaphore(1);
        taskFinish = new Semaphore(0);
    }

    public void addTask(Runnable runnable){
        try {
            semaphore.acquire();
            task = runnable;
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runTask(){
        if(task!=null){
            task.run();
            task = null;
            taskFinish.release();
        }
    }

    public void finishTask() {
        try {
            taskFinish.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
