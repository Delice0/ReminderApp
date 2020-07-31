package com.example.reminderApp.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * All current and upcoming ViewHolders MUST extend from this abstract AKA "base" class
 */
abstract class BaseViewHolder<T, L> internal constructor(view: View): RecyclerView.ViewHolder(view) {
    // Bind views to the item and *OPTIONAL* implement listener for items
    abstract fun bind(item: T, listener: L?)
}