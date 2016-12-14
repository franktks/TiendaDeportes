package itcelaya.tiendadeportes;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import itcelaya.tiendadeportes.ProductsAdapter;
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;
import itcelaya.tiendadeportes.task.LoadProductsTask;
import itcelaya.tiendadeportes.model.Products;

/**
 * Created by anton on 05/06/2016.
 */
public class MainActivity extends AppCompatActivity {

    String ip = "192.168.0.23";
    BDTienda objcreate;
    SQLiteDatabase objexecute;
    ListView listProducts;

    List<Products> items = new ArrayList<Products>();

    public static String consumer_key = "ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";

    String url = "https://192.168.0.23/DeportesITC/wc-api/v3/products";
    String jsonResult;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Añadir Producto</font>"));
        listProducts = (ListView) findViewById(R.id.listPro);
        listProducts.setOnItemClickListener(listenerOrdenes);
        objcreate= new BDTienda(this,"DESPENSA",null,1);
        objexecute = objcreate.getWritableDatabase();
        Bundle extras = getIntent().getExtras();
        user= extras.getString("user");

        NukeSSLCerts.nuke();

        loadProducts();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invitado,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemInOpc1:
                String c="";
                Intent carrito = new Intent(getApplicationContext(), ComprasActivity.class);
                carrito.putExtra("id_producto",c);
                carrito.putExtra("user",user);
                startActivity(carrito);
                break;
            case R.id.itemInOpc2:
                eliminarNegocio();
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                break;



        }
        return super.onOptionsItemSelected(item);
    }
    public void eliminarNegocio(){
        try{
            String query="DELETE from productos";
            objexecute.execSQL("PRAGMA foreign_keys=ON;");
            objexecute.execSQL(query);
            objexecute.execSQL("PRAGMA foreign_keys=OFF;");
            Toast.makeText(this,"Orden Enviada",Toast.LENGTH_LONG).show();
            Intent tienda =new Intent(this,MainActivity.class);
            startActivity(tienda);
            this.finish();
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    AdapterView.OnItemClickListener listenerOrdenes = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(MainActivity.this, view.getTag() + "", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, ComprasActivity.class);
            Products o = items.get(i);
            //Toast.makeText(getBaseContext(),"Holi", Toast.LENGTH_SHORT).show();
            intent.putExtra("id_producto", view.getTag().toString());
            intent.putExtra("user",user);
            startActivity(intent);
        }
    };
       public void probar(){
        Toast.makeText(this, "probar", Toast.LENGTH_LONG).show();
    }
    private void loadProducts(String id_pro) {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url+"/"+id_pro }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListProductos();

    }

    private void loadProducts() {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListProductos();

    }

    public void ListProductos() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("title");
                String type = jsonChildNode.optString("type");
                Integer id_product = jsonChildNode.optInt("id");
                Double price = jsonChildNode.optDouble("regular_price");
                String ImageURL = jsonChildNode.optString("featured_src");

                items.add(new Products(id_product, name, type, price, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        listProducts.setAdapter(new ProductsAdapter(this, items));
    }
}