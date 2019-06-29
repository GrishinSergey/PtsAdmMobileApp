package com.sagrishin.ptsadm.common.uikit.schedule

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.common.ClickSize
import com.sagrishin.ptsadm.common.animateClick
import com.sagrishin.ptsadm.patients.UiPatient
import kotlinx.android.synthetic.main.item_day_schedule_hour.view.*
import kotlinx.android.synthetic.main.item_patient.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class DayScheduleHourItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var onAppointmentActionListener: (UiPatient) -> Unit
    lateinit var onAddAppointmentListener: (DateTime) -> Unit
    lateinit var onDeleteAppointmentListener: (Long, DateTime) -> Unit
    private val formatter = DateTimeFormat.forPattern("HH:mm")

    init {
        View.inflate(context, com.sagrishin.ptsadm.R.layout.item_day_schedule_hour, this)
        resetPatient(View.INVISIBLE)

        addAppointment.setOnClickListener {
            addAppointment.animateClick(200, ClickSize.MIDDLE) {
                onAddAppointmentListener(getAppointmentTime())
            }
        }
    }

    fun setTime(formattedHour: String) {
        time.text = formattedHour
    }

    fun setPatient(patientData: UiPatient) {
        resetPatient(View.VISIBLE)
        avatarImageView.setText("${patientData.name[0]}${patientData.surname[0]}")
        patientFullName.text = "${patientData.name} ${patientData.surname}"
        phoneNumber.text = patientData.phoneNumber
        patient.setOnLongClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            onDeleteAppointmentListener(patientData.appointments[0].id, getAppointmentTime())
            true
        }
        patient.setOnClickListener {
            onAppointmentActionListener(patientData)
        }
    }

    fun resetPatient(visibility: Int = View.INVISIBLE) {
        avatarImageView.visibility = visibility
        patientFullName.visibility = visibility
        phoneNumber.visibility = visibility
        patient.setOnLongClickListener(null)
        with(avatarImageView.parent as ConstraintLayout) {
            if (avatarImageView.visibility == View.INVISIBLE) {
                isClickable = false
                isFocusable = false
                setAddAppointmentVisibility(View.VISIBLE, true)
            } else {
                isClickable = true
                isFocusable = true
                setAddAppointmentVisibility(View.INVISIBLE, false)
            }
        }
    }

    private fun setAddAppointmentVisibility(visibility: Int, isClickable: Boolean) {
        with (addAppointment) {
            this.visibility = visibility
            this.isClickable = isClickable
            this.isFocusable = isClickable
        }
    }

    private fun getAppointmentTime(): DateTime {
        return formatter.parseDateTime(time.text.toString())
    }

}
