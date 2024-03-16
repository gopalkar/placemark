package ie.setu.placemark.activities

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.placemark.databinding.ActivityMainBinding
import ie.setu.placemark.main.MainApp
import ie.setu.placemark.models.Location
import ie.setu.placemark.models.PlacemarkModel

import ie.setu.placemark.helpers.showImagePicker
import ie.setu.placemark.views.placemark.PlacemarkView
import timber.log.Timber

class PlacemarkPresenter(private val view: PlacemarkView) {

    var placemark = PlacemarkModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityMainBinding = ActivityMainBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = view.intent.extras?.getParcelable("placemark_edit")!!
            view.showPlacemark(placemark)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }
    fun doAddOrSave(title: String, description: String) {
        placemark.title = title
        placemark.description = description
        if (edit) {
            app.placemarks.update(placemark)
        } else {
            app.placemarks.create(placemark)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }
    fun doCancel() {
        view.finish()
    }
    fun doDelete() {
        view.setResult(99)
        app.placemarks.delete(placemark)
        view.finish()
    }
    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
    }
    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (placemark.zoom != 0f) {
            location.lat =  placemark.lat
            location.lng = placemark.lng
            location.zoom = placemark.zoom
        }
        val launcherIntent = Intent(view,MapActivity::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
    fun cachePlacemark (title: String, description: String) {
        placemark.title = title;
        placemark.description = description
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            placemark.image = result.data!!.data!!
                            view.contentResolver.takePersistableUriPermission(placemark.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            view.updateImage(placemark.image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            placemark.lat = location.lat
                            placemark.lng = location.lng
                            placemark.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }}