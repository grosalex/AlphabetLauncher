package com.grosalex.alphabeticallauncher.model

import android.content.pm.ShortcutInfo

data class Shortcut(val id: String?,
                    val packageName: String,
                    val label: String,
                    val shortcutInfo: ShortcutInfo?)