package com.example.arlab3

import android.location.Location

class Station(var name: String, var pos: Location) {

    override fun toString(): String {
        return "$name, ${pos.latitude}, ${pos.longitude}"
    }
}