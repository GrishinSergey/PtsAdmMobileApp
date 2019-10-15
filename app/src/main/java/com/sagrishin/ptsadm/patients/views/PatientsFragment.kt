package com.sagrishin.ptsadm.patients.views

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.MainActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.adapter.BaseRecyclerAdapter
import com.sagrishin.ptsadm.common.addOnBackPressedCallback
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.alertdialog.alert
import com.sagrishin.ptsadm.common.uikit.onScrolled
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.holders.PatientHolder
import com.sagrishin.ptsadm.patients.viewmodels.PatientsViewModel
import com.sagrishin.ptsadm.patients.views.PatientsFragmentDirections.Companion.actionPatientsToPatientAppointmentsLogDialog
import kotlinx.android.synthetic.main.fragment_patients.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatientsFragment : Fragment(R.layout.fragment_patients) {

    private val patientsViewModel: PatientsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientsViewModel.loadRemotePatients()

        activity?.addOnBackPressedCallback(this) {
            if (expandableFilter.isExpanded) {
                patientsViewModel.resetShownPatients()
                expandableFilter.collapse()
            } else {
                (activity as MainActivity).back()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        patientsViewModel.shownPatientsLiveData.observe(viewLifecycleOwner) {
            if (patients.adapter == null) {
                patients.adapter = BaseRecyclerAdapter(it.toMutableList(), this::onPatientClickListener).apply {
                    this += PatientHolder.getHolderDefinition(::onDeletePatient)
                }
            } else {
                (patients.adapter as BaseRecyclerAdapter<UiPatient>).setItems(it)
            }
        }
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        onFilterPatient.setOnClickListener {
            if (expandableFilter.isExpanded) {
                expandableFilter.collapse()
            } else {
                expandableFilter.expand()
                patientFilterText.requestFocus()
            }
        }

        patientFilterText.doOnTextChanged { text, _, _, _ ->
            patientsViewModel.filterPatientsBy(text.toString())
        }

        /**
         * For now this part is not used. New patients can be added only from phone's contacts book or web ui
         */
        fab.setOnClickListener {

        }

        patients.onScrolled { _, dy ->
            if ((dy > 0) && fab.isVisible) {
                fab.hide()
            } else {
                fab.show()
            }
        }
    }

    private fun onPatientClickListener(patient: UiPatient) {
        (activity as MainActivity).navigateTo(actionPatientsToPatientAppointmentsLogDialog(patient.id))
    }

    private fun onDeletePatient(patient: UiPatient) {
        alert {
            title = getString(R.string.delete_patient_dialog_title)
            message = getString(R.string.delete_patient_dialog_message, patient.name)
            negativeButton = R.string.no to { d -> d.dismiss() }
            positiveButton = R.string.yes to { d -> patientsViewModel.deletePatient(patient) }
        }
    }

}
