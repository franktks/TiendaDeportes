package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
import itcelaya.tiendadeportes.model.Address1;
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.LoginTask;
import itcelaya.tiendadeportes.task.WooCommerceTask;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class ClientesActivity extends AppCompatActivity implements View.OnClickListener{

    ListView list;
    TextView txtuser;
    List<Customer> items   = new ArrayList<Customer>();
    public static String consumer_key ="ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    public static String url = "https://192.168.0.23/DeportesITC/wc-api/v3/customers";
    String auth_url = "https://192.168.0.23/DeportesITC/auth_users.php";
    String jsonResult, loginResult;

    Dialog dLogin;
    CustomerAdapter cAdapter;


    Button btnAceptar, btnCancelar;
    EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        ActionBar actionBar= getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Lista Clientes</font>"));

        NukeSSLCerts.nuke();

        loadCustomers();

        list = (ListView) findViewById(R.id.listClientes);
        //list = getListView();
        list.setOnItemClickListener(listenerOrdenes);
        registerForContextMenu(list);

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

        if (v.getId() == R.id.listClientes) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.menu_crud, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Adapter adapter = list.getAdapter();
        //Object obj  = adapter.getItem(info.position);
        Customer customer  = (Customer) adapter.getItem(info.position);
//        txtuser = (TextView) dLogin.findViewById(R.id.user);


        switch (item.getItemId()) {
            case R.id.itmCrudOpc1:
              //  Toast.makeText(ClientesActivity.this, "Edit" + customer.getLast_name(), Toast.LENGTH_SHORT).show();
                editCustomer(customer.getId());

                break;
            case R.id.itmCrudOpc2:
                //Toast.makeText(ClientesActivity.this, "Delete" + customer.getLast_name(), Toast.LENGTH_SHORT).show();
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
                    Address billingAddress = new Address(jsonChildNodeBillingAddress.getString("first_name"), jsonChildNodeBillingAddress.getString("last_name"),jsonChildNodeBillingAddress.getString("company"),jsonChildNodeBillingAddress.getString("phone"),jsonChildNodeBillingAddress.getString("address_1"),jsonChildNodeBillingAddress.getString("city"),jsonChildNodeBillingAddress.getString("country"),jsonChildNodeBillingAddress.getString("state"),jsonChildNodeBillingAddress.getString("postcode"));
                    JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
                    Address1 shippingAddress = new Address1(jsonChildNodeShippingAddress.getString("first_name"), jsonChildNodeShippingAddress.getString("last_name"));

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
        Toast.makeText(ClientesActivity.this, username, Toast.LENGTH_SHORT).show();
        Toast.makeText(ClientesActivity.this, password, Toast.LENGTH_SHORT).show();
        Toast.makeText(ClientesActivity.this, loginResult, Toast.LENGTH_SHORT).show();
        LoginTask tarea = new LoginTask(ClientesActivity.this);
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
                WooCommerceTask tarea = new WooCommerceTask(ClientesActivity.this, WooCommerceTask.DELETE_TASK, "Eliminando Cliente", new AsyncResponse() {
                    @Override
                    public void setResponse(String output) {
                        jsonResult = output;
                        Toast.makeText(ClientesActivity.this, "Cliente eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });
                tarea.execute(new String[] { ClientesActivity.url + "/" + idCustomer });

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
        String user="admin";
        Intent i = new Intent(this, EditCustomerActivity.class);
        i.putExtra("idCustomer", idCustomer);
        i.putExtra("user",user);
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
