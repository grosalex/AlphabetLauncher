package com.grosalex.alphabetlauncher

import java.util.*

/**
 * Created by grosalex on 23/11/2017.
 */
class AppModelSectionMap : TreeMap<String, ArrayList<AppModel>>() {
    //  var appHash: = HashMap<String, ArrayList<AppModel>>()
    fun add(appModel: AppModel) {
        val key: String = appModel.appName.first().toUpperCase().toString()
        if (!this.containsKey(key)) {
            this[key] = ArrayList<AppModel>()
        } else if(this[key]?.contains(appModel) == true){
            return
        }

        this[key]?.add(appModel)
    }
}