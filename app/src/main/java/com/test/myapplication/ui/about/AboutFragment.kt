package com.test.myapplication.ui.about

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.test.myapplication.R
import com.test.myapplication.models.ImageModel
import kotlinx.android.synthetic.main.fragment_about.*
import moxy.MvpAppCompatFragment

class AboutFragment : MvpAppCompatFragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val item = requireArguments().getParcelable<ImageModel>("item")

        val encodedImage = item!!.base64.split(",")[1]
        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        image.setImageBitmap(decodedByte)

        description.text = item.description
        title.text = item.name
    }

}