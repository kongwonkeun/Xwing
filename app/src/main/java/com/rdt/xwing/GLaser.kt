package com.rdt.starwars

import android.graphics.Bitmap
import android.graphics.PointF

class GLaser(val scr_w: Int, val scr_h: Int, var x: Int, var y: Int) {

    var img: Bitmap = GConfig.laser_img
    var w = GConfig.laser_w
    var h = GConfig.laser_h
    var dead = false

    private val speed = 1200f // 1200f
    private var dir = PointF()

    init {
        dir.x = 0f
        dir.y = speed
    }

    fun update() {
        for (alien in GConfig.alien_list) {
            if (alien.check_collision(x, y, w, h)) {
                dead = true
                break
            }
        }
        y -= (dir.y * GUtil.delta_time).toInt()
        if (y < -h) {
            dead = true
        }
    }

}

/* EOF */