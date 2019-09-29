package com.sagrishin.ptsadm.patients.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.adapter.BaseRecyclerAdapter
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.holders.PatientHolder
import com.sagrishin.ptsadm.patients.viewmodels.PatientsViewModel
import kotlinx.android.synthetic.main.dialog_select_patients.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectPatientsDialog : BottomSheetDialogFragment() {

    lateinit var onSelectListener: (UiPatient) -> Unit
    private val patientsViewModel: PatientsViewModel by viewModel()

    companion object {
        fun newInstance(onSelectListener: (UiPatient) -> Unit): SelectPatientsDialog {
            return SelectPatientsDialog().apply { this.onSelectListener = onSelectListener }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientsViewModel.loadAllPatients()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAppearance
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

            toolbar.setNavigationOnClickListener { dialog.dismiss() }

            patientsViewModel.shownPatientsLiveData.observe(this@SelectPatientsDialog) {
                if (patients.adapter == null) {
                    patients.adapter = BaseRecyclerAdapter(it.toMutableList()) { uiPatient ->
                        onSelectListener(uiPatient)
                        dismiss()
                    }.apply {
                        this += PatientHolder.getHolderDefinition()
                    }
                } else {
                    (patients.adapter as BaseRecyclerAdapter<UiPatient>).setItems(it)
                }
            }
        }
    }

}
