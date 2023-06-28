package com.noritz.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.noritz.whatsup.R
import com.noritz.whatsup.databinding.ChatListItemBinding
import com.noritz.whatsup.GlideApp
import com.noritz.whatsup.MyAppGlideModule



class UserAdapter(var context : Context, var userList : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    inner class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding : ChatListItemBinding = ChatListItemBinding.bind(itemView) // change to different variable name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false)
        return UserViewHolder(v)
    }

    override fun getItemCount(): Int = userList.size
    /*override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.username.text = user.username // check if the names are all correct
        //GlideApp.with(context).load(user.image).placeholder(R.drawable.pp).into(holder.binding.profilePic)
    }*/

   /* override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.username.text = user.username // check if the names are all correct
        Glide.with(context).load(user.image).placeholder(R.drawable.pp).into(holder.binding.profilePic)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, MyChats::class.java)
            intent.putExtra("name", user.username) // a voir
            intent.putExtra("image", user.image)
            intent.putExtra("uid", user.id)
            context.startActivity(intent)
        }
    }*/
  override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val user = userList[position]
       holder.binding.username.text = user.username
       /*GlideApp.with(context)
           .load(user.image)
           .placeholder(R.drawable.pp)
           .into(holder.binding.profilePic)*/
       holder.itemView.setOnClickListener {
           val intent = Intent(context, MyChats::class.java)
           intent.putExtra("name", user.username)
           /*intent.putExtra("image", user.image)*/
           intent.putExtra("uid", user.id)
           context.startActivity(intent)
       }
   }


}

