package com.sagrishin.ptsadm.appointments.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.appointments.viewmodels.AppointmentsCalendarViewModel
import com.sagrishin.ptsadm.common.adapter.*
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.patients.UiPatient
import kotlinx.android.synthetic.main.dialog_patient_appointments_log.view.*
import kotlinx.android.synthetic.main.item_appointment.view.*
import org.joda.time.format.DateTimeFormat
import org.koin.android.ext.android.inject

class PatientAppointmentsLogDialog : BottomSheetDialogFragment() {

    private val appointmentsViewModel: AppointmentsCalendarViewModel by inject()
    private var patientId: Long? = null

    companion object {
        fun newInstance(patientId: Long): PatientAppointmentsLogDialog {
            return PatientAppointmentsLogDialog().apply {
                this.patientId = patientId
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appointmentsViewModel.loadAppointmentsBy(patientId!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAppearance
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        LayoutInflater.from(context).inflate(R.layout.dialog_patient_appointments_log, null).apply {
            dialog.setContentView(this)

            toolbar.setNavigationOnClickListener { dialog.dismiss() }

            appointmentsViewModel.patientAppointmentsLiveData.observe(this@PatientAppointmentsLogDialog) {
                if (it.isEmpty()) {
                    appointments.visibility = View.GONE
                    noAppointmentsText.visibility = View.VISIBLE
                } else {
                    appointments.visibility = View.VISIBLE
                    noAppointmentsText.visibility = View.GONE
                    appointments.adapter = BaseRecyclerAdapter(it.toMutableList()).apply {
                        this += AppointmentHolder.getHolderDefinition()
                    }
                }
            }
        }
    }


    class AppointmentHolder(view: View) : BaseHolder<UiAppointment>(view) {

        companion object {
            private val timeFormatter = DateTimeFormat.forPattern("HH:mm")
            private val dayFormatter = DateTimeFormat.forPattern("dd-MM-YYYY")

            fun getHolderDefinition(): HolderDefinition<UiAppointment> {
                return holder {
                    viewType = 1
                    predicate = { true }
                    generator = { AppointmentHolder(it.inflate(R.layout.item_appointment)) }
                }
            }
        }

        override fun bind(item: UiAppointment) {
            with(itemView) {
                date.text = item.dateTime.toString(dayFormatter)
                time.text = item.dateTime.toString(timeFormatter)
                description.text = item.description
            }
        }

    }

}
