package com.debk007.template.presentation.screen

import androidx.lifecycle.ViewModel
import com.debk007.template.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val TAG = DetailViewModel::class.java.simpleName
}