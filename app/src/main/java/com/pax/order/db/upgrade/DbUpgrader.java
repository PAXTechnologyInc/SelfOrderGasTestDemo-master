package com.pax.order.db.upgrade;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import com.pax.order.logger.AppLog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * ============================================================================		
 * = COPYRIGHT		
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION		
 *   This software is supplied under the terms of a license agreement or		
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied		
 *   or disclosed except in accordance with the terms in that agreement.		
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.		
 * Description: // Detail description about the function of this module,		
 *             // interfaces with the other modules, and dependencies. 		
 * Revision History:		
 * Date	                 Author	                Action
 * 2018/8/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public abstract class DbUpgrader {

    private static final String TAG = "DB Upgrader";

    protected enum OperationType {
        ADD,
        DELETE,
    }

    public static void upgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion,
                               int newVersion, String packagePath) {
        try {
            Class<?> c1 = Class.forName(packagePath + ".Upgrade" + oldVersion + "To" + newVersion);
            DbUpgrader upgrader = (DbUpgrader) c1.newInstance();
            AppLog.i("DbUpgrader", "upgrading from version(" + oldVersion
                    + ") to version(" + newVersion + ")");
            upgrader.upgrade(db, cs);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            AppLog.w(TAG, "", e);
            throw new IllegalArgumentException("No Upgrader for " + db.toString()
                    + " from version(" + oldVersion
                    + ") to version(" + newVersion + ")");
        }
    }

    private static void createTable(SQLiteDatabase db, ConnectionSource cs, Class clazz) throws SQLException {
        try {
            String sql = (String) TableUtils.getCreateTableStatements(cs, clazz).get(0);
            db.execSQL(sql);
        } catch (Exception e) {
            AppLog.w(TAG, e);
            TableUtils.createTable(cs, clazz);
        }
    }

    protected static void upgradeTable(SQLiteDatabase db, ConnectionSource cs, Class clazz,
                                       OperationType type, Map<String, Object> columnWithValue) throws SQLException {
        String tableName = extractTableName(clazz);

        db.beginTransaction();
        try {
            //Rename table
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            db.execSQL(sql);

            //Create table
            createTable(db, cs, clazz);

            //Load data
            String columns;
            if (type == OperationType.ADD) {
                columns = Arrays.toString(getColumnNames(db, tempTableName));
            } else if (type == OperationType.DELETE) {
                columns = Arrays.toString(getColumnNames(db, tableName));
            } else {
                throw new IllegalArgumentException("OperationType error");
            }

            columns = columns.replace("[", "").replace("]", "");
            sql = "INSERT INTO " + tableName + "(" + columns + ") " + "SELECT " + columns
                    + " FROM " + tempTableName;
            db.execSQL(sql);

            // update default value
            if (type == OperationType.ADD && columnWithValue != null && columnWithValue.size() > 0) {
                String[] sets = columnWithValue.keySet().toArray((new String[columnWithValue.size()]));
                for (int i = 0; i < sets.length; ++i) {
                    sets[i] += ("='" + columnWithValue.get(sets[i]).toString() + "'");
                }
                String newColumn = Arrays.toString(sets).replace("[", "")
                        .replace("]", "");
                sql = "UPDATE " + tableName + " SET " + newColumn;
                db.execSQL(sql);
            }

            //Drop temp table
            sql = "DROP TABLE IF EXISTS " + tempTableName;
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    private static String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * from " + tableName, null);
            if (cursor != null) {
                columnNames = cursor.getColumnNames();
            }
        } catch (Exception e) {
            AppLog.w(TAG, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }

    private static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name = null;
        if (databaseTable != null && databaseTable.tableName() != null
                && !databaseTable.tableName().isEmpty()) {
            name = databaseTable.tableName();
        }

        if (name == null) {
            name = clazz.getSimpleName().toLowerCase();
        }

        return name;
    }

    protected abstract void upgrade(SQLiteDatabase db, ConnectionSource cs);
}
