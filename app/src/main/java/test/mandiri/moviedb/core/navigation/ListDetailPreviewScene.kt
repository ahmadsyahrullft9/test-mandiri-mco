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

class ListDetailPreviewScene<T: Any>(
    private val list: NavEntry<T>,
    private val detail: NavEntry<T>,
    private val preview: NavEntry<T>,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {

    override val entries: List<NavEntry<T>>
        get() = listOf(list, detail, preview)

    override val content: @Composable (() -> Unit) = {
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.width(220.dp)) {
                list.Content()
            }
            Column(Modifier.weight(1f)) {
                detail.Content()
            }
            Column(Modifier.width(411.dp)) {
                preview.Content()
            }
        }
    }
}