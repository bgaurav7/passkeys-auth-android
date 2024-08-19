package `in`.bgaurav.passkeys.view

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import `in`.bgaurav.passkeys.R
import `in`.bgaurav.passkeys.databinding.ActivityBookBinding
import `in`.bgaurav.passkeys.utility.DataProvider

class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            DataProvider.logout()
            finish()
        }
    }
}