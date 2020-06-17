package com.test.myapplication.presenters

import com.test.myapplication.repository.PhotoRepository
import com.test.myapplication.models.ImageModel
import com.test.myapplication.views.PhotoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class PhotoPresenter : MvpPresenter<PhotoView>() {

    private val mRepository = PhotoRepository()

    fun loadPhotos(page: Int, limit: Int, new: Boolean, popular: Boolean) {
        val disposable = mRepository.loadImages(page, limit, new, popular)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadMediaResources(it.data)
            }, {
                viewState.showError()
            })
    }

    fun loadMediaResources(imageModels: List<ImageModel>) {
        val disposable = mRepository.loadPhotoResource(imageModels)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                viewState.loadPhoto(imageModels)
            }, {
                viewState.showError()
            }
            )
    }

}