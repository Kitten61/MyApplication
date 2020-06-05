package com.test.myapplication.util

import io.realm.RealmObject

open class MediaResourceModel(
    var file: String,
    var id: Int
): RealmObject() {
    constructor() : this("", 0)
}