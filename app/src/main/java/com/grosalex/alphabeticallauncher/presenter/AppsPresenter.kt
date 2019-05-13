package com.grosalex.alphabeticallauncher.presenter

import com.grosalex.alphabeticallauncher.contract.AppsContract
import com.grosalex.alphabeticallauncher.model.AppModelSectionMap

/**
 * @author abruneau
 *         Created on 06/05/2019
 */
class AppsPresenter(val appView:AppsContract.View, val appsIntractor:AppsContract.Intractor):AppsContract.Presenter, AppsContract.Intractor.OnAppsLoaded {
    override fun addApp(data: String?) {
        appsIntractor.addApp()
    }

    override fun onLoad(appModelSectionMap: AppModelSectionMap) {
        appView.updateAll(appModelSectionMap)
    }

    override fun onError() {
    }

    override fun loadApp() {
        appView.refresh()
        appsIntractor.loadApps(this)
    }
}