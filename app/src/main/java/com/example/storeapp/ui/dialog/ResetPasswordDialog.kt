package com.example.storeapp.ui.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.storeapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


fun Fragment.setupResetPasswordDialog(
    onSendClick : (String) -> Unit
){

    val dialog = BottomSheetDialog(requireContext())
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    val view = layoutInflater.inflate(R.layout.reset_password_item,null,false)
    dialog.setContentView(view)
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.et_email_reset_password)
    val btn_send = view.findViewById<Button>(R.id.buttonSendEmailResetPassword)
    val btn_cancel = view.findViewById<Button>(R.id.buttonCancelResetPassword)

    btn_send.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()


    }

    btn_cancel.setOnClickListener {

    }
}