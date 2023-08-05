package com.patrickmota.brewery.detail.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class ShowMap {
    operator fun invoke (context: Context, geoLocation: Uri?) {
        val intent = Intent(Intent.ACTION_VIEW, geoLocation)
        intent.setPackage("com.google.android.apps.maps")

        try {
            startActivity(context, intent, null)
        } catch (e: Throwable) {
            Toast.makeText(
                context,
                com.patrickmota.brewery.common.R.string.fragment_detail_error_location,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}