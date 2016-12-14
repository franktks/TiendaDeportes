package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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

import itcelaya.tiendadeportes.model.Orders;
import itcelaya.tiendadeportes.task.LoadOrdersTask;

public class OrdersActivity extends AppCompatActivity {
    ListView listOrders;

    List<Orders> items = new ArrayList<Orders>();

    // List aReviews = new ArrayList();

    Dialog dialog;
    Button btnRegresa;
    String customer_id;
    String consumer_key = "ck_9504739e63fdbdefa492559699298c92b10237ed";
    String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    String url = "https://192.168.0.23/DeportesITC/wc-api/v3/orders";
    String producto;
    String jsonResult,jsonResultuno;
    //String id_customer;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ActionBar actionBar= getSupportActionBar();
        //  actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Lista Ordenes</font>"));
//        Bundle extras = getIntent().getExtras();
  //      id_customer= extras.getString("id_customer");
        listOrders = (ListView) findViewById(R.id.listPedidos);
        listOrders.setOnItemClickListener(listenerOrders);
        //Toast.makeText(this, id_customer, Toast.LENGTH_SHORT).show();

        //   listProducts.setOnItemClickListener(listener);
        OrdersActivity.NukeSSLCerts.nuke();
          loadOrders();
    }
    AdapterView.OnItemClickListener listenerOrders = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Orders o = items.get(position);
            //Toast.makeText(getBaseContext(),"Holi", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(),"Producto :"+o.getId(), Toast.LENGTH_SHORT).show();
            llamar(o.getId());
            loaduno(o.getId());
        }
    };

    private void loadOrders() {
        LoadOrdersTask tarea = new LoadOrdersTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListOrders();

    }
    private void loaduno(int id) {
        LoadOrdersTask tarea = new LoadOrdersTask(this, consumer_key, consumer_secret);
        try {
            jsonResultuno = tarea.execute(new String[] { url+"/"+id }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        Listuno();
    }
    public void Listuno() {
        List<String> supplierNames1 = new ArrayList<String>();
        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResultuno);
            //
            JSONObject json=jsonResponse.optJSONObject("order");
            String nombre=json.optString("status");
            JSONArray items = json.getJSONArray("line_items");
            Toast.makeText(getBaseContext(),items.toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonChildNode = items.getJSONObject(i);
                String nombre_producto= jsonChildNode.optString("name");
                String precio_producto=jsonChildNode.optString("price");
                customer_id = jsonChildNode.optString(("customer_id"));
                supplierNames1.add(nombre_producto);
                supplierNames1.add(precio_producto);
                Toast.makeText(getBaseContext(), nombre_producto+precio_producto, Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }


    }
    public void llamar(int id){
        Intent intent=new Intent(this,DetallesActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void ListOrders() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("orders");
            JSONArray jsonMainproductos = jsonResponse.optJSONArray("line_items");

            String firstName=null;
            String lastName=null;
            String email=null;
            String methodTitle=null;

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Integer id = jsonChildNode.optInt("id");
                Integer orderNumber = jsonChildNode.optInt("order_number");
                String createdAt = jsonChildNode.optString("created_at");
                Integer total_producto = jsonChildNode.optInt("total_line_items_quantity");
                Double total = jsonChildNode.optDouble("total");

                //if (jsonChildNode.isNull("payment_details")) {
                //  methodTitle = "vacio";
                //} else {

                JSONObject jsonChildmetod = jsonChildNode.getJSONObject("payment_details");
                if(jsonChildmetod.isNull("method_title")){

                }else {
                    methodTitle = jsonChildmetod.optString("method_title");
                }
                //methodTitle=jsonMainmetod.length()+"";
                // }
                JSONObject jsonChildnombre = jsonChildNode.getJSONObject("billing_address");
                if (jsonChildnombre.isNull("first_name")) {
                    firstName = "cliente vacio";
                } else {
                    firstName = jsonChildnombre.optString("first_name");
                }

                if (jsonChildnombre.isNull("last_name")) {
                    lastName = "apellido vacio";
                } else {
                    lastName = jsonChildnombre.optString("last_name");
                }
                if (jsonChildnombre.isNull("email")) {
                    email = "email vacio";
                } else {
                    email = jsonChildnombre.optString("email");
                }
                String status = jsonChildNode.optString("status");
                int id_producto=jsonChildNode.optInt("line_items");

                items.add(new Orders(id, orderNumber, createdAt, total_producto, total, methodTitle, firstName, lastName, email,status,id_producto));
               // Toast.makeText(getApplicationContext(), "Error" + items.toString(),
                 //       Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }


        listOrders.setAdapter(new OrdersAdapter(this, items));
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
