package com.sagrishin.ptsadm.patients.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sagrishin.ptsadm.AuthActivity
import com.sagrishin.ptsadm.MainActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.adapter.BaseRecyclerAdapter
import com.sagrishin.ptsadm.common.addOnBackPressedCallback
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.alertdialog.alert
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.holders.PatientHolder
import com.sagrishin.ptsadm.patients.viewmodels.PatientsViewModel
import kotlinx.android.synthetic.main.fragment_patients.*
import org.koin.android.ext.android.inject

class PatientsFragment : Fragment() {

    private val patientsViewModel: PatientsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientsViewModel.loadPatients()

        activity?.addOnBackPressedCallback(this) {
            if (expandableFilter.isExpanded) {
                patientsViewModel.resetShownPatients()
                expandableFilter.collapse()
            } else {
                (activity as MainActivity).back()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_patients, container, false)
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

        fab.setOnClickListener {

        }

        patients.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((dy > 0) && (fab.visibility == View.VISIBLE)) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })

        patientsViewModel.shownPatientsLiveData.observe(this) {
            if (patients.adapter == null) {
                patients.adapter = BaseRecyclerAdapter(it.toMutableList()).apply {
                    this += PatientHolder.getHolderDefinition(::onDeletePatient)
                }
            } else {
                (patients.adapter as BaseRecyclerAdapter<UiPatient>).setItems(it)
            }
        }
    }

    private fun onDeletePatient(patient: UiPatient) {
        alert {
            title = getString(R.string.delete_patient_dialog_title)
            message = getString(R.string.delete_patient_dialog_message, patient.name)
            negativeButtonId = R.string.no to { d -> d.dismiss() }
            positiveButtonId = R.string.yes to { d -> patientsViewModel.deletePatient(patient) }
        }
    }

}
