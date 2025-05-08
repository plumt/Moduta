package com.yun.seoul.moduta.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
) {
    val isSuccess: Boolean get() = data != null
    val isError: Boolean get() = error != null

    companion object {
        fun <T> loading() = UiState<T>(isLoading = true)
        fun <T> success(data: T) = UiState(
            isLoading = false,
            data = data
        )

        fun <T> error(message: String) = UiState<T>(
            isLoading = false,
            error = message
        )
    }
}