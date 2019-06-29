package com.sagrishin.ptsadm.appointments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.sagrishin.ptsadm.MainActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.appointments.AppointmentsCalendarFragmentDirections.Companion.actionMonthNext
import com.sagrishin.ptsadm.appointments.AppointmentsCalendarFragmentDirections.Companion.actionMonthPrev
import com.sagrishin.ptsadm.appointments.viewmodels.AppointmentsCalendarViewModel
import com.sagrishin.ptsadm.common.*
import com.sagrishin.ptsadm.common.glide.loadBackgroundFrom
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.alertdialog.alert
import com.sagrishin.ptsadm.common.uikit.calendar.UiDayWithEvents
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.views.SelectPatientsDialog
import kotlinx.android.synthetic.main.fragment_appointments_calendar.*
import kotlinx.android.synthetic.main.view_day_with_events_badge.*
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentsCalendarFragment : Fragment() {

    private val appointmentsViewModel: AppointmentsCalendarViewModel by viewModel()
    private var isToolbarVisible = true
    private var isCalendarControllerVisible = false

    private lateinit var selectedDate: DateTime
    private lateinit var dayInMonth: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this) {
            if (dayInMonth.isCurrentMonth()) {
                (activity as MainActivity).close()
            } else {
                (activity as MainActivity).back()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_appointments_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.animateAlpha(20, View.INVISIBLE)
        dayInMonth = getPassedParameter()
        appointmentsViewModel.loadMonthEvents(dayInMonth)
        appBar.loadBackgroundFrom(getToolbarBackgroundResourceId(dayInMonth.monthOfYear))
        monthName.text = dayInMonth.monthOfYear().asText.capitalize()
        bindAppbarWith(dayInMonth)
        calendar.onDaySelectListener = this::onDaySelectListener
        calendar.showMonthFor(dayInMonth)
        schedule.onAddAppointmentListener = this::onAddAppointment
        schedule.onDeleteAppointmentListener = this::onDeleteAppointment
        schedule.onAppointmentActionListener = this::onAppointmentActionListener
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            onAppBarScrollOffsetChangeListener(offset)
        })
        prevMonth.setOnClickListener { onShowPrevMonth(dayInMonth) }
        nextMonth.setOnClickListener { onShowNextMonth(dayInMonth) }
        appointmentsViewModel.monthData.observe(viewLifecycleOwner, this::onMonthDataReceived)
        appointmentsViewModel.dayEvents.observe(viewLifecycleOwner, schedule::setEvents)
    }

    private fun getPassedParameter(): DateTime = arguments?.let {
        AppointmentsCalendarFragmentArgs.fromBundle(it).dayInMonth
    } ?: let {
        currentDayDateTime
    }

    private fun onAppBarScrollOffsetChangeListener(offset: Int) {
        val percentage = Math.abs(offset).toFloat() / appBar.totalScrollRange.toFloat()

        if (percentage <= 0.9F) {
            if (isToolbarVisible) {
                toolbar.animateAlpha(200, View.GONE)
                isToolbarVisible = false
            }
        } else {
            if (!isToolbarVisible) {
                toolbar.animateAlpha(200, View.VISIBLE)
                isToolbarVisible = true
            }
        }

        if (percentage <= 0.15F) {
            if (!isCalendarControllerVisible) {
                calendarController.animateAlpha(200, View.VISIBLE)
                isCalendarControllerVisible = true
            }
        } else {
            if (isCalendarControllerVisible) {
                calendarController.animateAlpha(200, View.INVISIBLE)
                isCalendarControllerVisible = false
            }
        }
    }

    private fun getToolbarBackgroundResourceId(monthNumber: Int): Int {
        return when (monthNumber) {
            1 -> R.drawable.ic_toolbar_january
            2 -> R.drawable.ic_toolbar_fabruary
            3 -> R.drawable.ic_toolbar_march
            4 -> R.drawable.ic_toolbar_april
            5 -> R.drawable.ic_toolbar_may
            6 -> R.drawable.ic_toolbar_june
            7 -> R.drawable.ic_toolbar_july
            8 -> R.drawable.ic_toolbar_august
            9 -> R.drawable.ic_toolbar_september
            10 -> R.drawable.ic_toolbar_october
            11 -> R.drawable.ic_toolbar_november
            12 -> R.drawable.ic_toolbar_december
            else -> throw RuntimeException("Unexpected month number: $monthNumber")
        }
    }

    private fun bindAppbarWith(day: DateTime, countEvents: Int = 0) {
        with(currentlySelectedDay) {
            this.dayName = day.dayOfMonth().asString
            this.countEvents = countEvents
        }

        this.countEvents.text = when (countEvents) {
            0 -> ""
            else -> getPluralBy(R.plurals.countEvents, countEvents)
        }

        this.dayNumber.text = selectedDayName.text
        this.toolbarMonthName.text = day.monthOfYear().asText
    }

    private fun onMonthDataReceived(uiMonthData: UiMonthData) {
        calendar.calendarEvents = uiMonthData.perDayEvents
    }

    private fun onDaySelectListener(dayWithEvents: UiDayWithEvents) {
        bindAppbarWith(dayWithEvents.day, dayWithEvents.countEvents)
        appointmentsViewModel.loadEventsForDay(dayWithEvents.day)
        selectedDate = dayWithEvents.day
    }

    private fun onAddAppointment(selectedHourWithMinutes: DateTime) {
        SelectPatientsDialog.newInstance().apply {
            onPatientSelectListener = { uiPatient ->
                val dateTime = selectedDate.plusHours(selectedHourWithMinutes.hourOfDay)
                    .plusMinutes(selectedHourWithMinutes.minuteOfHour)
                appointmentsViewModel.saveNewAppointment(uiPatient, dateTime)
            }
        }.show(fragmentManager!!, SelectPatientsDialog::class.java.canonicalName)
    }

    private fun onDeleteAppointment(id: Long, dateTime: DateTime) {
        alert {
            titleId = R.string.delete_appointment_dialog_title
            messageId = R.string.delete_appointment_dialog_message
            negativeButtonId = R.string.no to { d -> d.cancel() }
            positiveButtonId = R.string.yes to { d ->
                val appointmentDate = selectedDate.plusHours(dateTime.hourOfDay).plusMinutes(dateTime.minuteOfHour)
                appointmentsViewModel.deleteAppointmentBy(id, appointmentDate)
            }
        }
    }

    private fun onAppointmentActionListener(patient: UiPatient) {
        alert {
            titleId = R.string.call_to_patient_dialog_title
            message = context!!.getString(R.string.call_dialog_message, patient.name, patient.phoneNumber)
            positiveButtonId = R.string.yes to { d ->
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${patient.phoneNumber}")))
            }
        }
    }

    private fun onShowNextMonth(dayInMonth: DateTime) {
        val nextMonthDay = dayInMonth.plusOneMonth()
        val firstDayOfOfPrevMonth = if (nextMonthDay.isCurrentMonth()) {
            currentDayDateTime
        } else {
            nextMonthDay.firstDayOfMonth.toDateTimeAtStartOfDay()
        }
        (activity as MainActivity).navigateTo(actionMonthNext(firstDayOfOfPrevMonth))
    }

    private fun onShowPrevMonth(dayInMonth: DateTime) {
        val prevMonthDay = dayInMonth.minusOneMonth()
        val firstDayOfOfPrevMonth = if (prevMonthDay.isCurrentMonth()) {
            currentDayDateTime
        } else {
            prevMonthDay.firstDayOfMonth.toDateTimeAtStartOfDay()
        }
        (activity as MainActivity).navigateTo(actionMonthPrev(firstDayOfOfPrevMonth))
    }

}
