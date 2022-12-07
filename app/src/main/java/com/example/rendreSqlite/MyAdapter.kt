package com.example.rendreSqlite

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MyAdapter (private val reqL :  ActivityResultLauncher<Intent>,private val context: Context, private var mUsers: MutableList<UserModel>, private val mRowLayout: Int) : RecyclerView.Adapter<MyAdapter.UserViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mRowLayout, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = mUsers[position]
        holder.nom_prenom.text = user.nom_prenom
        holder.email.text = user.adresse_email
        holder.date_naissance.text = user.date_naissance
        holder.login.text = user.login
        holder.tel.text = user.tel.toString()

        holder.delete.setOnClickListener {
            showDeleteDialog(user,holder.itemView)
        }
        holder.edit.setOnClickListener {
            editUser(user)
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nom_prenom: TextView = itemView.findViewById(R.id.nomprenomF)
        var email: TextView = itemView.findViewById(R.id.emailF)
        var date_naissance: TextView = itemView.findViewById(R.id.date_naissanceF)
        var delete: Button = itemView.findViewById(R.id.delete)
        var login: TextView = itemView.findViewById(R.id.loginF)
        var tel: TextView = itemView.findViewById(R.id.telF)
        var edit: Button = itemView.findViewById(R.id.edit)

    }

    fun showDeleteDialog(user: UserModel,view:View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete user")
        builder.setMessage("Are you sure you want to delete this user?")
        builder.setPositiveButton("Yes") { dialog, which ->
            deleteUser(user,view)
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun deleteUser(user: UserModel,view:View) {
        println("user id: ${user.user_id}")
        val db = UserDbHelper(context)
        db.deleteUser(user.user_id)
        mUsers.remove(user)
        notifyDataSetChanged()
        Snackbar.make(view, "User deleted successfully", Snackbar.LENGTH_LONG).show()
    }

    fun refreshUsers() {
        val db = UserDbHelper(context)
        val users = db.readUsers()
        mUsers.clear()
        mUsers.addAll(users)
        notifyDataSetChanged()
    }

    fun editUser(user: UserModel){
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("user_id", user.user_id)
        reqL.launch(intent)
    }


}