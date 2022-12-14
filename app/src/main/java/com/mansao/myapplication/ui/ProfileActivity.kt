package com.mansao.myapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.mansao.myapplication.R
import com.mansao.myapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.profile)



        val user = auth.currentUser
        binding.tvName.text = StringBuilder(getString(R.string.email)).append( ": ${user?.email}")
//        Glide.with(this)
//            .load(resources.getString(R.string.profile_image))
//            .centerCrop()
//            .into(binding.imgProfile)
//
//        linkToGithub()
    }

    private fun linkToGithub() {
        val githubLink : Link = Link(resources.getString(R.string.my_username))
            .setUnderlined(true)
            .setBold(true)
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(resources.getString(R.string.github_link))
                startActivity(intent)
            }

//        binding.tvUsername.applyLinks(githubLink)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}