package ie.setu.placemark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.placemark.R
import ie.setu.placemark.databinding.ActivityMainBinding
import ie.setu.placemark.helpers.showImagePicker
import ie.setu.placemark.models.PlacemarkModel
import ie.setu.placemark.main.MainApp
import timber.log.Timber.Forest.i

class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var placemark = PlacemarkModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Placemark Activity started...")
        var edit = false
        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.placemarkDescription.setText(placemark.description)
            binding.btnAdd.setText(R.string.button_savePlacemark)
            binding.chooseImage.setText(R.string.button_changeImage)
            Picasso.get()
                .load(placemark.image)
                .into(binding.placemarkImage)
        }

        binding.btnAdd.setOnClickListener {
            //i("add Button Pressed")
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.placemarkDescription.text.toString()
            if (placemark.title.isNotEmpty()) {
                if (edit)
                    app.placemarks.update( placemark.copy() )
                else
                    app.placemarks.create( placemark.copy() )
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,getString(R.string.warning_missingTitle), Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish()
//                val launcherIntent = Intent(this, PlacemarkListActivity::class.java)
//                startActivity(launcherIntent)
                //getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_addplacemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            placemark.image = result.data!!.data!!
                            Picasso.get()
                                .load(placemark.image)
                                .into(binding.placemarkImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}