package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import itcelaya.tiendadeportes.model.Address;
import itcelaya.tiendadeportes.model.CategoryProducts;
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.model.Products;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.LoadCategoryProductsTask;
import itcelaya.tiendadeportes.task.LoadOrdersTask;
import itcelaya.tiendadeportes.task.WooCommerceTask;
import itcelaya.tiendadeportes.task.WooCommerceTaskC;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class CategoriasActivity extends AppCompatActivity implements View.OnClickListener {
    ListView list;
    List<CategoryProducts> items = new ArrayList<CategoryProducts>();
    public static String consumer_key ="ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    public static String url = "https://192.168.0.23/DeportesITC/wc-api/v3/products/categories";
    // String auth_url = "https://192.168.0.23/DeportesITC/auth_users.php";
    String jsonResult;
    CategoryProductsAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        ActionBar actionBar= getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Lista de Categorias-Producto</font>"));
        NukeSSLCerts.nuke();
        list = (ListView) findViewById(R.id.listCategortProducts);
        list.setOnItemClickListener(listenerCategorias);
        registerForContextMenu(list);
        loadCategoriesProducts();
    }
    public void loadCategoriesProducts() {
        LoadCategoryProductsTask tarea = new LoadCategoryProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        listCategoriesProductos();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categoria, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listCategortProducts) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.menu_crud, menu);

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Adapter adapter = list.getAdapter();
        CategoryProducts category  = (CategoryProducts) adapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.itmCrudOpc1:
                editCategory(category.getId());
                break;
            case R.id.itmCrudOpc2:
                //Toast.makeText(CategoriasActivity.this, "Delete" + category.getId(), Toast.LENGTH_SHORT).show();
                deleteCategory(category.getId());
                break;
        }
        return true;
    }

    public void listCategoriesProductos() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("product_categories");
            //    Toast.makeText(getApplicationContext(), jsonMainNode.toString(),
            //          Toast.LENGTH_LONG).show();
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                int id = jsonChildNode.optInt("id");
                String name = jsonChildNode.optString("name");
                String slug = jsonChildNode.optString("slug");
                int parent = jsonChildNode.optInt("parent");
                String description = jsonChildNode.optString("description");
                String display = jsonChildNode.optString("display");
                int count = jsonChildNode.optInt("count");
                items.add(new CategoryProducts(id,name,slug,parent,description,display,count));


            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }
        if(items!=null) {
            cAdapter = new CategoryProductsAdapter(CategoriasActivity.this, items);
            list.setAdapter(cAdapter);
            //list.setAdapter(new CategoryProductsAdapter(this, items));
        }

    }
    private void deleteCategory(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Deseas eliminar el registro seleccionado?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WooCommerceTaskC tarea = new WooCommerceTaskC(CategoriasActivity.this, WooCommerceTaskC.DELETE_TASK, "Eliminando Categoria", new AsyncResponse() {
                    @Override
                    public void setResponse(String output) {
                        jsonResult = output;
                        //Toast.makeText(CategoriasActivity.this, "Categoria eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });
                tarea.execute(new String[] { url + "/" + id });
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
    private void editCategory(int idCa) {
        Intent i = new Intent(this, EditCategoryActivity.class);
        i.putExtra("idcategoria", idCa);
        startActivity(i);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Boolean bandera = true;

        switch(id) {
            case R.id.mnuCategoria:
                Intent i = new Intent(this,newCategoryActivity.class);
                startActivity(i);
                break;
        }
        return bandera;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        list.setAdapter(null);
        cAdapter.cat_productos.clear();
        //cAdapter.notifyDataSetChanged();
        loadCategoriesProducts();
    }

    AdapterView.OnItemClickListener listenerCategorias = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(MainActivity.this, view.getTag() + "", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onClick(View view) {

    }
}
