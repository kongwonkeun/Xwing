package com.rdt.starwars

import android.graphics.Bitmap

class GExplosion(var x: Int, var y: Int, type: String) {

    var img: Bitmap
    var w = GConfig.exp_w
    var h = GConfig.exp_h
    var dead = false

    private var anim_delay = 0.04f
    private var anim_span = 0f
    private var id = 0

    init {
        if (type == "Small") {
            id = 20
            anim_delay = 0.1f
        }
        img = GConfig.exp_img[id]
    }

    fun update() {
        anim_span += GUtil.delta_time
        if (anim_span > anim_delay) {
            anim_span = 0f
            id += 1
            img = GConfig.exp_img[id]
            dead = (id == 24)
        }
    }

}

/* EOF */