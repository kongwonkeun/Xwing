package com.rdt.starwars

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rdt.xwing.R
import java.lang.Exception
import kotlin.random.Random

class GView(context: Context, attr: AttributeSet?) : View(context, attr) {

    companion object {
        lateinit var xwing: GXwing
        var scr_w = 0
        var scr_h = 0
    }
    private val TAG = GView::class.java.simpleName
    private lateinit var background_img: Bitmap
    private var thread: MyThread? = null
    private var ctx: Context = context
    private val ran = Random

    init {
        GConfig.setup(ctx)
    }

    //
    // IMPLEMENT VIEW
    //
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        canvas.drawBitmap(background_img, 0f, 0f, null)
        synchronized(GConfig.laser_list) {
            for (laser in GConfig.laser_list) {
                canvas.drawBitmap(laser.img, (laser.x - laser.w).toFloat(), (laser.y - laser.h).toFloat(), null)
            }
        }
        synchronized(GConfig.torpedo_list) {
            for (torpedo in GConfig.torpedo_list) {
                canvas.rotate( torpedo.angle, torpedo.x.toFloat(), torpedo.y.toFloat())
                canvas.drawBitmap(torpedo.img, (torpedo.x - torpedo.w).toFloat(), (torpedo.y - torpedo.h).toFloat(), null)
                canvas.rotate(-torpedo.angle, torpedo.x.toFloat(), torpedo.y.toFloat())
            }
        }
        synchronized(GConfig.alien_list) {
            for (alien in GConfig.alien_list) {
                canvas.drawBitmap(alien.img, (alien.x - alien.w).toFloat(), (alien.y - alien.h).toFloat(), null)
            }
        }
        canvas.drawBitmap(xwing.img, (xwing.x - xwing.w).toFloat(), (xwing.y - xwing.h).toFloat(), null)
        synchronized(GConfig.exp_list) {
            for (exp in GConfig.exp_list) {
                canvas.drawBitmap(exp.img, (exp.x - exp.w).toFloat(), (exp.y - exp.h).toFloat(), null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            xwing.control(event.x.toInt(), event.y.toInt())
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scr_w = w
        scr_h = h
        background_img = BitmapFactory.decodeResource(resources, R.drawable.sky)
        background_img = Bitmap.createScaledBitmap(background_img, w, h, true)
        xwing = GXwing(w, h)
        if (thread == null) {
            thread = MyThread()
            thread!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        if (thread != null) {
            thread!!.running = false
        }
        super.onDetachedFromWindow()
    }


    //
    // PRIVATE
    //
    private fun update() {
        xwing.update()
        GConfig.update_all()
    }

    private fun remove_dead() {
        GConfig.remove_dead()
    }

    private fun make_alien() {
        if (GConfig.alien_list.size < 6 && ran.nextInt(1000) < 1) {
            GConfig.add_alien(scr_w, scr_h)
        }
    }

    //
    // INNER CLASS
    //
    inner class MyThread : Thread() {

        var running = true

        override fun run() {
            while (running) {
                try {
                    GUtil.update_time()
                    make_alien()
                    update()
                    remove_dead()
                    postInvalidate()
                    sleep(10)
                } catch (e: Exception) {
                    //
                }
            }
        }

    }

}

/* EOF */