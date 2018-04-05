package layout

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import id.co.gmst.gajamadastone.GlobalVariables
import id.co.gmst.gajamadastone.LibraryInspira

import id.co.gmst.gajamadastone.R
import id.co.gmst.gajamadastone.ScrollingActivity
import org.json.JSONArray
import org.json.JSONObject

class listProductsFragment : Fragment() {

    val global: GlobalVariables = GlobalVariables

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var listProducts = ArrayList<product>()

    class product {
        var nomor: Int? = null
        var kode: String? = null
        var nama: String? = null
        var namajual: String? = null
        var satuan: String? = null
        var hargajual: Float? = null
        var barangTambang: Boolean? = null
        var barangImport: Boolean? = null

        constructor(nomor: Int, kode: String, nama: String, namajual: String, satuan: String, hargajual: Float, barangTambang: Boolean, barangImport: Boolean) {
            this.nomor = nomor
            this.kode = kode
            this.nama = nama
            this.namajual = namajual
            this.satuan = satuan
            this.hargajual = hargajual
            this.barangTambang = barangTambang
            this.barangImport = barangImport
        }
    }

    private inner class ItemAdapter : BaseAdapter {
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 20
        }


        private var itemsList = ArrayList<product>()
        private var context: Context? = null

        constructor(itemsList: ArrayList<product>, mainContext: Context) : super() {
            this.itemsList = itemsList
            this.context = mainContext
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.product, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.tvTitle.text = itemsList[position].namajual
            vh.tvContent.text = "Rp " + itemsList[position].hargajual

            return view!!
        }
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
        }

        //  if you target API 26, you should change to:
//        init {
//            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
//            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
//        }
    }

    private inner class getDataBarang : AsyncTask<String, Void, JSONArray>() {
        override fun doInBackground(vararg controller: String?): JSONArray {
            return LibraryInspira.requestMultiData(controller[0]!!, null)!!
        }

        override fun onPostExecute(result: JSONArray?) {
            super.onPostExecute(result)
            if (result != null) {
                LibraryInspira.setSharedString(global.productspreferences!!, global.products, result.toString())
                refreshList(result)
            }
            else
                LibraryInspira.makeToastLong("Terjadi Kesalahan\nError: 0x01\nAPI not returning anything", context!!)
        }
    }

    fun refreshList(newJsonArray: JSONArray?) {
        var jsonArray: JSONArray? = null
        if (newJsonArray == null)
            jsonArray = JSONArray(LibraryInspira.getSharedString(global.productspreferences!!, global.products, ""))
        else
            jsonArray = newJsonArray

//        for (i in 0..jsonArray.length()) {
        for (i in 0..jsonArray!!.length()-1) {
            val jsonObject = JSONObject(jsonArray[i].toString())
            listProducts.add(product(
                    jsonObject.get("nomor").toString().toInt(),
                    jsonObject.get("kode").toString(),
                    jsonObject.get("nama").toString(),
                    jsonObject.get("namajual").toString(),
                    jsonObject.get("satuan").toString(),
                    jsonObject.get("hargajual").toString().toFloat(),
                    jsonObject.get("tambang").toString().toBoolean(),
                    jsonObject.get("import").toString().toBoolean()
            ))
        }

        var itemAdapter = ItemAdapter(listProducts, context!!)
        val lvNotes = view?.findViewById<ListView>(R.id.lvProducts)
        Log.d("json", lvNotes.toString())
        lvNotes!!.adapter = itemAdapter
        lvNotes!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(this.context, ScrollingActivity::class.java)
            intent.putExtra("nomor", listProducts[position].nomor)
            intent.putExtra("kode", listProducts[position].kode)
            intent.putExtra("nama", listProducts[position].nama)
            intent.putExtra("namajual", listProducts[position].namajual)
            intent.putExtra("satuan", listProducts[position].satuan)
            intent.putExtra("hargajual", listProducts[position].hargajual)
            intent.putExtra("barangTambang", listProducts[position].barangTambang)
            intent.putExtra("barangImport", listProducts[position].barangImport)
            this.context!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (LibraryInspira.isInternetAvailable(context!!))
            getDataBarang().execute("Master/getBarang/")
        else
            refreshList(null)
        return inflater!!.inflate(R.layout.fragment_list_products, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}// Required empty public constructor
