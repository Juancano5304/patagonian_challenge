package com.example.patagonianchallenge

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.lang.Exception
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import org.junit.Rule




class MainActivityViewModelTest {

    private lateinit var viewModel: MainActivityViewModel

    companion object {
        const val EVEN_SESSION_COUNT = 2
        const val ODD_SESSION_COUNT = 3
        const val ZERO_SESSION_COUNT = 0
        const val TEN_OR_HIGHER_SESSION_COUNT = 11
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        viewModel = MainActivityViewModel()
    }

    @Rule @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun showDialogCorrectly() {
        viewModel.showSessionCount(EVEN_SESSION_COUNT)
        assertTrue(viewModel.showDialog.value!!)
    }

    @Test
    fun showViewCorrectly() {
        viewModel.showSessionCount(ODD_SESSION_COUNT)
        assertTrue(viewModel.showView.value!!)
    }

    @Test
    fun showNoDialogOrViewWhenSessionOnZero() {
        viewModel.showSessionCount(ZERO_SESSION_COUNT)
        assertNull(viewModel.showView.value)
        assertNull(viewModel.showDialog.value)
    }

    @Test
    fun showNoDialogOrViewWhenSessionOnTenOrHigher() {
        viewModel.showSessionCount(TEN_OR_HIGHER_SESSION_COUNT)
        assertNull(viewModel.showView.value)
        assertNull(viewModel.showDialog.value)
    }
}