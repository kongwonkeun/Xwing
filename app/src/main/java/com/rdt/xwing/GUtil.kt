package com.rdt.starwars

import android.graphics.PointF
import kotlin.math.*

class GUtil {

    companion object {

        var current_time: Long = System.nanoTime()
        var delta_time: Float = 0f

        fun update_time() {
            delta_time = (System.nanoTime() - current_time) / 1000000000f
            current_time = System.nanoTime()
        }

        fun distance(p1: PointF, p2: PointF): Int {
            return sqrt(((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y)).toDouble()).toInt()
        }

        fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return sqrt( ((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)).toDouble() ).toFloat()
        }

        fun distance_square(x1: Int, y1: Int, x2: Int, y2: Int): Int {
            return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)
        }

        fun touch(p: PointF, r: Float, t: PointF): Boolean {
            return (p.x-t.x)*(p.x-t.x) + (p.y-t.y)*(p.y-t.y) < r*r
        }

        fun touch(px: Int, py: Int, r: Int, tx: Int, ty: Int): Boolean {
            return (px-tx)*(px-tx) + (py-ty)*(py-ty) < r*r
        }

        fun lerp(p1: PointF, p2: PointF, rate: Float): PointF {
            if (distance(p1, p2) < 1f) {
                return p2
            }
            val pos = PointF()
            val dx = p2.x - p1.x
            val dy = p2.y - p1.y
            val dy_by_dx = dy/dx
            if (dx == 0f) {
                pos.x = p1.x
                pos.y = p1.y + dy*rate
            } else {
                pos.x = p1.x + dx*rate
                pos.y = p1.y + dx*dy_by_dx*rate
            }
            return pos
        }

        fun lerp(start: Int, end: Int, rate: Float): Int {
            return (start + (end-start)*rate).toInt()
        }

        fun check_collision(px: Int, py: Int, pw: Int, ph: Int, tx: Int, ty: Int, tw: Int, th: Int): Boolean {
            return (pw+tw) >= abs(px-tx) && (ph+th) >= abs(py-ty)
        }

        fun check_collision(px: Int, py: Int, pr: Int, tx: Int, ty: Int, tr: Int): Boolean {
            return (px-tx)*(px-tx) + (py-ty)*(py-ty) <= (pr+tr)*(pr+tr)
        }

        fun collide(px: Float, py: Float, pr: Float, tx: Float, ty: Float, tw: Float, th: Float): Boolean {
            return abs(px-tx) <= (tw+pr) && abs(py-ty) <= (th+pr)
        }

        fun degree(p1: PointF, p2: PointF): Float {
            val radian: Double = -atan2(p2.y - p1.y, p2.x - p1.x).toDouble()
            return (90 - Math.toDegrees(radian)).toFloat()
        }

        fun clamp(cur_val: Float, min_val: Float, max_val: Float): Float {
            return max(min_val, min(cur_val, max_val))
        }

        fun dir(p: PointF, t: PointF): PointF {
            val radian = -atan2(t.y-p.y, t.x-p.x)
            val pos = PointF()
            pos.x = cos(radian)
            pos.y = -sin(radian)
            return pos
        }

    }

}

/* EOF */