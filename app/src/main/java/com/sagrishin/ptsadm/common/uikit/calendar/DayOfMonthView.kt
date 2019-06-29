package com.sagrishin.ptsadm.common.uikit.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.sagrishin.ptsadm.R
import kotlinx.android.synthetic.main.item_day_of_month.view.*

class DayOfMonthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var dayName: String = ""
        set(value) {
            day.text = value
            field = value
            visibility = if (field.isEmpty()) View.INVISIBLE else View.VISIBLE
        }
    var countEvents: Int = 0
        set(value) {
            eventsMarker.setImageDrawable(getEventMarkerBy(value))
        }
    var isDaySelected: Boolean = false
        set(value) {
            field = value
            if (field) {
                setBackgroundResource(R.drawable.bg_current_day_name)
            } else {
                background = null
            }
        }

    init {
        View.inflate(context, R.layout.item_day_of_month, this)

        context.obtainStyledAttributes(attrs, R.styleable.DayOfMonthView).use { attributes ->
            val countEvents = attributes.getInt(R.styleable.DayOfMonthView_countEvents, 0)
            val dayName = attributes.getString(R.styleable.DayOfMonthView_dayName)
            val isDaySelected = attributes.getBoolean(R.styleable.DayOfMonthView_isDaySelected, false)

            eventsMarker.setImageDrawable(getEventMarkerBy(countEvents))
            day.text = dayName

            if (isDaySelected) {
                setBackgroundResource(R.drawable.bg_current_day_name)
            } else {
                background = null
            }
        }
    }


    private fun getEventMarkerBy(countEvents: Int): Drawable? {
        return when {
            countEvents == 1 -> context.resources.getDrawable(R.drawable.ic_one_event, context.theme)
            countEvents == 2 -> context.resources.getDrawable(R.drawable.ic_two_events, context.theme)
            countEvents >= 3 -> context.resources.getDrawable(R.drawable.ic_many_events, context.theme)
            else -> null
        }
    }

}