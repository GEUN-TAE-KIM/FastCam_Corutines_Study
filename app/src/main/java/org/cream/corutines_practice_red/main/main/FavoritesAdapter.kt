package org.cream.corutines_practice_red.main.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.cream.corutines_practice_red.main.model.Item


class FavoritesAdapter: RecyclerView.Adapter<ImageSearchViewHolder>() {

    private var items : List<Item> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Item>) {
        this.items = items
    }
}