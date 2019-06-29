package com.sagrishin.ptsadm.patients.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.adapter.BaseRecyclerAdapter
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.holders.PatientHolder
import com.sagrishin.ptsadm.patients.viewmodels.PatientsViewModel
import kotlinx.android.synthetic.main.dialog_select_patients.view.*
import org.koin.android.ext.android.inject

class SelectPatientsDialog : BottomSheetDialogFragment() {

    private val patientsViewModel: PatientsViewModel by inject()
    lateinit var onPatientSelectListener: (UiPatient) -> Unit

    companion object {
        fun newInstance(): SelectPatientsDialog {
            return SelectPatientsDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientsViewModel.loadPatients()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAppearance
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        LayoutInflater.from(context).inflate(R.layout.dialog_select_patients, null).apply {
            dialog.setContentView(this)

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

            patientsViewModel.shownPatientsLiveData.observe(this@SelectPatientsDialog) {
                if (patients.adapter == null) {
                    patients.adapter = BaseRecyclerAdapter(it.toMutableList(), ::onPatientSelectListener).apply {
                        this += PatientHolder.getHolderDefinition()
                    }
                } else {
                    (patients.adapter as BaseRecyclerAdapter<UiPatient>).setItems(it)
                }
            }
        }
    }

    private fun onPatientSelectListener(it: UiPatient) {
        this.onPatientSelectListener.invoke(it)
        dismiss()
    }

}