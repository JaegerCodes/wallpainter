package com.americancolors.endreverse

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.americancolors.endreverse.ar.segmentor.SeedPointAndColor
import com.americancolors.endreverse.ar.segmentor.Segmentor2ImplFinal
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Point
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }

        val btnPaint: Button = findViewById(R.id.btn_paint)
        val imageView: ImageView = findViewById(R.id.image_view)
        var dstBitmap: Bitmap
        val srcBitmap: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.room_3)
        val segmentor2 = Segmentor2ImplFinal(true, false, true, 2, 7f, 7.0)

        val rgbColor = intArrayOf(230, 126, 34)
        val seeds: MutableList<SeedPointAndColor> = ArrayList()
        seeds.add(SeedPointAndColor(Point(100.0, 60.0), rgbColor))
        // seeds.add(SeedPointAndColor(Point(400.0, 60.0), rgbColor))
        btnPaint.setOnClickListener {
            dstBitmap = segmentor2.predictAndColorMultiTapSingleMask(srcBitmap, seeds)
            imageView.setImageBitmap(dstBitmap)
        }
    }

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {}
                else -> super.onManagerConnected(status)
            }
        }
    }

}