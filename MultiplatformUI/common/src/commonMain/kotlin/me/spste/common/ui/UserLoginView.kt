package me.spste.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.AnnotatedString
import me.spste.common.model.Login

@Composable
fun UserLoginView(login: Login?, modifier: Modifier) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End, modifier = modifier) {
        if (login != null) {
            Text("User: ${login.username}")
        }else{
            SignInButton(
                modifier = Modifier.wrapContentSize(),
                onClick = {

                }
            )
        }
        Column() {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = login?.username ?: "Guest",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
            if (login != null) {
                ClickableText(
                    text = AnnotatedString("Log out"),
                    onClick = {

                    }
                )
            }

        }
    }
}

