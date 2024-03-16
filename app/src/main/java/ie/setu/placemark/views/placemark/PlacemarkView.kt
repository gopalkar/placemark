package ie.setu.placemark.views.placemark

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.placemark.R
import ie.setu.placemark.activities.PlacemarkPresenter
import ie.setu.placemark.databinding.ActivityMainBinding
import ie.setu.placemark.models.PlacemarkModel
import timber.log.Timber.Forest.i

class PlacemarkView : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: PlacemarkPresenter
    var placemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PlacemarkPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.placemarkLocation.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.placemarkTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.warning_missingTitle, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                // presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())  
                presenter.doAddOrSave(binding.placemarkTitle.text.toString(), binding.description.text.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_addplacemark, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPlacemark(placemark: PlacemarkModel) {
        binding.placemarkTitle.setText(placemark.title)
        binding.description.setText(placemark.description)
        binding.btnAdd.setText(R.string.button_savePlacemark)
        Picasso.get()
            .load(placemark.image)
            .into(binding.placemarkImage)
        if (placemark.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.button_changeImage)
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.placemarkImage)
        binding.chooseImage.setText(R.string.button_changeImage)
    }
}