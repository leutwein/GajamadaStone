package id.co.gmst.gajamadastone

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        addtocart.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action" + intent.getIntExtra("nomor", 0), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setTitle(intent.getStringExtra("namajual"))
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as android.support.v7.widget.Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        findViewById<AppBarLayout>(R.id.app_bar).setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PicsActivity::class.java)
            this.startActivity(intent)
        })
        kodeBarang.setText(intent.getStringExtra("kode"))
        namaBarang.setText(intent.getStringExtra("nama"))
        satuanBarang.setText(intent.getStringExtra("satuan"))
        hargaBarang.setText("Rp " + intent.getFloatExtra("hargajual", 0F))
        if (intent.getBooleanExtra("barangTambang", false) == true)
            barangTambang.setText("✓ Barang Tambang ")
        else
            barangTambang.setText("X Bukan Barang Tambang")

        if (intent.getBooleanExtra("barangImport", false) == true)
            barangImport.setText("✓ Barang Import")
        else
            barangImport.setText("X Bukan Barang Import")

//        kodeBarang.setText(intent.getStringExtra("kode"))


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
