package com.rdt.starwars

import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.SoundPool
import com.rdt.xwing.R
import java.util.*
import kotlin.collections.ArrayList

class GConfig {

    companion object {

        private val TAG = GConfig::class.java.simpleName

        val xwing_img: MutableList<Bitmap> = ArrayList()
        var xwing_w = 0
        var xwing_h = 0
        lateinit var alien_img: Bitmap
        var alien_w = 0
        var alien_h = 0
        lateinit var laser_img: Bitmap
        var laser_w = 0
        var laser_h = 0
        lateinit var torpedo_img: Bitmap
        var torpedo_w = 0
        var torpedo_h = 0
        val exp_img: MutableList<Bitmap> = ArrayList()
        var exp_w = 0
        var exp_h = 0

        lateinit var sound_pool: SoundPool
        var sound_laser: Int = 0
        var sound_big: Int = 0
        var sound_small: Int = 0

        val laser_list: MutableList<GLaser> = Collections.synchronizedList(ArrayList<GLaser>())
        val alien_list: MutableList<GAlien> = Collections.synchronizedList(ArrayList<GAlien>())
        val torpedo_list: MutableList<GTorpedo> = Collections.synchronizedList(ArrayList<GTorpedo>())
        val exp_list: MutableList<GExplosion> = Collections.synchronizedList(ArrayList<GExplosion>())

        fun setup(ctx: Context) {
            make_xwing(ctx)
            make_alien(ctx)
            make_laser(ctx)
            make_torpedo(ctx)
            make_exp(ctx)
            make_sound(ctx)
        }

        fun make_xwing(ctx: Context) {
            xwing_img.add(BitmapFactory.decodeResource(ctx.resources, R.drawable.xwing))
            xwing_w = xwing_img[0].width/2
            xwing_h = xwing_img[0].height/2
            val filter = LightingColorFilter(0xFF0000, 0x404040)
            val paint = Paint()
            paint.colorFilter = filter

            xwing_img.add(Bitmap.createBitmap(xwing_w*2, xwing_h*2, Bitmap.Config.ARGB_8888))
            val canvas = Canvas(xwing_img[1])
            canvas.drawBitmap(xwing_img[0], 0f, 0f, paint)
        }

        fun make_alien(ctx: Context) {
            alien_img = BitmapFactory.decodeResource(ctx.resources, R.drawable.alien)
            alien_w = alien_img.width/2
            alien_h = alien_img.height/2
        }

        fun make_laser(ctx: Context) {
            laser_img = BitmapFactory.decodeResource(ctx.resources, R.drawable.laser)
            laser_w = laser_img.width/2
            laser_h = laser_img.height/2
        }

        fun make_torpedo(ctx: Context) {
            torpedo_img = BitmapFactory.decodeResource(ctx.resources, R.drawable.torpedo)
            torpedo_w = torpedo_img.width/2
            torpedo_h = torpedo_img.height/2
        }

        fun make_exp(ctx: Context) {
            val img = BitmapFactory.decodeResource(ctx.resources, R.drawable.explosion)
            val w = img.width/5
            val h = img.height/5

            var n = 0
            for (i in 0 until 5) {
                for (j in 0 until 5) {
                    exp_img.add(Bitmap.createBitmap(img, w*j, h*i, w, h))
                    exp_img[n] = Bitmap.createScaledBitmap(exp_img[n], w*2, h*2, true)
                    n++
                }
            }
            exp_w = exp_img[0].width/2
            exp_h = exp_img[0].height/2
        }

        fun make_sound(ctx: Context) {
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            sound_pool = SoundPool.Builder()
                //.setAudioAttributes(attributes)
                .setMaxStreams(5)
                .build()
            sound_laser = sound_pool.load(ctx, R.raw.laser, 1)
            sound_big = sound_pool.load(ctx, R.raw.big_explosion, 2)
            sound_small = sound_pool.load(ctx, R.raw.small_explosion, 3)
        }

        fun play_sound(sound: String) {
            when (sound) {
                "Laser" -> { sound_pool.play(sound_laser, 1f, 1f, 9, 0, 1f) }
                "Big"   -> { sound_pool.play(sound_big,   1f, 1f, 9, 0, 1f) }
                "Small" -> { sound_pool.play(sound_small, 1f, 1f, 9, 0, 1f) }
                else -> {}
            }
        }

        fun add_laser(scr_w: Int, scr_h: Int, x: Int, y: Int) {
            synchronized(laser_list) {
                laser_list.add(GLaser(scr_w, scr_h, x, y))
            }
        }

        fun add_alien(scr_w: Int, scr_h: Int) {
            synchronized(alien_list) {
                alien_list.add(GAlien(scr_w, scr_h))
            }
        }

        fun add_torpedo(scr_w: Int, scr_h: Int, x: Int, y: Int) {
            synchronized(torpedo_list) {
                torpedo_list.add(GTorpedo(scr_w, scr_h, x, y))
            }
        }

        fun add_exp(x: Int, y: Int, type: String) {
            synchronized(exp_list) {
                exp_list.add(GExplosion(x, y, type))
            }
        }

        fun update_all() {
            synchronized(laser_list) {
                for (laser in laser_list) {
                    laser.update()
                }
            }
            synchronized(alien_list) {
                for (alien in alien_list) {
                    alien.update()
                }
            }
            synchronized(torpedo_list) {
                for (torpedo in torpedo_list) {
                    torpedo.update()
                }
            }
            synchronized(exp_list) {
                for (exp in exp_list) {
                    exp.update()
                }
            }
        }

        fun remove_dead() {
            synchronized(laser_list) {
                for (i in laser_list.size - 1 downTo 0) {
                    if (laser_list[i].dead) {
                        laser_list.remove(laser_list[i])
                    }
                }
            }
            synchronized(torpedo_list) {
                for (i in torpedo_list.size - 1 downTo 0) {
                    if (torpedo_list[i].dead) {
                        torpedo_list.remove(torpedo_list[i])
                    }
                }
            }
            synchronized(exp_list) {
                for (i in exp_list.size - 1 downTo 0) {
                    if (exp_list[i].dead) {
                        exp_list.remove(exp_list[i])
                    }
                }
            }
        }

    }

}

/* EOF */