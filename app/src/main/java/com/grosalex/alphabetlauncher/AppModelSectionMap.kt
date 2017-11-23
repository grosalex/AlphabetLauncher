package com.grosalex.alphabetlauncher

import java.util.*

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelSectionMap : TreeMap<String, ArrayList<AppModel>>() {
    //  var appHash: = HashMap<String, ArrayList<AppModel>>()
    fun add(appModel: AppModel) {
        var key: String = appModel.appName.first().toString()
        if (!this.containsKey(key)) {
            this.put(key, ArrayList<AppModel>())
        }

        this.get(key)?.add(appModel)
    }
}