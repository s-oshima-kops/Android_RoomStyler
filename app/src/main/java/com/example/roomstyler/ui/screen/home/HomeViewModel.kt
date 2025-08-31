package com.example.roomstyler.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomstyler.domain.model.Layout
import com.example.roomstyler.domain.repository.LayoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val layoutRepository: LayoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecentLayouts()
    }

    private fun loadRecentLayouts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            layoutRepository.getRecentLayouts(10)
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
                .collect { layouts ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recentLayouts = layouts,
                        error = null
                    )
                }
        }
    }

    fun refreshLayouts() {
        loadRecentLayouts()
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val recentLayouts: List<Layout> = emptyList(),
    val error: String? = null
)
