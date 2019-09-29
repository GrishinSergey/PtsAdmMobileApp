package com.sagrishin.ptsadm.common.uikit.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.*
import com.sagrishin.ptsadm.common.adapter.BaseHolder
import com.sagrishin.ptsadm.common.adapter.BaseRecyclerAdapter
import com.sagrishin.ptsadm.common.adapter.holder1
import com.sagrishin.ptsadm.common.adapter.inflate
import kotlinx.android.synthetic.main.view_collapsing_calendar.view.*
import kotlinx.android.synthetic.main.view_one_calendar_line.view.*
import org.joda.time.DateTime
import kotlin.properties.Delegates.observable

class CollapsingCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var selectedDayName: String
    private lateinit var daysOfMonth: List<String>
    private lateinit var visibleDays: List<String>
    private var selectedDay: DateTime by observable(DateTime.now()) { _, _, new ->
        selectedDayName = new.dayOfMonth().asString
    }
    var onDaySelectListener: ((UiDayWithEvents) -> Unit)? = null
    var calendarEvents: List<UiDayWithEvents> by observable(emptyList()) { _, _, _ ->
        onCalendarEventsSet()
    }

    init {
        View.inflate(context, R.layout.view_collapsing_calendar, this)
        expandCalendar.setOnClickListener {
            if (days.isExpanded) {
                days.collapse()
                expandCalendar.rotate(180F, 0F, 200)
                firstLine.visibility = View.VISIBLE
            } else {
                firstLine.visibility = View.GONE
                days.expand()
                expandCalendar.rotate(0F, 180F, 200)
            }
        }
    }

    fun showMonthFor(dayInMonth: DateTime) {
        val firstDayOfMonthNumberInWeek = dayInMonth.firstDayOfMonth.dayOfWeek
        val lastDayOfMonth = dayInMonth.lastDayOfMonth
        val lastDayOfMonthNumberInWeek = lastDayOfMonth.dayOfWeek
        val lastDayOfMonthNumberInMonth = lastDayOfMonth.dayOfMonth
        val prevMonthPart = (1 until firstDayOfMonthNumberInWeek).map { "" }
        val nextMonthPart = (1..(7 - lastDayOfMonthNumberInWeek)).map { "" }

        selectedDay = dayInMonth
        daysOfMonth = prevMonthPart + (1..lastDayOfMonthNumberInMonth).map { it.toString() } + nextMonthPart

        visibleDays = if (!dayInMonth.isCurrentMonth()) {
            daysOfMonth.subList(0, 7)
        } else {
            (dayInMonth.firstDayOfWeek.dayOfMonth..dayInMonth.lastDayOfWeek.dayOfMonth).map { it.toString() }
        }
        onCalendarEventsSet()
    }

    private fun onCalendarEventsSet() {
        initVisibleLine(visibleDays)
        val daysByWeeks = daysOfMonth.withIndex().groupBy { it.index / 7 }.map { it.value.map { it.value } }
        daysContainer.adapter = BaseRecyclerAdapter(daysByWeeks.toMutableList()).apply {
            this += holder1 { MonthsDaysLine(it.inflate(R.layout.view_one_calendar_line)) }
        }
        calendarEvents.find { it.day.dayOfMonth.toString() == selectedDayName }?.let {
            onDaySelectListener?.invoke(it)
        }
    }

    private fun initVisibleLine(week: List<String>) {
        dayOfMonth1.init(week[0])
        dayOfMonth2.init(week[1])
        dayOfMonth3.init(week[2])
        dayOfMonth4.init(week[3])
        dayOfMonth5.init(week[4])
        dayOfMonth6.init(week[5])
        dayOfMonth7.init(week[6])
    }

    private fun DayOfMonthView.init(dayName: String) {
        this.dayName = dayName
        if (dayName.isNotBlank()) {
            val foundDayWithEvent = calendarEvents.find { dayName == it.day.dayOfMonth().asString }
            this.countEvents = foundDayWithEvent?.countEvents ?: 0
            this.isDaySelected = dayName == selectedDayName
            this.setOnClickListener {
                daysOfMonth.find { it == dayName }?.let {
                    selectedDayName = it
                    initVisibleLine(visibleDays)
                    this@CollapsingCalendarView.daysContainer.adapter!!.notifyDataSetChanged()
                }

                foundDayWithEvent?.let {
                    onDaySelectListener?.invoke(foundDayWithEvent)
                } ?: let {
                    onDaySelectListener?.invoke(UiDayWithEvents(DateTime.now().withDayOfMonth(dayName.toInt())))
                }
            }
        }
    }


    inner class MonthsDaysLine(v: View) : BaseHolder<List<String>>(v) {

        override fun bind(item: List<String>) {
            with(itemView) {
                dayOfMonth1.init(item[0])
                dayOfMonth2.init(item[1])
                dayOfMonth3.init(item[2])
                dayOfMonth4.init(item[3])
                dayOfMonth5.init(item[4])
                dayOfMonth6.init(item[5])
                dayOfMonth7.init(item[6])
            }
        }

    }

}
