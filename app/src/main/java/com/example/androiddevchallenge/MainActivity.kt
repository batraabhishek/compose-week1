/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.data.pupies.impl.BlockingFakePuppiesRepository
import com.example.androiddevchallenge.data.pupies.impl.puppies
import com.example.androiddevchallenge.data.successOr
import com.example.androiddevchallenge.model.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.utils.loadPicture
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MaterialTheme {
                    MyApp()
                }
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(navController = navController) }
        composable(
            "detail/{puppyId}",
            arguments = listOf(navArgument("puppyId") { type = NavType.StringType })
        ) { backStackEntry ->
            Details(
                backStackEntry.arguments?.getString("puppyId")
            )
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun PuppyDetails(puppy: Puppy) {
    val typography = MaterialTheme.typography

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val image =
            loadPicture(url = puppy.url, defaultImage = R.drawable.ic_launcher_foreground)
        image.component1()?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = puppy.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(16.dp, 4.dp),
            text = puppy.name,
            style = typography.h4
        )
        Text(
            modifier = Modifier.padding(16.dp, 4.dp),
            text = puppy.traits,
            style = typography.h6
        )
        Text(
            text = puppy.about,
            modifier = Modifier.padding(16.dp, 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@ExperimentalCoroutinesApi
@Composable
fun PuppyRow(navController: NavHostController, puppy: Puppy) {
    val typography = MaterialTheme.typography
    Card(Modifier.clickable { navController.navigate("detail/${puppy.id}") }) {
        Column {
            val image =
                loadPicture(url = puppy.url, defaultImage = R.drawable.ic_launcher_foreground)
            image.component1()?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = puppy.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(255.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(16.dp, 4.dp),
                text = puppy.name,
                style = typography.h6
            )
            Text(
                modifier = Modifier.padding(16.dp, 4.dp),
                text = puppy.traits,
                style = typography.body2
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun Details(string: String?) {
    val context = LocalContext.current
    val puppy: Puppy = BlockingFakePuppiesRepository(context)
        .getPuppy(string ?: "0")
        .successOr(puppies[0])

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = puppy.name)
                }
            )
        },
        content = {
            Surface(color = MaterialTheme.colors.background) {
                PuppyDetails(puppy = puppy)
            }
        }
    )
}

@Composable
fun Home(navController: NavHostController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = " Hope for Puppies")
                }
            )
        },
        content = {
            Surface(color = MaterialTheme.colors.background) {
                BlockingFakePuppiesRepository(context = context).getPuppies().successOr(
                    emptyList()
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BlockingFakePuppiesRepository(context = context)
                        .getPuppies()
                        .successOr(emptyList())
                        .forEach {
                            PuppyRow(navController = navController, puppy = it)
                        }
                }
            }
        }
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
