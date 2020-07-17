package com.konztic.chatzillo.common

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class ToolbarActivity : AppCompatActivity(), IToolbar {
    protected var toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?) {
        this.toolbar = toolbar
        this.toolbar?.let{ setSupportActionBar(this.toolbar) }
    }

    override fun enableHomeDisplay(display: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(display)
    }
}