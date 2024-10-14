package com.ravemaster.daddyjokes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravemaster.daddyjokes.data.Result
import com.ravemaster.daddyjokes.data.interfaces.BreweriesRepository
import com.ravemaster.daddyjokes.data.models.Breweries
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreweriesViewModel(
    private val breweriesRepository: BreweriesRepository
): ViewModel() {

    private val _breweries = MutableStateFlow(Breweries())
    val breweries = _breweries.asStateFlow()
    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            breweriesRepository.getList().collectLatest { result ->
                when(result){
                    is Result.Error -> _showErrorToastChannel.send(true)
                    is Result.Success ->
                        result.data?.let { breweries ->
                            _breweries.update { breweries }
                        }
                }
            }
        }
    }
}
