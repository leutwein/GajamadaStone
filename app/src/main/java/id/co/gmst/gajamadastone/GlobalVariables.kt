package id.co.gmst.gajamadastone

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Shoma on 1/28/2018.
 */

object GlobalVariables {
    private val domain : String = "http://vpn.inspiraworld.com:99"
    private val webserviceURL : String = domain + "/wsGMS/gms/index.php/api/"
    val fragmentID: Int = R.id.fragment_container

    var userpreferences: SharedPreferences? = null
    var productspreferences: SharedPreferences? = null

    var user: User = User()


    fun initialize(context: Context) {
        userpreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        productspreferences = context.getSharedPreferences("products", Context.MODE_PRIVATE)
    }

    val products = "products"

    class User {
        val nomor = "nomor"
        val email = "email"
        val nama = "nama"
        val alamat = "alamat"
        val hash = "hash"
        val token = "token"
        val telepon = "telp"
        val fax = "fax"
        val namapengiriman = "namapengiriman"
        val alamatpengiriman = "alamatpengiriman"
        val teleponpengiriman = "teleponpengiriman"
        val hp = "hp"
    }


    fun getServiceURL() : String {
        return webserviceURL
    }
}