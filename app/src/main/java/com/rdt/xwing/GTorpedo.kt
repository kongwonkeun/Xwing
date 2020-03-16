package com.rdt.starwars

import android.graphics.Bitmap
import android.graphics.PointF

class GTorpedo(val scr_w: Int, val scr_h: Int, var x: Int, var y: Int) {

    var img: Bitmap = GConfig.torpedo_img
    var w = GConfig.torpedo_w
    var h = GConfig.torpedo_h
    var dead = false
    var angle = 0f

    private val speed = 800f
    private var dir = PointF()
    private var target = PointF()

    init {
        aim_target()
    }

    fun update() {
        check_collision()
        x += (dir.x * speed * GUtil.delta_time).toInt()
        y += (dir.y * speed * GUtil.delta_time).toInt()

        if (x < -w || x > GView.scr_w || y < -h || y > GView.scr_h) {
            dead = true
        }
    }

    //
    // PRIVATE
    //
    private fun check_collision() {
        if (GView.xwing.check_collision(x, y, h)) {
            dead = true
        }
    }

    private fun aim_target() {
        target.x = GView.xwing.x.toFloat()
        target.y = GView.xwing.y.toFloat()
        val pos = PointF(x.toFloat(), y.toFloat())
        dir.set(GUtil.dir(pos, target))
        angle = GUtil.degree(pos, target)
    }

}

/* EOF */