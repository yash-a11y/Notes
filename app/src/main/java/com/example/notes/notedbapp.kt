package com.example.notes

import android.app.Application

class notedbapp : Application() {

    val DB by lazy {
        note_datbase.getinstance(this)
    }
}