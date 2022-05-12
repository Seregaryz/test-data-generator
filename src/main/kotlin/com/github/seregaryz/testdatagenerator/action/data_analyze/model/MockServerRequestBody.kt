package com.github.seregaryz.testdatagenerator.action.data_analyze.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MockServerRequestBody(
    val jsonData: String?,
    val endpoint: String?
): Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(jsonData)
        dest?.writeString(endpoint)
    }
}
