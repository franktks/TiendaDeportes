package itcelaya.tiendadeportes;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.LoginTask;
import itcelaya.tiendadeportes.task.WooCommerceTask;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    TextView txtuser;
    List<Customer> items   = new ArrayList<Customer>();
    public static String consumer_key    = "ck_a65d32cc7da4f54f71287a832336426a52161e50";
    public static String consumer_secret = "cs_cd1286641771420e8e1caf698da797f7ae8bb19b";
    //public static String url = "https://192.168.56.1/~niluxer/wordpress/wc-api/v3/customers";
    public static String url = "https://192.168.1.75/store_itc/wc-api/v3/customers";
    //String auth_url = "https://192.168.56.1/~niluxer/wordpress/auth_users.php";
    String auth_url = "https://192.168.1.75/store_itc/auth_users.php";
    String jsonResult, loginResult;

    Dialog dLogin;
    CustomerAdapter cAdapter;


    Button btnAceptar, btnCancelar;
    EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NukeSSLCerts.nuke();


        mostrarLogin();

        list = (ListView) findViewById(R.id.listCustomers);
        //list = getListView();
        list.setOnItemClickListener(listenerOrdenes);
        registerForContextMenu(list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invitado, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemInOpc1:
                Intent ialta = new Intent(getApplicationContext(), ComprasActivity.class);
                startActivity(ialta);
                this.finish();
                break;
            case R.id.itemInOpc2:
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        //Boolean bandera = true;

      //  switch (id) {
          /*  case R.id.mnuNew:
                newCustomer();
                break;

            default:
                bandera = super.onOptionsItemSelected(item);
        }
        return bandera;*/
    //    }
  //  }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listCustomers) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.menu_cliente, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Adapter adapter = list.getAdapter();
        //Object obj  = adapter.getItem(info.position);
        Customer customer  = (Customer) adapter.getItem(info.position);
        txtuser = (TextView) dLogin.findViewById(R.id.user);


        switch (item.getItemId()) {
            case R.id.itmCliOpc1:
                //Toast.makeText(MainActivity.this, "Edit" + customer.getLast_name(), Toast.LENGTH_SHORT).show();
                editCustomer(customer.getId());

                break;
            case R.id.itmCliOpc2:
                //Toast.makeText(MainActivity.this, "Delete" + customer.getLast_name(), Toast.LENGTH_SHORT).show();
                deleteCustomer(customer.getId());

                break;
        }
        return true;
    }

    public void loadSales() {
//String url_sales = "https://192.168.56.1/~niluxer/wordpress/wc-api/v3/reports/sales?filter[date_min]=2016-11-23&filter[date_max]=2016-12-05";
        String url_sales = "https://192.168.1.75/store_itc/wc-api/v3/reports/sales?filter[period]=year";

        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Reporte...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
               // Intent intent_grafica = new Intent(MainActivity.this, Grafica1Activity.class);
                //intent_grafica.putExtra("json", jsonResult);
                //startActivity(intent_grafica);
            }
        });

        tarea.execute(new String[] { url_sales });

    }


    private void mostrarLogin() {
        dLogin = new Dialog(this);
        dLogin.setTitle("Login");
        dLogin.setContentView(R.layout.login);

        txtUsername = (EditText) dLogin.findViewById(R.id.txtUsername);

        txtPassword = (EditText) dLogin.findViewById(R.id.txtPassword);
        btnAceptar = (Button) dLogin.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) dLogin.findViewById(R.id.btnCancelar);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        dLogin.show();
    }

    public void loadCustomers() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Clientes...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListCustomers();
            }
        });
        tarea.execute(new String[] { url });

    }

    public void ListCustomers() {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("customers");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                JSONObject jsonChildNodeBillingAddress = jsonChildNode.getJSONObject("billing_address");
                Address billingAddress = new Address(jsonChildNodeBillingAddress.getString("first_name"), jsonChildNodeBillingAddress.getString("last_name"));
                JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
                Address shippingAddress = new Address(jsonChildNodeShippingAddress.getString("first_name"), jsonChildNodeShippingAddress.getString("last_name"));

                items.add(
                        new Customer(
                                jsonChildNode.optInt("id"),
                                jsonChildNode.optString("email"),
                                jsonChildNode.optString("first_name"),
                                jsonChildNode.optString("last_name"),
                                jsonChildNode.optString("username"),
                                billingAddress,
                                shippingAddress
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        cAdapter = new CustomerAdapter(this, items);

        list.setAdapter(cAdapter);
    }

    AdapterView.OnItemClickListener listenerOrdenes = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(MainActivity.this, view.getTag() + "", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, CustomerOrdersActivity.class);
            intent.putExtra("id_customer", view.getTag().toString());
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAceptar:
                validaAcceso();
                break;
            case R.id.btnCancelar:
                break;
        }
    }

    private void validaAcceso () {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, loginResult, Toast.LENGTH_SHORT).show();
        LoginTask tarea = new LoginTask(MainActivity.this);
        tarea.setUsername(username);
        tarea.setPassword(password);
        try {
            loginResult = tarea.execute(new String[] { auth_url }).get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Error..." + e.getMessage());
        } catch (ExecutionException e) {
            //e.printStackTrace();
            System.out.println("Error..." + e.getMessage());
        }




        try {
            JSONObject jso = new JSONObject(loginResult);
            JSONArray jsonMainNode = jso.optJSONArray("auth");

            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Boolean valido = jsonChildNode.optBoolean("valido");
                String rol=jsonChildNode.getString("rol");
                //&& rol.equals("administrator")
                if (valido == true) {
                    dLogin.dismiss();
                    loadCustomers();
                } else {
                    Toast.makeText(this, "" +
                                    "Usuario y/o contrase;a no validos",
                            Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {
            //e.printStackTrace();
            System.out.println("Errors:" + e.getMessage());
        }


    }

    private void deleteCustomer(final int idCustomer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Deseas eliminar el registro seleccionado?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WooCommerceTask tarea = new WooCommerceTask(MainActivity.this, WooCommerceTask.DELETE_TASK, "Eliminando Cliente", new AsyncResponse() {
                    @Override
                    public void setResponse(String output) {
                        jsonResult = output;
                        Toast.makeText(MainActivity.this, "Cliente eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });
                tarea.execute(new String[] { MainActivity.url + "/" + idCustomer });

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

    private void editCustomer(int idCustomer) {
        Intent i = new Intent(this, EditCustomerActivity.class);
        i.putExtra("idCustomer", idCustomer);
        startActivity(i);

    }

    private void newCustomer() {
        Intent i = new Intent(this, NewCustomerActivity.class);
        startActivity(i);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        list.setAdapter(null);
        cAdapter.customers.clear();
        //cAdapter.notifyDataSetChanged();
        loadCustomers();
    }
}
