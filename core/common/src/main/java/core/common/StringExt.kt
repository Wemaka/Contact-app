package core.common

fun String.normalizePhoneNumber(): String {
    return this.replace(Regex("[^0-9+]"), "")
}