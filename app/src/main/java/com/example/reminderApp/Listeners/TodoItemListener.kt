package com.example.reminderApp.Listeners

import android.view.View

class TodoItemListener(val listener: (View, Int) -> Unit) {
    fun onClickItem(v: View, pos: Int?) = listener(v, pos!!)
}
