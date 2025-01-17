package com.sdss.workout.setup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.sdss.workout.R
import com.sdss.workout.ui.buttons.PrimaryButton
import com.sdss.workout.ui.buttons.SecondaryButton

@Composable
fun SetupScreenLayout(
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onBackClick: () -> Unit,
    @StringRes primaryButtonRes: Int = R.string.btn_continue,
    @StringRes secondaryButtonRes: Int = R.string.btn_skip,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = if (MaterialTheme.colors.isLight) {
                                painterResource(id = R.drawable.ic_baseline_arrow_back_24)
                            } else {
                                painterResource(id = R.drawable.ic_baseline_arrow_back_dm_24)
                            },
                            contentDescription = null
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.setup_screen_top_bar_title)) },
            )
        }
    ) {
        SetupScreenLayoutTemplate(
            content,
            primaryButtonRes,
            onPrimaryClick,
            secondaryButtonRes,
            onSecondaryClick
        )
    }
}

@Composable
fun SetupScreenLayoutTemplate(
    content: @Composable() (BoxScope.() -> Unit),
    primaryButtonRes: Int,
    onPrimaryClick: () -> Unit,
    secondaryButtonRes: Int,
    onSecondaryClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            content = content, modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        )
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Column {
                PrimaryButton(
                    text = stringResource(id = primaryButtonRes),
                    modifier = Modifier
                        .fillMaxWidth(.75f)
                        .align(Alignment.CenterHorizontally),
                    onClick = onPrimaryClick

                )
                SecondaryButton(
                    text = stringResource(id = secondaryButtonRes),
                    modifier = Modifier
                        .fillMaxWidth(.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp, bottom = 16.dp),
                    onClick = onSecondaryClick
                )
            }
        }
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
    }
}