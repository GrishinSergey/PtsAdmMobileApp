package com.sagrishin.ptsadm.common.uikit.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sagrishin.ptsadm.R
import kotlinx.android.synthetic.main.view_day_with_events_badge.view.*

class DayWithBadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var dayName: String = ""
        set(value) {
            field = value
            selectedDayName.text = field
        }
    var countEvents: Int = 0
        set(value) {
            field = value
            if (field == 0) {
                countEventsBadge.visibility = View.GONE
            } else {
                countEventsBadge.visibility = View.VISIBLE
                countEventsBadge.text = field.toString()
            }
        }

    init {
        View.inflate(context, R.layout.view_day_with_events_badge, this)
    }

}