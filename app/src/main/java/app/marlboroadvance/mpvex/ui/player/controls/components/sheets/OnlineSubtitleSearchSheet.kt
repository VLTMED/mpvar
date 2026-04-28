package app.marlboroadvance.mpvex.ui.player.controls.components.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.marlboroadvance.mpvex.repository.wyzie.WyzieEpisode
import app.marlboroadvance.mpvex.repository.wyzie.WyzieSeason
import app.marlboroadvance.mpvex.repository.wyzie.WyzieSubtitle
import app.marlboroadvance.mpvex.repository.wyzie.WyzieTmdbResult
import app.marlboroadvance.mpvex.repository.wyzie.WyzieTvShowDetails
import app.marlboroadvance.mpvex.ui.theme.spacing
import app.marlboroadvance.mpvex.utils.media.MediaInfoParser
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// ─── Keep original sealed class for compatibility ───────────────────────────
sealed class OnlineSubtitleItem {
    data class OnlineTrack(val subtitle: WyzieSubtitle) : OnlineSubtitleItem()
    data class Header(val title: String) : OnlineSubtitleItem()
    object Divider : OnlineSubtitleItem()
}

// ─── Accent helpers — derived from MaterialTheme at runtime ──────────────────
private val MaterialTheme.subAccent        @Composable get() = colorScheme.primary
private val MaterialTheme.subAccentDim     @Composable get() = colorScheme.onPrimaryContainer
private val MaterialTheme.subAccentContainer @Composable get() = colorScheme.primary.copy(alpha = 0.08f)
private val MaterialTheme.subAccentBorder  @Composable get() = colorScheme.primary.copy(alpha = 0.40f)

// ─── Main composable ─────────────────────────────────────────────────────────
@Composable
fun OnlineSubtitleSearchSheet(
    onDismissRequest: () -> Unit,
    onDownloadOnline: (WyzieSubtitle) -> Unit,
    isSearching: Boolean = false,
    isDownloading: Boolean = false,
    searchResults: ImmutableList<WyzieSubtitle> = emptyList<WyzieSubtitle>().toImmutableList(),
    isOnlineSectionExpanded: Boolean = true,
    onToggleOnlineSection: () -> Unit = {},
    modifier: Modifier = Modifier,
    mediaTitle: String = "",
    mediaSearchResults: ImmutableList<WyzieTmdbResult> = emptyList<WyzieTmdbResult>().toImmutableList(),
    isSearchingMedia: Boolean = false,
    onSearchMedia: (String) -> Unit = {},
    onSelectMedia: (WyzieTmdbResult) -> Unit = {},
    selectedTvShow: WyzieTvShowDetails? = null,
    isFetchingTvDetails: Boolean = false,
    selectedSeason: WyzieSeason? = null,
    onSelectSeason: (WyzieSeason) -> Unit = {},
    seasonEpisodes: ImmutableList<WyzieEpisode> = emptyList<WyzieEpisode>().toImmutableList(),
    isFetchingEpisodes: Boolean = false,
    selectedEpisode: WyzieEpisode? = null,
    onSelectEpisode: (WyzieEpisode) -> Unit = {},
    onClearMediaSelection: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val mediaInfo = remember(mediaTitle) { MediaInfoParser.parse(mediaTitle) }
    var searchQuery by remember { mutableStateOf(mediaInfo.title) }

    LaunchedEffect(mediaInfo.title) {
        if (mediaInfo.title.isNotBlank()) onSearchMedia(mediaInfo.title)
    }

    val showSuggestions = mediaSearchResults.isNotEmpty() && selectedTvShow == null
    val showTvControls = selectedTvShow != null
    val showResults = searchResults.isNotEmpty() && !isSearching

    val items = remember(searchResults) {
        searchResults.map { OnlineSubtitleItem.OnlineTrack(it) as OnlineSubtitleItem }.toImmutableList()
    }

    GenericTracksSheet(
        tracks = items,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        header = {
            Column(modifier = Modifier.fillMaxWidth()) {

                // ── Search bar ──────────────────────────────────────────
                SubSearchField(
                    query = searchQuery,
                    onQueryChange = { v ->
                        searchQuery = v
                        if (v.length >= 2) onSearchMedia(v)
                        else if (v.isEmpty()) onClearMediaSelection()
                    },
                    onSearch = {
                        onSearchMedia(searchQuery)
                        keyboardController?.hide()
                    },
                    onClear = {
                        searchQuery = ""
                        onClearMediaSelection()
                    },
                    isLoading = isSearchingMedia || isSearching || isDownloading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(top = MaterialTheme.spacing.medium, bottom = 6.dp)
                )

                // ── Autocomplete suggestions ────────────────────────────
                AnimatedVisibility(
                    visible = showSuggestions,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    SuggestionDropdown(
                        results = mediaSearchResults,
                        onSelect = { result ->
                            searchQuery = result.title
                            onSelectMedia(result)
                            keyboardController?.hide()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(bottom = 6.dp)
                    )
                }

                // ── TV show controls ─────────────────────────────────────
                AnimatedVisibility(
                    visible = showTvControls,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    if (selectedTvShow != null) {
                        TvShowControlRow(
                            tvShow = selectedTvShow,
                            isFetchingDetails = isFetchingTvDetails,
                            selectedSeason = selectedSeason,
                            onSelectSeason = onSelectSeason,
                            isFetchingEpisodes = isFetchingEpisodes,
                            episodes = seasonEpisodes,
                            selectedEpisode = selectedEpisode,
                            onSelectEpisode = onSelectEpisode,
                            onBack = onClearMediaSelection,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .padding(bottom = 6.dp)
                        )
                    }
                }

                // ── Searching indicator ─────────────────────────────────
                AnimatedVisibility(visible = isSearching || isFetchingTvDetails) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .height(2.dp),
                        color = MaterialTheme.subAccent,
                        trackColor = MaterialTheme.subAccent.copy(alpha = 0.15f)
                    )
                }

                // ── Results header ──────────────────────────────────────
                if (showResults) {
                    val ctx = if (selectedTvShow != null && selectedSeason != null && selectedEpisode != null)
                        "${selectedTvShow.name} · S${selectedSeason.season_number} E${selectedEpisode.episode_number}"
                    else searchQuery
                    ResultsHeader(
                        count = searchResults.size,
                        context = ctx,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(top = 8.dp, bottom = 4.dp)
                    )
                }
            }
        },
        track = { item ->
            when (item) {
                is OnlineSubtitleItem.OnlineTrack ->
                    SubtitleResultCard(
                        subtitle = item.subtitle,
                        onDownload = { onDownloadOnline(item.subtitle) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium, vertical = 3.dp)
                    )
                is OnlineSubtitleItem.Header -> {}
                OnlineSubtitleItem.Divider -> {}
            }
        }
    )
}

// ─── Search field (logical LTR order, RTL reverses automatically) ──────────
@Composable
private fun SubSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    var focused by remember { mutableStateOf(false) }
    val borderColor = if (focused) MaterialTheme.subAccent else colors.outlineVariant.copy(alpha = 0.5f)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(0.5.dp, borderColor, RoundedCornerShape(14.dp))
            .background(colors.surfaceContainerLow)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ① Trailing icon (clear / spinner) – leftmost in LTR, rightmost in RTL
        Box(Modifier.size(18.dp), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 1.8.dp,
                    color = MaterialTheme.subAccent
                )
            } else if (query.isNotEmpty()) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onClear() }
                )
            }
        }

        // ② Text input – fills remaining space
        BasicTextField_RTL(
            value = query,
            onValueChange = onQueryChange,
            onSearch = onSearch,
            onFocusChange = { focused = it },
            placeholder = "ابحث عن فيلم أو مسلسل...",
            modifier = Modifier.weight(1f)
        )

        // ③ Leading icon (search) – rightmost in LTR, leftmost in RTL
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun BasicTextField_RTL(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFocusChange: (Boolean) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { onFocusChange(it.isFocused) },
        textStyle = TextStyle(
            color = colors.onSurface,
            fontSize = 14.sp,
            textAlign = TextAlign.Right
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.CenterEnd) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 14.sp,
                        color = colors.onSurfaceVariant.copy(alpha = 0.4f),
                        textAlign = TextAlign.Right
                    )
                }
                inner()
            }
        }
    )
}

// ─── Autocomplete dropdown ───────────────────────────────────────────────────
@Composable
private fun SuggestionDropdown(
    results: ImmutableList<WyzieTmdbResult>,
    onSelect: (WyzieTmdbResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column {
            results.forEachIndexed { index, result ->
                SuggestionRow(result = result, onClick = { onSelect(result) })
                if (index < results.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionRow(result: WyzieTmdbResult, onClick: () -> Unit) {
    val isTV = result.mediaType == "tv"
    // LTR logical order: Info | Poster | Chevron → RTL mirrors to Chevron|Poster|Info
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Info (weight 1f)
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = result.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Right
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val (badgeBg, badgeFg) = if (isTV)
                    Color(0x1A534AB7) to Color(0xFF534AB7)
                else
                    Color(0x1A185FA5) to Color(0xFF185FA5)

                Text(
                    text = if (isTV) "مسلسل" else "فيلم",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeFg,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(badgeBg)
                        .border(0.5.dp, badgeFg.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
                if (result.releaseYear != null) {
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Text(
                        text = result.releaseYear,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            if (!result.overview.isNullOrBlank()) {
                Text(
                    text = result.overview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Right
                )
            }
        }

        // Poster
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (result.poster != null) {
                AsyncImage(
                    model = result.poster,
                    contentDescription = result.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Default.Movie,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Chevron
        Icon(
            Icons.Default.ChevronLeft,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.size(14.dp)
        )
    }
}

// ─── TV Show controls ────────────────────────────────────────────────────────
@Composable
private fun TvShowControlRow(
    tvShow: WyzieTvShowDetails,
    isFetchingDetails: Boolean,
    selectedSeason: WyzieSeason?,
    onSelectSeason: (WyzieSeason) -> Unit,
    isFetchingEpisodes: Boolean,
    episodes: ImmutableList<WyzieEpisode>,
    selectedEpisode: WyzieEpisode?,
    onSelectEpisode: (WyzieEpisode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, colors.outlineVariant.copy(alpha = 0.45f)
        )
    ) {
        Column {
            // Header row: title – button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    text = tvShow.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.surfaceContainerHigh)
                        .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            HorizontalDivider(
                color = colors.outlineVariant.copy(alpha = 0.35f),
                thickness = 0.5.dp
            )

            if (isFetchingDetails) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                    color = MaterialTheme.subAccent,
                    trackColor = MaterialTheme.subAccent.copy(alpha = 0.1f)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Hint text (flex spacer, left in RTL = trailing)
                    val hintText = when {
                        selectedSeason == null -> "اختر الموسم أولاً"
                        selectedEpisode == null -> "اختر الحلقة الآن"
                        else -> "S${selectedSeason.season_number} · Ep ${selectedEpisode.episode_number}"
                    }
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selectedEpisode != null) MaterialTheme.subAccentDim
                                else colors.onSurfaceVariant.copy(alpha = 0.5f),
                        fontWeight = if (selectedEpisode != null) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Episode pill
                    SubDropdownPill(
                        labelMain = selectedEpisode?.let { "Ep ${it.episode_number}" } ?: "الحلقة",
                        labelSub = selectedEpisode?.let { "الحلقة ${it.episode_number}" } ?: "اختر",
                        enabled = selectedSeason != null && !isFetchingEpisodes,
                        loading = isFetchingEpisodes,
                        isFilled = selectedEpisode != null
                    ) { closeMenu ->
                        episodes.forEachIndexed { index, ep ->
                            DropdownMenuItem(
                                text = {
                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                                        Text("Ep ${ep.episode_number}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        ep.name?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                    }
                                },
                                onClick = { onSelectEpisode(ep); closeMenu() },
                                leadingIcon = if (ep == selectedEpisode) ({ Icon(Icons.Default.Check, null, tint = MaterialTheme.subAccent, modifier = Modifier.size(14.dp)) }) else null,
                                modifier = Modifier.background(if (ep == selectedEpisode) MaterialTheme.subAccentContainer else Color.Transparent)
                            )
                            if (index < episodes.lastIndex) HorizontalDivider(Modifier.padding(horizontal = 10.dp), thickness = 0.5.dp, color = colors.outlineVariant.copy(alpha = 0.3f))
                        }
                    }

                    // Season pill
                    SubDropdownPill(
                        labelMain = selectedSeason?.let { "S${it.season_number}" } ?: "الموسم",
                        labelSub = selectedSeason?.let { "الموسم ${it.season_number}" } ?: "اختر",
                        enabled = true,
                        loading = false,
                        isFilled = selectedSeason != null
                    ) { closeMenu ->
                        tvShow.seasons.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = {
                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                                        Text("S${s.season_number}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text("الموسم ${s.season_number}", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                                    }
                                },
                                onClick = { onSelectSeason(s); closeMenu() },
                                leadingIcon = if (s == selectedSeason) ({ Icon(Icons.Default.Check, null, tint = MaterialTheme.subAccent, modifier = Modifier.size(14.dp)) }) else null,
                                modifier = Modifier.background(if (s == selectedSeason) MaterialTheme.subAccentContainer else Color.Transparent)
                            )
                            if (index < tvShow.seasons.lastIndex) HorizontalDivider(Modifier.padding(horizontal = 10.dp), thickness = 0.5.dp, color = colors.outlineVariant.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
    }
}

// ─── Dropdown pill (Season / Episode) ───────────────────────────────────────
@Composable
private fun SubDropdownPill(
    labelMain: String,
    labelSub: String,
    enabled: Boolean,
    loading: Boolean,
    isFilled: Boolean,
    menuContent: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "arr")
    val colors = MaterialTheme.colorScheme

    Box {
        val borderCol = when {
            isFilled -> MaterialTheme.subAccentBorder
            expanded -> MaterialTheme.subAccent
            else -> colors.outlineVariant.copy(alpha = 0.5f)
        }
        val bgCol = if (isFilled || expanded) MaterialTheme.subAccentContainer else colors.surfaceContainerHigh

        Row(
            modifier = Modifier
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(0.5.dp, if (enabled) borderCol else colors.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                .background(if (enabled) bgCol else colors.surfaceContainerLow)
                .then(if (enabled) Modifier.clickable { expanded = true } else Modifier)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = if (isFilled || expanded) MaterialTheme.subAccent else colors.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(13.dp)
                    .rotate(arrowRotation)
            )
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(11.dp),
                    strokeWidth = 1.4.dp,
                    color = MaterialTheme.subAccent
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = labelMain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) colors.onSurface else colors.onSurface.copy(alpha = 0.35f),
                    lineHeight = 14.sp
                )
                Text(
                    text = labelSub,
                    fontSize = 9.sp,
                    color = if (enabled) colors.onSurfaceVariant.copy(alpha = 0.55f) else colors.onSurface.copy(alpha = 0.2f),
                    lineHeight = 11.sp
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 260.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = colors.surfaceContainerHigh
        ) {
            menuContent { expanded = false }
        }
    }
}

// ─── Results header ──────────────────────────────────────────────────────────
@Composable
private fun ResultsHeader(count: Int, context: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceContainerHigh)
                    .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.5f), CircleShape)
                    .padding(horizontal = 9.dp, vertical = 2.dp)
            )
            if (context.isNotBlank()) {
                Text(
                    text = context,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = "نتائج الترجمة",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = colors.onSurfaceVariant.copy(alpha = 0.65f)
        )
    }
}

// ─── Subtitle result card ────────────────────────────────────────────────────
@Composable
fun SubtitleResultCard(
    subtitle: WyzieSubtitle,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val isMatch = subtitle.matchedRelease != null || subtitle.matchedFilter != null

    val borderColor = if (isMatch) MaterialTheme.subAccentBorder else colors.outlineVariant.copy(alpha = 0.5f)
    val bgColor = if (isMatch) MaterialTheme.subAccentContainer else Color.Transparent

    Surface(
        modifier = modifier.clickable { onDownload() },
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp)) {
            // Top row: download button – name – flag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .background(colors.surfaceContainerHigh)
                        .clickable { onDownload() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Download, null, tint = colors.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
                Text(
                    text = subtitle.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
                FlagImage(flagUrl = subtitle.flagUrl, langCode = subtitle.language ?: "??")
            }

            Spacer(Modifier.height(8.dp))

            // Pill row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val downloadCount = subtitle.downloadCount ?: 0
                if (downloadCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(Icons.Default.Download, null, tint = colors.onSurfaceVariant.copy(alpha = 0.45f), modifier = Modifier.size(10.dp))
                        Text(formatCount(downloadCount), style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant.copy(alpha = 0.5f))
                    }
                }
                Spacer(Modifier.weight(1f))
                subtitle.source?.let { SubPill(it, PillStyle.Source) }
                if (subtitle.isHearingImpaired) SubPill("وصفية", PillStyle.HearingImpaired)
                if (isMatch) SubPill("متوافق", PillStyle.Match, checkIcon = true)
                subtitle.format?.let { SubPill(it.uppercase(), PillStyle.Format) }
            }
        }
    }
}

// ─── Flag image ──────────────────────────────────────────────────────────────
@Composable
private fun FlagImage(flagUrl: String?, langCode: String) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(width = 30.dp, height = 21.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(colors.surfaceVariant)
            .border(0.5.dp, colors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (!flagUrl.isNullOrBlank()) {
            AsyncImage(flagUrl, langCode, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            Text(langCode.take(2).uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = colors.onSurfaceVariant)
        }
    }
}

// ─── Pill styles ─────────────────────────────────────────────────────────────
private enum class PillStyle { Format, Match, HearingImpaired, Source }

@Composable
private fun SubPill(text: String, style: PillStyle, checkIcon: Boolean = false) {
    val (bg, fg, border) = when (style) {
        PillStyle.Format -> Triple(Color(0x1A185FA5), Color(0xFF185FA5), Color(0x33185FA5))
        PillStyle.Match  -> Triple(MaterialTheme.subAccentContainer, MaterialTheme.subAccentDim, MaterialTheme.subAccent.copy(alpha = 0.25f))
        PillStyle.HearingImpaired -> Triple(Color(0x17854F0B), Color(0xFF854F0B), Color(0x33854F0B))
        PillStyle.Source -> Triple(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .border(0.5.dp, border, CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (checkIcon) Icon(Icons.Default.Check, null, tint = fg, modifier = Modifier.size(9.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = fg)
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────
private fun formatCount(count: Int): String = when {
    count >= 1_000_000 -> "${count / 1_000_000}.${(count % 1_000_000) / 100_000}M"
    count >= 1_000     -> "${count / 1_000}.${(count % 1_000) / 100}k"
    else               -> count.toString()
}

// ─── Legacy composables for call-site compatibility ──────────────────────────
@Composable
fun WyzieSubtitleRow(subtitle: WyzieSubtitle, onDownload: () -> Unit, modifier: Modifier = Modifier) =
    SubtitleResultCard(subtitle, onDownload, modifier)

@Composable
fun TmdbResultRow(result: WyzieTmdbResult, onClick: () -> Unit, modifier: Modifier = Modifier) =
    SuggestionRow(result, onClick)

@Composable
fun SeriesDetailsSection(
    tvShow: WyzieTvShowDetails,
    isFetchingSeasons: Boolean,
    selectedSeason: WyzieSeason?,
    onSelectSeason: (WyzieSeason) -> Unit,
    isFetchingEpisodes: Boolean,
    episodes: ImmutableList<WyzieEpisode>,
    selectedEpisode: WyzieEpisode?,
    onSelectEpisode: (WyzieEpisode) -> Unit,
    onClose: () -> Unit
) = TvShowControlRow(
    tvShow = tvShow,
    isFetchingDetails = isFetchingSeasons,
    selectedSeason = selectedSeason,
    onSelectSeason = onSelectSeason,
    isFetchingEpisodes = isFetchingEpisodes,
    episodes = episodes,
    selectedEpisode = selectedEpisode,
    onSelectEpisode = onSelectEpisode,
    onBack = onClose
)
