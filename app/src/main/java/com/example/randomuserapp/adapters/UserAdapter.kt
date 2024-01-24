package com.example.randomuserapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuserapp.R
import com.example.randomuserapp.databinding.UserItemBinding
import com.example.randomuserapp.models.User

class UserAdapter(private val context: Context, private val clickListener: ClickListener) :
    ListAdapter<User, UserAdapter.ViewHolder>(
        CountryDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, getItem(position), clickListener)
    }

    class ViewHolder private constructor(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context, item: User, clickListener: ClickListener
        ) {
            Glide.with(context).load(item.picture.thumbnail).into(binding.profileImageUserItem)
            binding.nameUserItem.text =
                context.getString(R.string.user_full_name, item.name.first, item.name.last)
            binding.emailUserItem.text = item.email
            binding.containerUserItem.setOnClickListener {
                clickListener.onClick(adapterPosition)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

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

