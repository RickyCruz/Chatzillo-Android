package com.konztic.chatzillo.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.konztic.chatzillo.R
import com.konztic.chatzillo.utilities.toast

class RateDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, which ->
                activity!!.toast("Ok")
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which ->
                activity!!.toast("Cancel")
            }
            .create()
    }
}