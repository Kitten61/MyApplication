package com.test.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.test.myapplication.R
import com.test.myapplication.models.ImageModel

class PhotoRecyclerAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder>() {

    private val imageList: ArrayList<ImageModel> = ArrayList()

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image)

        fun bind(
            imageModel: ImageModel,
            listener: OnItemClickListener
        ) {
            imageView.setImageBitmap(imageModel.bitmap)

            imageView.setOnClickListener {
                listener.onClick(imageModel)
            }
        }

    }

    interface OnItemClickListener {
        fun onClick(imageModel: ImageModel)
    }

    fun clear() {
        imageList.clear()
        notifyDataSetChanged()
    }

    fun add(imageModel: ImageModel) {
        imageList.add(imageModel)
        notifyDataSetChanged()
    }

    fun addAll(imageModelsList: Collection<ImageModel>) {
        imageList.addAll(imageModelsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_photo_grid, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(imageList[position], listener)
    }

}