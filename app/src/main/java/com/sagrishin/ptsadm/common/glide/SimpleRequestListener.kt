package com.sagrishin.ptsadm.common.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

interface SimpleRequestListener<T> : RequestListener<T> {

    fun onLoadFailed(e: Throwable?): Boolean

    fun onResourceReady(resource: T, isFirst: Boolean, dataSource: DataSource): Boolean


    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<T>,
        isFirst: Boolean
    ): Boolean = onLoadFailed(e)

    override fun onResourceReady(
        resource: T,
        model: Any,
        target: Target<T>,
        dataSource: DataSource,
        isFirst: Boolean
    ): Boolean = onResourceReady(resource, isFirst, dataSource)

}


typealias SimpleDrawableReadyListener = SimpleRequestListener<Drawable>
typealias SimpleBitmapReadyListener = SimpleRequestListener<Bitmap>

@FunctionalInterface
interface SimpleResourceReadyListener<T> : SimpleRequestListener<T> {

    override fun onLoadFailed(e: Throwable?): Boolean = true


    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<T>,
        isFirst: Boolean
    ): Boolean = onLoadFailed(e)

    override fun onResourceReady(
        resource: T,
        model: Any,
        target: Target<T>,
        dataSource: DataSource,
        isFirst: Boolean
    ): Boolean = onResourceReady(resource, isFirst, dataSource)

}
