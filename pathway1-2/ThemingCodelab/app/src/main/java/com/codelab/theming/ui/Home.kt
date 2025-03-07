/*
 * Copyright 2020 The Android Open Source Project
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

package com.codelab.theming.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.theming.R
import com.codelab.theming.data.Post
import com.codelab.theming.data.PostRepo
import com.codelab.theming.ui.theme.JetnewsTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val featured = remember { PostRepo.getFeaturedPost() }
    val posts = remember { PostRepo.getPosts() }
    MaterialTheme {
        Scaffold(
            topBar = { AppBar() }
        ) { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                item {
                    Header(stringResource(R.string.top))
                }
                item {
                    FeaturedPost(
                        post = featured,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Header(stringResource(R.string.popular))
                }
                items(posts) { post ->
                    PostItem(post = post)
                    Divider(modifier = Modifier.padding(start = 72.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Palette,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        },
        title = {
            Text(text = stringResource(R.string.app_title))
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = modifier.semantics { heading() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun FeaturedPost(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* onClick */ }
        ) {
            Image(
                painter = painterResource(post.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 180.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            val padding = Modifier.padding(horizontal = 16.dp)
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = padding
            )
            Text(
                text = post.metadata.author.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = padding
            )
            PostMetadata(post, padding)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PostMetadata(
    post: Post,
    modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val tagDivider = "  "
    val text = buildAnnotatedString {
        append(post.metadata.date)
        append(divider)
        append(stringResource(R.string.read_time, post.metadata.readTimeMinutes))
        append(divider)
        val tagStyle = MaterialTheme.typography.labelSmall.toSpanStyle().copy(
            background = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        post.tags.forEachIndexed { index, tag ->
            if (index != 0) {
                append(tagDivider)
            }
            withStyle(tagStyle) {
                append(" ${tag.uppercase(Locale.getDefault())} ")
            }
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineText = {
            Text(text = post.title)
        },
        supportingText = {
            PostMetadata(post)
        },
        leadingContent = {
            Image(
                painter = painterResource(post.imageThumbId),
                contentDescription = null,
                modifier = Modifier.clip(shape = MaterialTheme.shapes.small)
            )
        },
        modifier = modifier
            .clickable { /* todo */ }
            .padding(vertical = 8.dp),
    )
}

@Preview("Post Item")
@Composable
private fun PostItemPreview() {
    val post = remember { PostRepo.getFeaturedPost() }
    JetnewsTheme {
        Surface {
            PostItem(post = post)
        }
    }
}

@Preview("Featured Post")
@Composable
private fun FeaturedPostPreview() {
    val post = remember { PostRepo.getFeaturedPost() }
    JetnewsTheme {
        FeaturedPost(post = post)
    }
}

@Preview("Home")
@Composable
private fun HomePreview() {
    Home()
}
