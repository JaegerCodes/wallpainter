package com.americancolors.endreverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.americancolors.endreverse.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.americancolors.endreverse.ar.segmentor.Segmentor2ImplFinal
import com.americancolors.endreverse.ar.segmentor.SeedPointAndColor
import com.americancolors.endreverse.databinding.MainActivityBinding
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.core.Point
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
        val rgbColor = intArrayOf(255, 0, 0)
        val seeds: MutableList<SeedPointAndColor> = ArrayList()
        // seeds.add(SeedPointAndColor(Point(100.0, 60.0), rgbColor))
        seeds.add(SeedPointAndColor(Point(400.0, 60.0), rgbColor))
        btnPaint.setOnClickListener {
            dstBitmap = segmentor2.predictAndColorMultiTapSingleMask(srcBitmap, seeds)
            imageView.setImageBitmap(dstBitmap)
        }
    }

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            Log.d("HEY", "WHE")
            when (status) {
                SUCCESS -> {
                    Log.d("HEY", "CONECTADO")
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

}