package ie.setu.placemark.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

var image: Uri = Uri.EMPTY
@Parcelize
data class PlacemarkModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY
): Parcelable