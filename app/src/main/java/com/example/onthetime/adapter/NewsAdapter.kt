package com.example.onthetime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.model.News
import com.example.onthetime.repository.EmployerRepository
import com.example.onthetime.ui.fragments.DashboardFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class NewsAdapter(var news: List<News>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    val repository = EmployerRepository()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var newsName = view.findViewById<TextView>(R.id.news_name_textview)
        var newsAuthor = view.findViewById<TextView>(R.id.author_name_textview)
        var newsCreatedTime = view.findViewById<TextView>(R.id.time_textview)
        var newsCreatedDate = view.findViewById<TextView>(R.id.date_textview)
        var profilPhoto = view.findViewById<ImageView>(R.id.profile_photo_news)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = news.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val post = news[position]
        val time = post.time?.hours.toString() + ":" + post.time?.minutes.toString()
        val date = post.date?.day.toString() + "/" + post.date?.month.toString() + "/" + post.date?.year.toString()

        holder.newsName.text = post.message
        holder.newsAuthor.text = post.authornameSurname
        holder.newsCreatedTime.text = time
        holder.newsCreatedDate.text = date

//        holder.profilPhoto.setImageDrawable(null)

        val authorUid = post.owner



        if (!authorUid.isNullOrEmpty()) {
            repository.getEmployerPhotoPath(authorUid) { profilePhotoPath ->
                if (!profilePhotoPath.isNullOrEmpty()) {
                    Picasso.get()
                        .load(profilePhotoPath)
                        .placeholder(R.drawable.none_profile_photo)
                        .error(R.drawable.none_profile_photo)
                        .into(holder.profilPhoto, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                // Sekil  yüklendi
                            }

                            override fun onError(e: Exception?) {
                                println("Photo yüklenerken bir xeta oldu: ${e?.message}")
                            }
                } )}
                else {
                    Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/onthetime-53976.appspot.com/o/images%2F8387143f-eca9-4c6b-939f-b47c7b4ef34e.jpg?alt=media&token=67260b6b-3c7d-40a7-b686-e0d8088a99f2")
                        .into(holder.profilPhoto)
                }
            }
        } else {
            Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/onthetime-53976.appspot.com/o/images%2F8387143f-eca9-4c6b-939f-b47c7b4ef34e.jpg?alt=media&token=67260b6b-3c7d-40a7-b686-e0d8088a99f2")
                .into(holder.profilPhoto)

        }
    }
}
