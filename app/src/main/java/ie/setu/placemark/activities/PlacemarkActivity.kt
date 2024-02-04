package ie.setu.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import ie.setu.placemark.databinding.ActivityMainBinding
import ie.setu.placemark.models.PlacemarkModel
import timber.log.Timber.Forest.i

class PlacemarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var placemark = ArrayList<PlacemarkModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Placemark Activity started..")

        binding.btnAdd.setOnClickListener {
            //i("add Button Pressed")
            val title = binding.placemarkTitle.text.toString()
            val description = binding.placemarkDescription.text.toString()
            if (title.isNotEmpty()) {
                placemark.add( PlacemarkModel(title, description) )
                i("add Button Pressed: $placemark")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}