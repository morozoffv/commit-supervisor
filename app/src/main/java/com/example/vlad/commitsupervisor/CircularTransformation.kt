package com.example.vlad.commitsupervisor

import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Color.parseColor
import com.squareup.picasso.Transformation


/**
 * Created by vlad on 23/11/2017.
 */
class CircularTransformation(radius: Int) : Transformation {

    private var mRadius : Int = 10

    init {
        this.mRadius = radius
    }

    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, source.width, source.height)

        paint.setAntiAlias(true)
        paint.setFilterBitmap(true)
        paint.setDither(true)

        canvas.drawARGB(0, 0, 0, 0)

        paint.setColor(Color.parseColor("#BAB399"))

        if (mRadius == 0) {
            canvas.drawCircle(source.width / 2 + 0.7f, source.height / 2 + 0.7f,
                    source.width / 2 - 1.1f, paint)
        } else {
            canvas.drawCircle(source.width / 2 + 0.7f, source.height / 2 + 0.7f,
                    mRadius + 0f, paint)
        }

        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

        canvas.drawBitmap(source, rect, rect, paint)

        if (source != output) {
            source.recycle()
        }
        return output
    }

    override fun key(): String {
        return "circular" + mRadius.toString()
    }
}