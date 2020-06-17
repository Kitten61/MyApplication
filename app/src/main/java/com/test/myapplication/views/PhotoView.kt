package com.test.myapplication.views

import com.test.myapplication.models.ImageModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface PhotoView : MvpView {
    fun loadPhoto(imageModelList: List<ImageModel>)
    fun showError()
}