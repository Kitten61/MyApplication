package com.test.myapplication.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.myapplication.R
import com.test.myapplication.adapters.PhotoRecyclerAdapter
import com.test.myapplication.presenters.PhotoPresenter
import com.test.myapplication.models.ImageModel
import com.test.myapplication.views.PhotoView
import kotlinx.android.synthetic.main.fragment_grid.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


open class PhotoFragment(
    private val isPopular: Boolean,
    private val isNew: Boolean) : MvpAppCompatFragment(R.layout.fragment_grid), PhotoView {

    private var limit = 10
    private var hasNext = true
    private var loadedPages = 1
    private var isLoading = false
    private var mImageModelList = ArrayList<ImageModel>()
    private lateinit var adapter: PhotoRecyclerAdapter
    private val photoPresenter by moxyPresenter { PhotoPresenter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mLayoutManager = GridLayoutManager(context, 2)
        photoGrid.layoutManager = mLayoutManager

        adapter = PhotoRecyclerAdapter(object: PhotoRecyclerAdapter.OnItemClickListener {
            override fun onClick(imageModel: ImageModel) {
                val args = Bundle()
                args.putParcelable("item", imageModel)
                findNavController().navigate(R.id.action_navigation_photo_to_aboutFragment, args)
            }

        })
        photoGrid.adapter = adapter

        photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)

        photoGrid.setOnScrollChangeListener { _: View, _: Int, _: Int, _: Int, _: Int ->
            val lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition()
            if (lastvisibleitemposition == adapter.itemCount - 1) {
                if (!isLoading && hasNext) {
                    isLoading = true
                    loadedPages++
                    load_more_progress.visibility = View.VISIBLE
                    photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)
                }
            }
        }

        reloadPhotos.setOnRefreshListener {
            loadedPages = 1
            photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)
        }

    }

    override fun loadPhoto(imageModelList: List<ImageModel>) {
        no_internet_layout.visibility = View.GONE
        if (reloadPhotos.isRefreshing) {
            mImageModelList.clear()
            adapter.clear()
            reloadPhotos.isRefreshing = false
        }

        if (imageModelList.size < limit) {
            hasNext = false
        }

        adapter.addAll(imageModelList)
        if (isLoading) {
            load_more_progress.visibility = View.GONE
            isLoading = false
        }
    }

    override fun showError() {
        mImageModelList.clear()
        adapter.clear()
        loadedPages = 1
        no_internet_layout.visibility = View.VISIBLE
        if (reloadPhotos.isRefreshing) reloadPhotos.isRefreshing = false
        if (isLoading) {
            load_more_progress.visibility = View.GONE
            isLoading = false
        }
    }
}
