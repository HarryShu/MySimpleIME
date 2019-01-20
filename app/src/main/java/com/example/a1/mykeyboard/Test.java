package com.example.a1.mykeyboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by a1 on 2017/10/17.
 */

public class Test {


    public enum UnDockItem{

        SWITCH_ITEM{
            @Override
            int matchCode() {
                return 0;
            }

            @Override
            Cursor doQuery() {
                return null;
            }

            @Override
            void doUpdate() {
                Log.i("Tag","doUpdate");
            }
        },
        CONFIG_ITEM {
            @Override
            int matchCode() {
                return 0;
            }

            @Override
            Cursor doQuery() {
                return null;
            }

            @Override
            void doUpdate() {

            }
        },
        LANDSCAPE_CONFIG_ITEM{
            @Override
            int matchCode() {
                return 0;
            }

            @Override
            Cursor doQuery() {
                return null;
            }

            @Override
            void doUpdate() {

            }
        };


        abstract int matchCode();
        abstract Cursor doQuery();
        abstract void doUpdate();
    }


    public static UnDockItem getAA(int code){
        for(UnDockItem item : UnDockItem.values()){
            if(code==item.matchCode()){
                return item;
            }
        }
        return null;
    }


}
