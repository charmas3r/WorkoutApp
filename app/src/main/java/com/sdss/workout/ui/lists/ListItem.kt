package com.sdss.workout.ui.lists

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.sdss.workout.R

@Composable
fun ListItem(
    @StringRes titleRes: Int = R.string.google_sync_sign_in_title,
    @StringRes descRes: Int? = null,
    hasSwitch: Boolean = false,
    icon: ImageVector? = null,
    hasArrow: Boolean = false,
    onSwitchClick: (isSwitch: Boolean) -> Unit = { },
    onListItemClick: () -> Unit = { }
) {

    val checkedState = remember { mutableStateOf(true) }

    when {
        (hasSwitch) -> {
            require(icon == null)
        }
        (icon == null) -> {
            require(!hasSwitch)
        }
    }

    Column(
        modifier = Modifier
            .padding(bottom = if (descRes != null) 4.dp else 0.dp)
            .height(56.dp)
            .clickable { onListItemClick.invoke() },
        verticalArrangement = Arrangement.Center
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = titleRes),
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(start = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = if (hasArrow) 4.dp else 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                when {
                    (hasSwitch) -> {
                        Switch(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                onSwitchClick.invoke(it)
                            })
                    }
                    (icon != null) -> {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (hasArrow) MaterialTheme.colors.onBackground else LocalContentColor.current.copy(
                                alpha = LocalContentAlpha.current
                            )
                        )
                    }
                }
            }

        }
        if (descRes != null) {
            Text(
                text = stringResource(id = descRes),
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun SetupFinishPreview() {
    MaterialTheme {
        ListItem(hasSwitch = true)
    }
}