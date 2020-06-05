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
import com.test.myapplication.util.ImageModel
import moxy.MvpAppCompatFragment

class AboutFragment : MvpAppCompatFragment() {

    private lateinit var mDescriptionView: TextView
    private lateinit var mTitleView: TextView
    private lateinit var mImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val item = requireArguments().getParcelable<ImageModel>("item")

        mImageView = view.findViewById(R.id.image)
        mDescriptionView = view.findViewById(R.id.description)
        mTitleView = view.findViewById(R.id.title)

        val encodedImage = item!!.base64.split(",")[1]
        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        mImageView.setImageBitmap(decodedByte)

        mDescriptionView.text = item.description
        mTitleView.text = item.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_about, container, false)
    }
}