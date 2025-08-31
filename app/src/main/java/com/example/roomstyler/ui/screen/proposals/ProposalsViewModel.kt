package com.example.roomstyler.ui.screen.proposals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomstyler.domain.model.*
import com.example.roomstyler.domain.repository.FurnitureRepository
import com.example.roomstyler.domain.usecase.LayoutConstraintSolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProposalsViewModel @Inject constructor(
    private val layoutConstraintSolver: LayoutConstraintSolver,
    private val furnitureRepository: FurnitureRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProposalsUiState())
    val uiState: StateFlow<ProposalsUiState> = _uiState.asStateFlow()

    // デモ用の部屋データ
    private val demoRoom = Room(
        id = "demo-room",
        name = "リビングルーム",
        widthM = 6.0,
        heightM = 2.5,
        depthM = 4.0,
        doors = listOf("entrance"),
        windows = listOf("main-window")
    )

    fun generateProposals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // 利用可能な家具を取得
                val availableFurniture = furnitureRepository.getAllFurniture().first()
                
                if (availableFurniture.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "家具データが見つかりません。カタログを確認してください。"
                    )
                    return@launch
                }
                
                // レイアウト案を生成
                val proposals = layoutConstraintSolver.generateLayoutProposals(
                    room = demoRoom,
                    availableFurniture = availableFurniture,
                    layoutIntent = null,
                    proposalCount = 5
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    proposals = proposals,
                    error = null
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "レイアウト案の生成に失敗しました: ${e.message}"
                )
            }
        }
    }

    fun generateProposalsWithIntent(layoutIntent: LayoutIntent) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val availableFurniture = furnitureRepository.getAllFurniture().first()
                
                val proposals = layoutConstraintSolver.generateLayoutProposals(
                    room = demoRoom,
                    availableFurniture = availableFurniture,
                    layoutIntent = layoutIntent,
                    proposalCount = 5
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    proposals = proposals,
                    error = null
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "レイアウト案の生成に失敗しました: ${e.message}"
                )
            }
        }
    }

    fun getProposal(proposalId: String): LayoutProposal? {
        return _uiState.value.proposals.find { it.id == proposalId }
    }
}

data class ProposalsUiState(
    val isLoading: Boolean = false,
    val proposals: List<LayoutProposal> = emptyList(),
    val error: String? = null
)
