package com.test.myapplication.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.myapplication.R
import com.test.myapplication.adapters.PhotoRecyclerAdapter
import com.test.myapplication.presenters.PhotoPresenter
import com.test.myapplication.util.ImageModel
import com.test.myapplication.views.PhotoView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


open class PhotoFragment(private val isPopular: Boolean, private val isNew: Boolean) : MvpAppCompatFragment(), PhotoView {

    private var limit = 10
    private var hasNext = true
    private var loadedPages = 1
    private var isLoading = false
    private var mImageModelList = ArrayList<ImageModel>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorLayout: ViewGroup
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var adapter: PhotoRecyclerAdapter
    private val photoPresenter by moxyPresenter { PhotoPresenter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        errorLayout = view.findViewById(R.id.no_internet_layout)
        loadProgressBar = view.findViewById(R.id.load_more_progress)

        val gridView: RecyclerView = view.findViewById(R.id.photoGrid)

        val mLayoutManager = GridLayoutManager(context, 2)
        gridView.layoutManager = mLayoutManager

        adapter = PhotoRecyclerAdapter(object: PhotoRecyclerAdapter.OnItemClickListener {
            override fun onClick(imageModel: ImageModel) {
                val args = Bundle()
                args.putParcelable("item", imageModel)
                findNavController().navigate(R.id.action_navigation_photo_to_aboutFragment, args)
            }

        })
        gridView.adapter = adapter


        photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)

        gridView.setOnScrollChangeListener { _: View, _: Int, _: Int, _: Int, _: Int ->
            val lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition()
            if (lastvisibleitemposition == adapter.itemCount - 1) {
                if (!isLoading && hasNext) {
                    isLoading = true
                    loadedPages++
                    loadProgressBar.visibility = View.VISIBLE
                    photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)
                }
            }
        }

        swipeRefreshLayout = view.findViewById(R.id.reloadPhotos)

        swipeRefreshLayout.setOnRefreshListener {
            loadedPages = 1
            photoPresenter.loadPhotos(loadedPages, 10, isNew, isPopular)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grid, container, false)
    }

    override fun loadPhoto(imageModelList: List<ImageModel>) {
        errorLayout.visibility = View.GONE
        if (swipeRefreshLayout.isRefreshing) {
            mImageModelList.clear()
            adapter.clear()
            swipeRefreshLayout.isRefreshing = false
        }

        if (imageModelList.size < limit) {
            hasNext = false
        }

        adapter.addAll(imageModelList)
        if (isLoading) {
            loadProgressBar.visibility = View.GONE
            isLoading = false
        }
    }

    override fun showError() {
        mImageModelList.clear()
        adapter.clear()
        loadedPages = 1
        errorLayout.visibility = View.VISIBLE
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        if (isLoading) {
            loadProgressBar.visibility = View.GONE
            isLoading = false
        }
    }
}
