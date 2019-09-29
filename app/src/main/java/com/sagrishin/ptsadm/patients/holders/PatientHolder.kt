package com.sagrishin.ptsadm.patients.holders

import android.view.View
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.adapter.*
import com.sagrishin.ptsadm.patients.UiPatient
import kotlinx.android.synthetic.main.item_patient.view.*

class PatientHolder(v: View) : BaseHolder<UiPatient>(v) {

    private var onDeletePatient: ((UiPatient) -> Unit)? = null

    companion object {
        fun getHolderDefinition(onDeletePatient: ((UiPatient) -> Unit)? = null): HolderDefinition<UiPatient> {
            return holder1 {
                PatientHolder(it.inflate(R.layout.item_patient)).apply {
                    this.onDeletePatient = onDeletePatient
                }
            }
        }
    }

    override fun bind(item: UiPatient) {
        with(itemView) {
            avatarImageView.setText("${item.name[0]}${item.surname[0]}")
            patientFullName.text = "${item.name} ${item.surname}"
            phoneNumber.text = item.phoneNumber

            onDeletePatient?.let {
                deletePatientIcon.visibility = View.VISIBLE
                deletePatientIcon.setOnClickListener { it(item) }
            } ?: let {
                deletePatientIcon.visibility = View.GONE
                deletePatientIcon.setOnClickListener(null)
            }
        }
    }

    override fun bind(item: UiPatient, cl: ClickListener<UiPatient>) {
        bind(item)
        itemView.setOnClickListener { cl(item) }
    }

}
