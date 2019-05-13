package com.grosalex.alphabeticallauncher.contract

import com.grosalex.alphabeticallauncher.model.AppModelSectionMap

/**
 * @author abruneau
 *         Created on 06/05/2019
 */
interface AppsContract {

    interface View{
        fun updateAll(appModelSectionMap: AppModelSectionMap)
        fun refresh()

    }

    interface  Presenter{
        fun loadApp()
        fun addApp(data: String?)
    }

    interface Intractor{
        interface OnAppsLoaded{
            fun onLoad(appModelSectionMap: AppModelSectionMap)
            fun onError()
        }

        suspend fun loadApps(onAppsLoaded: OnAppsLoaded)
        fun addApp(data: String?, onAppsLoaded: OnAppsLoaded)
    }
}