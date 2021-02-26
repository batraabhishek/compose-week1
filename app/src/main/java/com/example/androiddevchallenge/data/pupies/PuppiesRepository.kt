package com.example.androiddevchallenge.data.pupies

import com.example.androiddevchallenge.model.Puppy
import com.example.androiddevchallenge.data.Result

interface PuppiesRepository {

    fun getPuppy(puppyId: String): Result<Puppy>
    fun getPuppies(): Result<List<Puppy>>
}