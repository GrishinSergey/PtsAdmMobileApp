package com.sagrishin.ptsadm.common.behaviours

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sagrishin.ptsadm.R

@SuppressWarnings("UnusedDeclaration")
class FloatingActionButtonBehaviour(context: Context, a: AttributeSet) : Behavior<FloatingActionButton>(context, a) {

    private var toolbarHeight: Int = getToolbarHeight(context)

    override fun layoutDependsOn(parent: CoordinatorLayout, fab: FloatingActionButton, view: View): Boolean {
        return view is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, fab: FloatingActionButton, view: View): Boolean {
        if (view is AppBarLayout) {
            val lp = fab.layoutParams as CoordinatorLayout.LayoutParams
            val distanceToScroll = fab.height + lp.bottomMargin
            fab.translationY = -distanceToScroll * (view.getY() / toolbarHeight.toFloat())
        }
        return true
    }


    private fun getToolbarHeight(context: Context): Int {
        val styledAttributes = context.theme.obtainStyledAttributes(
            intArrayOf(R.attr.actionBarSize)
        )
        val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        return toolbarHeight
    }

}
