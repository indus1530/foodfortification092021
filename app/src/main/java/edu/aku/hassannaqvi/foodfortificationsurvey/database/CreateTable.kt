package edu.aku.hassannaqvi.foodfortificationsurvey.database

import edu.aku.hassannaqvi.foodfortificationsurvey.contracts.TableContracts.*
import edu.aku.hassannaqvi.foodfortificationsurvey.core.MainApp.PROJECT_NAME

object CreateTable {

    const val DATABASE_NAME = "$PROJECT_NAME.db"
    const val DATABASE_COPY = "${PROJECT_NAME}_copy.db"
    const val DATABASE_VERSION = 1

    const val SQL_CREATE_FORMS = ("CREATE TABLE "
            + FormsTable.TABLE_NAME + "("
            + FormsTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsTable.COLUMN_UID + " TEXT,"
            + FormsTable.COLUMN_ENUM_BLOCK + " TEXT,"
            + FormsTable.COLUMN_HHID + " TEXT,"
            + FormsTable.COLUMN_USERNAME + " TEXT,"
            + FormsTable.COLUMN_SYSDATE + " TEXT,"
            + FormsTable.COLUMN_ISTATUS + " TEXT,"
            + FormsTable.COLUMN_DEVICEID + " TEXT,"
            + FormsTable.COLUMN_DEVICETAGID + " TEXT,"
            + FormsTable.COLUMN_SYNCED + " TEXT,"
            + FormsTable.COLUMN_SYNCED_DATE + " TEXT,"
            + FormsTable.COLUMN_APPVERSION + " TEXT,"
            + FormsTable.COLUMN_SA1 + " TEXT,"
            + FormsTable.COLUMN_SA2 + " TEXT,"
            + FormsTable.COLUMN_SA3 + " TEXT,"
            + FormsTable.COLUMN_SB1 + " TEXT,"
            + FormsTable.COLUMN_SC1 + " TEXT,"
            + FormsTable.COLUMN_SC2 + " TEXT,"
            + FormsTable.COLUMN_SD1 + " TEXT,"
            + FormsTable.COLUMN_SE1 + " TEXT,"
            + FormsTable.COLUMN_SF1 + " TEXT,"
            + FormsTable.COLUMN_SF2 + " TEXT,"
            + FormsTable.COLUMN_SF3 + " TEXT,"
            + FormsTable.COLUMN_SG1 + " TEXT,"
            + FormsTable.COLUMN_SG2 + " TEXT,"
            + FormsTable.COLUMN_SG3 + " TEXT,"
            + FormsTable.COLUMN_SG4 + " TEXT,"
            + FormsTable.COLUMN_SG5 + " TEXT,"
            + FormsTable.COLUMN_SG6 + " TEXT,"
            + FormsTable.COLUMN_SG7 + " TEXT"
            + " );"
            )


    const val SQL_CREATE_USERS = ("CREATE TABLE "
            + UsersTable.TABLE_NAME + "("
            + UsersTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UsersTable.COLUMN_USERNAME + " TEXT,"
            + UsersTable.COLUMN_PASSWORD + " TEXT,"
            + UsersTable.COLUMN_FULLNAME + " TEXT"
            + " );"
            )


    const val SQL_CREATE_ENUMBLOCKS = ("CREATE TABLE "
            + EnumBlocksTable.TABLE_NAME + "("
            + EnumBlocksTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EnumBlocksTable.COLUMN_DISTRICT_NAME + " TEXT,"
            + EnumBlocksTable.COLUMN_TEHSIL_NAME + " TEXT,"
            + EnumBlocksTable.COLUMN_ENUM_BLOCK_CODE + " TEXT"
            + " );"
            )


    const val SQL_CREATE_RANDOM = ("CREATE TABLE "
            + RandomTable.TABLE_NAME + "("
            + RandomTable.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + RandomTable.COLUMN_SNO + " TEXT,"
            + RandomTable.COLUMN_ENUM_BLOCK_CODE + " TEXT,"
            + RandomTable.COLUMN_HH_NO + " TEXT,"
            + RandomTable.COLUMN_HEAD_HH + " TEXT"
            + " );"
            )

    const val SQL_CREATE_VERSIONAPP = ("CREATE TABLE "
            + VersionTable.TABLE_NAME + " ("
            + VersionTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VersionTable.COLUMN_VERSION_CODE + " TEXT, "
            + VersionTable.COLUMN_VERSION_NAME + " TEXT, "
            + VersionTable.COLUMN_PATH_NAME + " TEXT "
            + ");"
            )


}