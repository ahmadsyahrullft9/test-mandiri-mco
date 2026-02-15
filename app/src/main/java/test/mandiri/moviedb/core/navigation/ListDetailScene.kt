package test.mandiri.moviedb.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene

class ListDetailScene<T: Any>(
    private val list: NavEntry<T>,
    private val detail: NavEntry<T>,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {

    override val entries: List<NavEntry<T>>
        get() = listOf(list, detail)

    override val content: @Composable (() -> Unit) = {
        /*Row(Modifier.fillMaxSize()) {
            Column(Modifier.weight(4f)) {
                list.Content()
            }
            Column(Modifier.weight(6f)) {
                detail.Content()
            }
        }*/
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.width(220.dp)) {
                list.Content()
            }
            Column(Modifier.weight(1f)) {
                detail.Content()
            }
        }
    }
}