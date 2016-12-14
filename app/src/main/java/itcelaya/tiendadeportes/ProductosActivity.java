package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import itcelaya.tiendadeportes.model.CategoryProducts;
import itcelaya.tiendadeportes.model.Product;
import itcelaya.tiendadeportes.model.Products;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.LoadCategoryProductsTask;
import itcelaya.tiendadeportes.task.LoadProductsTask;
import itcelaya.tiendadeportes.task.WooCommerceTaskP;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class ProductosActivity extends AppCompatActivity {
    ListView list;
    List<Products> items = new ArrayList<Products>();
    public static String consumer_key ="ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    public static String url = "https://192.168.0.23/DeportesITC/wc-api/v3/products";
    // String auth_url = "https://192.168.0.23/DeportesITC/auth_users.php";
    String jsonResult;
    ProductsAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        ActionBar actionBar= getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Lista de Productos</font>"));

        NukeSSLCerts.nuke();
        list = (ListView) findViewById(R.id.listProducts);
        registerForContextMenu(list);
        loadCategoriesProducts();
    }
    public void loadCategoriesProducts() {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        listProductos();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listProducts) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.menu_crud, menu);

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Adapter adapter = list.getAdapter();
        Products producto  = (Products) adapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.itmCrudOpc1:
                editCategory(producto.getId());
                break;
            case R.id.itmCrudOpc2:
                Toast.makeText(ProductosActivity.this, "Delete" + producto.getId(), Toast.LENGTH_SHORT).show();
                deleteProducto(producto.getId());
                break;
        }
        return true;
    }

    public void listProductos() {

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
                String description = jsonChildNode.optString("description");

                items.add(new Products(id_product, name, type, price, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        list.setAdapter(new ProductsAdapter(this, items));

    }
    private void deleteProducto(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Deseas eliminar el registro seleccionado?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WooCommerceTaskP tarea = new WooCommerceTaskP(ProductosActivity.this, WooCommerceTaskP.DELETE_TASK, "Eliminando Productos", new AsyncResponse() {
                    @Override
                    public void setResponse(String output) {
                        jsonResult = output;
                        //Toast.makeText(CategoriasActivity.this, "Categoria eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });
                tarea.execute(new String[] { url + "/" + id +"?force=true"});
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Dialog d = builder.create();
        d.show();
    }
    private void editCategory(int idPro) {
        Intent i = new Intent(this, EditProductActivity.class);
        i.putExtra("idproducto", idPro);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Boolean bandera = true;

        switch(id) {
            case R.id.mnuNuevoProducto:
                Intent i = new Intent(this,NewProductActivity.class);
                startActivity(i);
                break;
        }
        return bandera;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        list.setAdapter(null);
        items.clear();
        //cAdapter.notifyDataSetChanged();
        loadCategoriesProducts();
    }

}

