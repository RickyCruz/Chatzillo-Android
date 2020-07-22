package com.konztic.chatzillo.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.konztic.chatzillo.R
import com.konztic.chatzillo.models.NewRateEvent
import com.konztic.chatzillo.models.Rate
import com.konztic.chatzillo.utilities.RxBus
import com.konztic.chatzillo.utilities.toast
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogLayout = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(dialogLayout)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                val textRate = dialogLayout.edit_text_rate_feedback.text.toString()
                val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run { "" }
                val rate = Rate(textRate, dialogLayout.rating_bar_feedback.rating, Date(), imgURL)

                RxBus.publish(NewRateEvent(rate))
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                activity!!.toast("Canceled")
            }
            .create()
    }
}