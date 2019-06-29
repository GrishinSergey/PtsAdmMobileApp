package com.sagrishin.ptsadm.common.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.glide.transformations.BlurTransformation

fun ImageView.loadBitmapFrom(url: String, l: (resource: Bitmap) -> Unit): RequestBuilder<Bitmap> {
    return Glide.with(this).asBitmap().load(url).listener(object : SimpleResourceReadyListener<Bitmap> {
        override fun onResourceReady(resource: Bitmap, isFirst: Boolean, dataSource: DataSource): Boolean {
            l(resource)
            return true
        }
    }).apply { into(this@loadBitmapFrom) }
}

fun View.loadBackgroundFrom(url: String) {
    Glide.with(this)
        .load(url)
        .apply(bitmapTransform(BlurTransformation(12)))
        .listener {
            Observable.fromCallable { it }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                it?.let { background = it }
            }
        }
        .submit()
}

fun View.loadBackgroundFrom(@DrawableRes resId: Int) {
    Glide.with(this)
        .load(resId)
        .apply(bitmapTransform(BlurTransformation(12)))
        .listener {
            Observable.fromCallable { it }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                it?.let { background = it }
            }
        }
        .submit()
}


fun RequestBuilder<Drawable>.listener(callback: (Drawable?) -> Unit): RequestBuilder<Drawable> {
    return listener(object : SimpleDrawableReadyListener {
        override fun onResourceReady(resource: Drawable, isFirst: Boolean, dataSource: DataSource): Boolean {
            callback(resource)
            return false
        }

        override fun onLoadFailed(e: Throwable?): Boolean {
            callback(null)
            return true
        }
    })
}
