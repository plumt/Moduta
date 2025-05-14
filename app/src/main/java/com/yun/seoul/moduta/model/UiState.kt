package com.yun.seoul.moduta.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val isEmpty: Boolean? = null,
) {
    val isSuccess: Boolean get() = data != null
    val isError: Boolean get() = error != null

    companion object {
        fun <T> loading() = UiState<T>(isLoading = true)
        fun <T> success(data: T) = UiState(
            isLoading = false,
            data = data,
            isEmpty = false,
        )

        fun <T> error(message: String) = UiState<T>(
            isLoading = false,
            error = message,
            isEmpty = null,
        )

        fun <T> empty() = UiState<T>(isEmpty = true)
    }
}

fun <T> UiState<T>.handleState(
    onEmpty: () -> Unit = {},
    onError: (String) -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit = {},
) {
    when {
        isEmpty == true -> onEmpty()
        isLoading -> onLoading()
        error != null -> onError(error)
        data != null -> onSuccess(data)
    }
}