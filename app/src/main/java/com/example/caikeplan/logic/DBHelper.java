package com.example.caikeplan.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 2017/3/2.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final int VERSION=1;
    public static final String TABLE_NAME_ADVER="adverdb";//广告表

    /**
    * 广告表信息
    * */

    //创建表
    private static final String SQL_CREATE_ADVER="create table IF NOT EXISTS "+TABLE_NAME_ADVER+"(_id integer primary key autoincrement," +
            "ad_id varchar(8),end varchar(32),title varchar(128),url varchar(128),typeid varchar(2),imageurl varchar(128),flag varchar(2))";
    //删除表
    private static final String SQL_DROP_ADVER="drop table if exists plistdb"+TABLE_NAME_ADVER;
    //初始化
    private static DBHelper sDBHelper=null;

    public DBHelper(Context context) {
        super(context, TABLE_NAME_ADVER, null, VERSION);
    }

    public static DBHelper getInstance(Context context){
        if(sDBHelper==null){
            sDBHelper=new DBHelper(context);
        }
        return sDBHelper;
    }

    /**
     * 这个方法
     * 1、在第一次打开数据库的时候才会走
     * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
     * 3、没有清除数据，不会走这个方法
     * 4、数据库升级的时候这个方法不会走
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("dml","onCreate");
        initAllTables(db);
    }

    /**
     * * 执行数据库的降级操作
    * * 1、只有新版本比旧版本低的时候才会执行
    * * 2、如果不执行降级操作，会抛出异常
    */
    @SuppressLint("NewApi") @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.i("dml","onDown");
        dropAllTables(db);
        initAllTables(db);
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * 初始化所有表
     * @param db
     */
    private void initAllTables(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ADVER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除所有表
     * @param db
     */
    private void dropAllTables(SQLiteDatabase db) {
        Log.i("dml","onDrop");
        db.execSQL(SQL_DROP_ADVER);
        db.execSQL(SQL_CREATE_ADVER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
