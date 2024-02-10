package ie.setu.placemark.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import ie.setu.placemark.R
import ie.setu.placemark.databinding.ActivityMainBinding
import ie.setu.placemark.models.PlacemarkModel
import ie.setu.placemark.main.MainApp
import timber.log.Timber.Forest.i

class PlacemarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp
    var placemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Placemark Activity started...")
        binding.btnAdd.setOnClickListener {
            //i("add Button Pressed")
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.placemarkDescription.text.toString()
            if (placemark.title.isNotEmpty()) {
                app.placemarks.add( placemark.copy() )
                i("add Button Pressed: $placemark")
                for (i in app.placemarks.indices)
                {
                    i("Placemark[$i]: ${this.app.placemarks[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                val launcherIntent = Intent(this, PlacemarkListActivity::class.java)
                startActivity(launcherIntent)
                //getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_addplacemark, menu)
        return super.onCreateOptionsMenu(menu)
    }
}