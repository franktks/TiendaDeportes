package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import itcelaya.tiendadeportes.model.Products;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.LoadProductsTask;
import itcelaya.tiendadeportes.task.WooCommerceTask;

public class AdminActivity extends AppCompatActivity {
    ListView listProducts;
    List<Products> items = new ArrayList<Products>();
    List a_reviews=new ArrayList();
    public static String consumer_key = "ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    Button btn_regresa;
    ArrayAdapter adaptesReview;
    BDTienda objcreate;
    SQLiteDatabase objexecute;
    String url = "https://192.168.0.23/DeportesITC/wc-api/v3/products";
    String jsonResult;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ActionBar actionBar= getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Home Admin</font>"));
        listProducts = (ListView) findViewById(R.id.listProductoAdmin);
        listProducts.setOnItemClickListener(listenerProducts);
        objcreate= new BDTienda(this,"DESPENSA",null,1);
        objexecute = objcreate.getWritableDatabase();
       // MainActivity.NukeSSLCerts.nuke();

        loadProducts();
    }
    AdapterView.OnItemClickListener listenerProducts= new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Products p = items.get(position);
            //Toast.makeText(getBaseContext(),"Producto"+ p.getId()+" Nombre"+ p.getTitle(),Toast.LENGTH_SHORT).show();
            loadReview(p.getId());

        }
    };
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_administrador,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemadmOpc1:
                Intent clientes = new Intent(getApplicationContext(), ClientesActivity.class);
                startActivity(clientes);
                break;
            case R.id.itemadmOpc2:
                Intent productos = new Intent(getApplicationContext(), ProductosActivity.class);
                startActivity(productos);
                break;
            case R.id.itemadmOpc3:
                Intent categorias = new Intent(getApplicationContext(), CategoriasActivity.class);
                startActivity(categorias);
                break;
            case R.id.itemadmOpc4:
                Intent ordenes = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(ordenes);
                break;
            case R.id.itemadmOpc5:
                Intent cupones = new Intent(getApplicationContext(), Cupones.class);
                startActivity(cupones);
                break;
            case R.id.itemadmOpc6:
                Intent reportes = new Intent(getApplicationContext(), Reporte2Activity.class);
                startActivity(reportes);
                break;
            case R.id.itemadmOpc7:
                eliminarNegocio();
                Intent ialta = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(ialta);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void loadReview(int id_producto) {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url+"/"+id_producto+"/reviews" }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        // ListProductos();
        a_reviews.clear();
        ListReview();



    }
    public void ListReview() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("product_reviews");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String review = jsonChildNode.optString("review");
                String name = jsonChildNode.optString("reviewer_name");
                String correo = jsonChildNode.optString("reviewer_email");
                a_reviews.add(name);
                a_reviews.add(correo);
                a_reviews.add(review);
                //items.add(new Products(review, name,correo, price, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }
        //System.out.println(a_reviews);
        // listProducts.setAdapter(new ProductsAdapter(this, items));
        AlertDialog.Builder b_reviews= new AlertDialog.Builder(this);
        LinearLayout layout1 = new LinearLayout(this);
        layout1.setOrientation(LinearLayout.VERTICAL);
        ListView v_reviews=new ListView(this);
        adaptesReview=new ArrayAdapter(this,android.R.layout.simple_list_item_1,a_reviews);
        //  System.out.println(adaptesReview);
        v_reviews.setAdapter(adaptesReview);
        layout1.addView(v_reviews);
        b_reviews.setView(layout1);
        dialog=b_reviews.create();
        dialog.show();
        //builder.setView(ivFoto);


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



    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

}