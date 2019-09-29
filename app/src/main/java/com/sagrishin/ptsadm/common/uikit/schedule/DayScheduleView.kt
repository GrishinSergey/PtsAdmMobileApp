package com.sagrishin.ptsadm.common.uikit.schedule

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.plusAssign
import com.sagrishin.ptsadm.patients.UiPatient
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import kotlin.properties.Delegates.observable

class DayScheduleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MORNING_HOUR_NUMBER = 7
        private const val EVENING_HOUR_NUMBER = 22
    }

    var onAddAppointmentListener: (DateTime) -> Unit by observable({ _ -> }) { _, _, _ ->
        children.forEach { (it as DayScheduleHourItem).onAddAppointmentListener = onAddAppointmentListener }
    }
    var onDeleteAppointmentListener: (Long, DateTime) -> Unit by observable({ _, _ -> }) { _, _, _ ->
        children.forEach { (it as DayScheduleHourItem).onDeleteAppointmentListener = onDeleteAppointmentListener }
    }
    var onAppointmentActionListener: (UiPatient) -> Unit by observable({_ -> }) { _, _, _ ->
        children.forEach { (it as DayScheduleHourItem).onAppointmentActionListener = onAppointmentActionListener }
    }
    private val formatter = DateTimeFormat.forPattern("HH:mm")

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL

        val dayRange = MORNING_HOUR_NUMBER..EVENING_HOUR_NUMBER
        val hours = dayRange.map { "%02d:00".format(it) }
        val halfAndHours = dayRange.map { "%02d:30".format(it) }
        hours.zip(halfAndHours.dropLast(1)) { a, b -> listOf(a, b) }.flatten().forEach {
            this += DayScheduleHourItem(context).apply {
                setTime(it)
                tag = it
            }
        }
    }

    fun setEvents(patientsWithAppointments: List<UiPatient>) {
        children.forEach { (it as DayScheduleHourItem).resetPatient() }
        patientsWithAppointments.forEach {
            findViewWithTag<DayScheduleHourItem>(it.appointments[0].dateTime.format()).setPatient(it)
        }
    }

    private fun DateTime.format(format: DateTimeFormatter = formatter): String = toString(format)

}
