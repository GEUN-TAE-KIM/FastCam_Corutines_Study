package org.cream.corutines_practice_red.main.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cream.corutines_practice_red.R
import org.cream.corutines_practice_red.databinding.ImageSearchItemBinding
import org.cream.corutines_practice_red.main.model.Item

class ImageSearchViewHolder(
    private val like: (Item) -> Unit,
    private val binding: ImageSearchItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item?) {
        item?.let { item ->
            Glide.with(binding.root)
                .load(item.thumbnail)
                .centerCrop()
                .into(binding.imageView)
            binding.imageView.setOnClickListener{
                like.invoke(item) // 외부에 있는 이미지 호출
            }
        }
    }

    companion object {
        fun create(
            like: (Item) -> Unit,
            parent: ViewGroup
        ): ImageSearchViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_search_item, parent, false)
            val binding = ImageSearchItemBinding.bind(view)
            return ImageSearchViewHolder(like, binding)
        }
    }
}