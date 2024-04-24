package com.example.randomuserapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuserapp.R
import com.example.randomuserapp.databinding.ItemRowProgressBinding
import com.example.randomuserapp.databinding.UserItemBinding
import com.example.randomuserapp.models.User

class UserAdapter(private val context: Context, private val clickListener: ClickListener) :
    PagingDataAdapter<User, RecyclerView.ViewHolder>(
        CountryDiffCallback()
    ) {
    var isLoading = false
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == itemCount - 1) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            UserViewHolder.from(parent)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRowProgressBinding.inflate(layoutInflater, parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            val user = getItem(position)
            if (user != null) {
                holder.bind(context, user, clickListener)
            }
        }
    }

    class UserViewHolder private constructor(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context, item: User, clickListener: ClickListener
        ) {
            Glide.with(context).load(item.picture.thumbnail).into(binding.profileImageUserItem)
            binding.nameUserItem.text =
                context.getString(R.string.user_full_name, item.name.first, item.name.last)
            binding.emailUserItem.text = item.email
            binding.containerUserItem.setOnClickListener {
                clickListener.onClick(absoluteAdapterPosition)
            }
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }

    class LoadingViewHolder(private val binding: ItemRowProgressBinding) :
        RecyclerView.ViewHolder(binding.root)

    class CountryDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    fun getUserPosition(position: Int): User? {
        return getItem(position)
    }
}

