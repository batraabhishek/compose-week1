package com.example.androiddevchallenge.data.pupies.impl

import android.content.Context
import com.example.androiddevchallenge.data.Result
import com.example.androiddevchallenge.data.pupies.PuppiesRepository
import com.example.androiddevchallenge.model.Puppy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlockingFakePuppiesRepository(private val context: Context) : PuppiesRepository {
    override fun getPuppy(puppyId: String): Result<Puppy> {

        val puppy = puppies.find { it.id == puppyId }
        return if (puppy == null) {
            Result.Error(IllegalArgumentException("Unable to find cute puppy"))
        } else {
            Result.Success(puppy)
        }

    }

    override fun getPuppies(): Result<List<Puppy>> {
        return Result.Success(puppies)
    }
}