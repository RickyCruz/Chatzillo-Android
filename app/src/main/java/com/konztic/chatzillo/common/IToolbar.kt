package com.konztic.chatzillo.common

import androidx.appcompat.widget.Toolbar

interface IToolbar {
    fun toolbarToLoad(toolbar: Toolbar?)
    fun enableHomeDisplay(display: Boolean)
}