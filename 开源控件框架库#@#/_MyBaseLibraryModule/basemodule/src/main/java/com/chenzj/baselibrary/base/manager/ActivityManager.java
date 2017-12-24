package com.chenzj.baselibrary.base.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chenzj.baselibrary.base.utils.LogUtils;

import java.util.Stack;


/**
 * Created by Jason Chen on 2017/9/1.
 */
public class ActivityManager implements Application.ActivityLifecycleCallbacks {
    public static ActivityManager manager;
    private Stack<AppCompatActivity> stacks = new Stack<>();
    private int SURVIVAL_ACTIVITY_COUNT = 0;        //存活的activity的数量

    public static ActivityManager getInstance() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    /**
     * 返回一个activity
     * @param c
     * @return
     */
    public AppCompatActivity getActivity(Class c){
        for (AppCompatActivity a : stacks) {
            LogUtils.d("a.getname = "+a.getClass().getName() +" c.getname = "+ c.getName());
            if(a.getClass().getName().equals(c.getName())){
                return a;
            }
        }
        return null;
    }

    /**
     * 返回一个activity
     * @return
     */
    public AppCompatActivity getActivity(){
        if(stacks.size() == 0){
            return null;
        }
        return stacks.get(stacks.size()-1);
    }

    /**
     * 添加activity
     *
     * @param act
     */
    public void addActivity(AppCompatActivity act) {
        stacks.add(act);
    }

    /**
     * 退出所有activity
     */
    public void removeAll() {
        for (Activity act : stacks) {
            act.finish();
        }
    }

    /**
     * 移除所有但不包括某些
     *
     * @param classes
     */
    public void removeAllNotContains(Class... classes) {
        for (Activity act : stacks) {
            boolean isContains = false;
            for (Class c : classes) {
                if (act.getClass().getName().equals(c.getName())) {
                    isContains = true;
                    break;
                }
            }
            if (isContains) {
                continue;
            } else {
                act.finish();
            }
        }
    }

    /**
     * 删除某些
     * @param classes
     */
    public void removeContains(Class... classes) {
        for (Activity act : stacks) {
            for (Class c : classes) {
                if (act.getClass().getName().equals(c.getName())) {
                    act.finish();
                }
            }
        }
    }

    public int size(){
        return stacks.size();
    }

    /**-------------------------------------Activity生命周期 ----------------------------------------**/

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        SURVIVAL_ACTIVITY_COUNT++;
        if(SURVIVAL_ACTIVITY_COUNT==1){
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        SURVIVAL_ACTIVITY_COUNT--;
        if(SURVIVAL_ACTIVITY_COUNT == 0){
            //为0说明已经退出APP，但并不是进程结束
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        stacks.remove(activity);
    }
}
