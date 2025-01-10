package com.example.shoppingcartcompose.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
<<<<<<< HEAD
import com.example.shoppingcartcompose.model.CategoryModel
import com.example.shoppingcartcompose.model.ItemsModel
=======
>>>>>>> 25e250283faa4a1b42d23d5a2954e8550b12abf8
import com.example.shoppingcartcompose.model.SliderModel
import com.google.firebase.database.*

class MainViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
<<<<<<< HEAD
    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    private val _banners = MutableLiveData<List<SliderModel>>()
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()

    val banners: LiveData<List<SliderModel>> = _banners
    val categories: LiveData<MutableList<CategoryModel>> = _category
    val recommended: LiveData<MutableList<ItemsModel>> = _recommended
    fun loadFiltered(id:String) {
        val Ref = firebaseDatabase.getReference("Items")
        val query: Query = Ref.orderByChild("categoryId").equalTo(id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recommendedList = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val recommended = childSnapshot.getValue(ItemsModel::class.java)
                    if (recommended != null) {
                        recommendedList.add(recommended)
                    }
                }
                _recommended.value = recommendedList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun loadRecommended() {
        val Ref = firebaseDatabase.getReference("Items")
        val query: Query = Ref.orderByChild("showRecommended").equalTo(true)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recommendedList = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val recommended = childSnapshot.getValue(ItemsModel::class.java)
                    if (recommended != null) {
                        recommendedList.add(recommended)
                    }
                }
                _recommended.value = recommendedList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
=======
    private val _banners = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banners
>>>>>>> 25e250283faa4a1b42d23d5a2954e8550b12abf8

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

<<<<<<< HEAD
    fun loadCategory() {
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

=======
>>>>>>> 25e250283faa4a1b42d23d5a2954e8550b12abf8
    override fun onCleared() {
        super.onCleared()
        bannerListener?.let {
            firebaseDatabase.getReference("Banner").removeEventListener(it)
        }
    }
}
