package com.mansao.myapplication.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mansao.myapplication.R
import com.mansao.myapplication.data.local.entity.NewsEntity
import com.mansao.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, NewsEntity::class.java)
        } else {
            intent.getParcelableExtra<NewsEntity>(EXTRA_DATA)
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = data?.title


        binding.apply {
            tvTitle.text = data?.title
            tvPublished.text = data?.publishedAt
            tvSource.text = data?.url
            tvDescription.text = data?.description
            Glide.with(this@DetailActivity)
                .load(data?.urlToImage)
                .placeholder(R.drawable.ic_broken_image_black)
                .into(ivNews)

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }


    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}