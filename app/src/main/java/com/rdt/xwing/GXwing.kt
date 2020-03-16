package com.rdt.starwars

import android.graphics.Bitmap

class GXwing(val scr_w: Int, val scr_h: Int) {

    var img: Bitmap = GConfig.xwing_img[0]
    var w = GConfig.xwing_w
    var h = GConfig.xwing_h
    var x = 0
    var y = 0

    private val anim_delay = 0.5f
    private var anim_span = 0f
    private var id = 0
    private val max_speed = 1200
    private var speed = 0

    private var state = MyState.IDLE
    private var cur_x = 0
    private var dir = 0

    init {
        x = GView.scr_w / 2
        y = GView.scr_h - h - 40
    }

    fun update() {
        animate()
        when (state) {
            MyState.START -> { speed = GUtil.lerp(speed, max_speed, 5 * GUtil.delta_time) }
            MyState.STOP  -> { speed = GUtil.lerp(speed, 0, 10 * GUtil.delta_time) }
            else -> {}
        }
        x += (speed * dir * GUtil.delta_time).toInt()

        if (GUtil.distance_square(x, y, cur_x, y) < 2500) {
            state = MyState.STOP
        }
        if (x < w || x > GView.scr_w - w) {
            x -= (speed * dir * GUtil.delta_time).toInt()
            speed = 0
            dir = 0
        }
    }

    fun control(tx: Int, ty: Int) {
        if (GUtil.touch(x, y, h, tx, ty)) {
            fire()
        } else {
            start_move(tx)
        }
    }

    fun check_collision(tx: Int, ty: Int, tr: Int): Boolean {
        val hit = GUtil.check_collision(x, y, (h*0.7f).toInt(), tx, ty, tr)
        if (hit) {
            GConfig.play_sound("Small")
            GConfig.add_exp(tx, ty + tr, "Small")
        }
        return hit
    }

    fun fire_1_() {
        GConfig.play_sound("Laser")
        GConfig.add_laser(GView.scr_w, GView.scr_h, x, y)
    }

    fun fire_2_() {
        GConfig.play_sound("Laser")
        GConfig.add_laser(GView.scr_w, GView.scr_h, x - w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x + w, y)
    }

    fun fire_3_() {
        GConfig.play_sound("Laser")
        GConfig.add_laser(GView.scr_w, GView.scr_h, x, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x - w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x + w, y)
    }

    fun fire_5_() {
        GConfig.play_sound("Laser")
        GConfig.add_laser(GView.scr_w, GView.scr_h, x, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x - w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x + w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x - w - w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x + w + w, y)
    }

    fun start_move_(dir_: Int) {
        cur_x = 0
        dir = dir_
        if (dir == 1) {
            cur_x += 100
        } else if (dir == -1) {
            cur_x -= 100
        }
        state = MyState.START
    }

    //
    // PRIVATE
    //
    private fun animate() {
        anim_span += GUtil.delta_time
        if (anim_span > anim_delay) {
            anim_span = 0f
            id = 1 - id
            img = GConfig.xwing_img[id]
        }
    }

    private fun fire() {
        GConfig.play_sound("Laser")
        GConfig.add_laser(GView.scr_w, GView.scr_h, x - w, y)
        GConfig.add_laser(GView.scr_w, GView.scr_h, x + w, y)
    }

    private fun start_move(loc_x: Int) {
        cur_x = loc_x
        if (x < loc_x) {
            dir = 1
        } else {
            dir = -1
        }
        state = MyState.START
    }

    //
    // INNER CLASS
    //
    enum class MyState(val i: Int) {
        IDLE(1),
        START(2),
        STOP(3),
        UNKNOWN(4)
    }

}

/* EOF */