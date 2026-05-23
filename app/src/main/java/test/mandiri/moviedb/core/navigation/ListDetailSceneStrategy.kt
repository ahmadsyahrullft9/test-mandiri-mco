package test.mandiri.moviedb.core.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

class ListDetailSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND)) {
            val previewEntry = entries.lastOrNull()?.takeIf {
                it.metadata.containsKey(PREVIEW_KEY)
            }

            if (previewEntry != null) {
                val detailEntry = entries.find {
                    it.metadata.containsKey(DETAIL_KEY)
                } ?: return null

                val listEntry = entries.findLast {
                    it.metadata.containsKey(LIST_KEY)
                } ?: return null

                return ListDetailPreviewScene(
                    list = listEntry,
                    detail = detailEntry,
                    preview = previewEntry,
                    key = listEntry.contentKey,
                    previousEntries = entries.dropLast(1)
                )
            }

        }

        val detailEntry = entries.lastOrNull()?.takeIf {
            it.metadata.containsKey(DETAIL_KEY)
        } ?: return null

        val listEntry = entries.findLast {
            it.metadata.containsKey(LIST_KEY)
        } ?: return null

        if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return ListDetailScene(
                list = listEntry,
                detail = detailEntry,
                key = listEntry.contentKey,
                previousEntries = entries.dropLast(1)
            )
        }

        return null
    }

    companion object {
        val LIST_KEY = "list-ListDetailSceneStrategy"
        val DETAIL_KEY = "detail-ListDetailSceneStrategy"
        val PREVIEW_KEY = "preview-ListDetailSceneStrategy"

        fun listPane() = mapOf(LIST_KEY to true)
        fun detailPane() = mapOf(DETAIL_KEY to true)
        fun previewPane() = mapOf(PREVIEW_KEY to true)
    }
}

@Composable
fun <T : Any> rememberListDetailSceneStrategy(): ListDetailSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    return remember(windowSizeClass) {
        ListDetailSceneStrategy(windowSizeClass)
    }
}