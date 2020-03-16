package com.rdt.starwars

import android.graphics.Bitmap
import kotlin.random.Random

class GAlien(val scr_w: Int, val scr_h: Int) {

    var img: Bitmap = GConfig.alien_img
    var w = GConfig.alien_w
    var h = GConfig.alien_h
    var x = 0
    var y = 0

    private var speed = 0f
    private var dir = 0
    private var fire_delay = 0f
    private var hit_cnt = 0
    private val ran = Random

    init {
        initialize()
    }

    fun update() {
        fire()
        x += (speed * dir * GUtil.delta_time).toInt()
        if (x < -w*2 || x > GView.scr_w + w*2) {
            initialize()
        }
    }

    fun check_collision(tx: Int, ty: Int, tw: Int, th: Int): Boolean {
        val hit = GUtil.check_collision(x, y, w, h, tx, ty, tw, th)
        if (!hit) {
            return false
        }
        hit_cnt += 1
        if (hit_cnt >= 4) {
            GConfig.play_sound("Big")
            GConfig.add_exp(x, y, "Big")
            initialize()
        } else {
            GConfig.play_sound("Small")
            GConfig.add_exp(x, y, "Small")
        }
        return true
    }

    //
    // PRIVATE
    //
    private fun initialize() {
        speed = ran.nextFloat()*101 + 400
        y = ran.nextInt(201) + h
        if (ran.nextInt(2) == 1) {
            x = -w*2
            dir = 1
        } else {
            x = GView.scr_w + w*2
            dir = -1
        }
        fire_delay = ran.nextFloat()*2 + 1
        hit_cnt = 0
    }

    private fun fire() {
        fire_delay -= GUtil.delta_time
        if (fire_delay < 0) {
            GConfig.add_torpedo(GView.scr_w, GView.scr_h, x, y)
            fire_delay = ran.nextFloat()*2 + 1
        }
    }

}

/* EOF */