package com.example.shoppingcartcompose.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingcartcompose.model.SliderModel
import com.google.firebase.database.*

class MainViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _banners = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banners

    private var bannerListener: ValueEventListener? = null

    fun loadBanners() {
        val bannerReference = firebaseDatabase.getReference("Banner")

        bannerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val banner = childSnapshot.getValue(SliderModel::class.java)
                    banner?.let { bannerList.add(it) }
                }
                _banners.value = bannerList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Error loading banners: ${error.message}")
            }
        }

        bannerReference.addValueEventListener(bannerListener!!)
    }

    override fun onCleared() {
        super.onCleared()
        bannerListener?.let {
            firebaseDatabase.getReference("Banner").removeEventListener(it)
        }
    }
}
