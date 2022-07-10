package com.example.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [notes_entity::class],version = 1)
abstract class note_datbase : RoomDatabase(){

    abstract fun getnoteDao() : noteDao



    companion object
    {
        @Volatile
        private var Instance : note_datbase ?= null

        //singeltane

        fun getinstance(context : Context) : note_datbase{

            var instance = Instance

            synchronized(this){
                if(instance == null)
                {
                   instance = Room.databaseBuilder(
                       context.applicationContext,
                       note_datbase::class.java,
                       "noteDB"
                   ).fallbackToDestructiveMigration().build()
                }
                Instance = instance
                }
            return instance!!
        }

    }


}