package com.sagrishin.ptsadm.common

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.RectF
import android.view.View
import android.view.animation.*
import android.widget.ProgressBar
import androidx.core.animation.doOnEnd

fun View.animateAlpha(durationInMillis: Long, visibility: Int, callback: (() -> Unit)? = null) {
    val animation = (if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)).apply {
        duration = durationInMillis
        fillAfter = true

        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                isClickable = true
                callback?.invoke()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }
    this.visibility = visibility

    startAnimation(animation)
}


fun View.rotate(from: Float, to: Float, durationInMillis: Long, callback: (() -> Unit)? = null) {
    RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
        duration = durationInMillis
        fillAfter = true

        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                isClickable = true
                callback?.invoke()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        startAnimation(this)
    }
}


fun ProgressBar.animateProgress(from: Double, to: Double, callback: (() -> Unit)? = null) {
    val progressAnimator = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            progress = (from + (to - from) * interpolatedTime).toInt()
        }
    }

    progressAnimator.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            isClickable = false
        }

        override fun onAnimationEnd(animation: Animation) {
            isClickable = true
            callback?.invoke()
        }

        override fun onAnimationRepeat(animation: Animation) {

        }
    })

    startAnimation(progressAnimator)
}


fun View.resize(newHeight: Int, isSquired: Boolean = false, callback: (() -> Unit)? = null) {
    val newHeightInPx = (newHeight * resources.displayMetrics.density).toInt()
    ValueAnimator.ofInt(layoutParams.height, newHeightInPx).let { valueAnimator ->
        valueAnimator.duration = 250

        if (isSquired) {
            valueAnimator.addUpdateListener { animation ->
                layoutParams.height = animation.animatedValue as Int
                layoutParams.width = animation.animatedValue as Int
                requestLayout()
            }
        } else {
            valueAnimator.addUpdateListener { animation ->
                layoutParams.height = animation.animatedValue as Int
                requestLayout()
            }
        }

        valueAnimator.doOnEnd { callback?.invoke() }

        AnimatorSet().apply {
            interpolator = AccelerateDecelerateInterpolator()
            play(valueAnimator)
            start()
        }
    }
}


enum class ClickSize {
    SMALL, MIDDLE, LARGE
}

fun View.animateClick(durationInMillis: Long, size: ClickSize, callback: (() -> Unit)? = null) {
    val from = 1.00f
    val to = when (size) {
        ClickSize.SMALL -> 0.85F
        ClickSize.MIDDLE -> 0.89F
        ClickSize.LARGE -> 0.93F
    }

    ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
        interpolator = CycleInterpolator(1f)
        duration = durationInMillis

        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                isClickable = true
                callback?.invoke()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        startAnimation(this)
    }
}


fun View.animateBounce(
    durationInMillis: Long,
    rectF: RectF,
    startOffset: Long,
    fillAfter: Boolean,
    repeatCount: Int
) {
    startAnimation(TranslateAnimation(rectF.left, rectF.right, rectF.top, rectF.bottom).apply {
        this.startOffset = startOffset
        this.duration = durationInMillis
        this.fillAfter = fillAfter
        this.interpolator = BounceInterpolator()
        this.repeatCount = repeatCount
    })
}


fun View.animateBounceVertical(
    durationInMillis: Long,
    from: Float,
    to: Float,
    startOffset: Long,
    fillAfter: Boolean,
    repeatCount: Int
) {
    animateBounce(durationInMillis, RectF(0F, 0F, from, to), startOffset, fillAfter, repeatCount)
}
