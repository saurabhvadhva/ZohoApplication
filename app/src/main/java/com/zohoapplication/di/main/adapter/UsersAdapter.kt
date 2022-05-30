package com.zohoapplication.di.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zohoapplication.R
import com.zohoapplication.data.model.UserItem
import kotlinx.android.synthetic.main.item_list_users.view.*
import java.util.*

class UsersAdapter(
    val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var mList: ArrayList<UserItem?> = ArrayList()
    private var mOriginalList: ArrayList<UserItem?> = ArrayList()

    val ITEM_VIEW = 1
    val ITEM_NO_DATA_VIEW = 2

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItem: UserItem?, onClickListener: View.OnClickListener) {
            itemView.tag = userItem
            userItem?.nameItem?.let {
                val name = StringBuilder()
                it.title.let {
                    name.append(it).append(" ")
                }
                it.first.let {
                    name.append(it).append(" ")
                }
                it.last.let {
                    name.append(it).append(" ")
                }
                itemView.txt_name.text = name.toString().trim().ifEmpty { "-" }
            }
            itemView.txt_email.text = userItem?.email ?: "-"
            Glide.with(itemView.img_user.context)
                .load(userItem?.pictureItem?.thumbnail ?: "")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(itemView.img_user)
            itemView.setOnClickListener {
                onClickListener.onClick(itemView)
            }
        }
    }

    class NoDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {}
    }


    override fun getItemViewType(position: Int): Int {
        return if (mList[position] != null)
            ITEM_VIEW
        else
            ITEM_NO_DATA_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ITEM_VIEW) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_list_users, parent,
                    false
                )
            )
        } else {
            NoDataViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_no_data_found, parent,
                    false
                )
            )
        }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder is ViewHolder) {
            holder.bind(mList[position], onClickListener)
        } else {
            (holder as NoDataViewHolder).bind()
        }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<UserItem>) {
        mList.clear()
        mOriginalList.clear()
        mList = ArrayList(list)
        mOriginalList = ArrayList(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<UserItem>) {
        mList.addAll(list)
        mOriginalList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<UserItem?> = ArrayList()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(mOriginalList)
                } else {
                    val filterPattern: String =
                        constraint.toString().lowercase(Locale.getDefault()).trim()
                    for (item in mOriginalList) {
                        val name = StringBuilder()
                        item?.nameItem?.title?.let {
                            name.append(it.lowercase(Locale.getDefault())).append(" ")
                        }
                        item?.nameItem?.first?.let {
                            name.append(it.lowercase(Locale.getDefault())).append(" ")
                        }
                        item?.nameItem?.last?.let {
                            name.append(it.lowercase(Locale.getDefault())).append(" ")
                        }
                        val email = item?.email?.lowercase(Locale.getDefault())
                        if (name.toString().trim().contains(filterPattern)
                            || email?.contains(filterPattern) == true
                        ) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mList.clear()
                mList.addAll(results?.values as List<UserItem>)
                if (mList.isEmpty())
                    mList.add(null)
                notifyDataSetChanged()
            }
        }
    }
}