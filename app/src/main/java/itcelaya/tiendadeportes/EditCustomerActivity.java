package itcelaya.tiendadeportes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import itcelaya.tiendadeportes.model.Address;
import itcelaya.tiendadeportes.model.Address1;
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.WooCommerceTask;

public class EditCustomerActivity extends Activity implements View.OnClickListener {

    String jsonResult;
    int idCustomer;
    EditText txtNombres, txtApellidos, txtEmail, txtCompañia, txtTelefono, txtPais,txtDireccion, txtCiudad, txtEstado, txtCP ;
    Button btnEnviar, btnCancelar;
    Customer customer;
    String user;
    String id_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        Bundle extras = getIntent().getExtras();
        id_customer= extras.getString("id_customer");
        txtNombres   = (EditText) findViewById(R.id.txtNombres);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtEmail     = (EditText) findViewById(R.id.txtEmail);
        txtCompañia =(EditText) findViewById(R.id.txtCompañia);
        txtTelefono =(EditText) findViewById(R.id.txtTelefono);
        txtPais =(EditText) findViewById(R.id.txtPais);
        txtDireccion =(EditText) findViewById(R.id.txtCalle);
        txtCiudad =(EditText) findViewById(R.id.txtCiudad);
        txtEstado =(EditText) findViewById(R.id.txtEstado);
        txtCP =(EditText) findViewById(R.id.txtCP);


        btnEnviar    = (Button) findViewById(R.id.btnEnviar);
        btnCancelar  = (Button) findViewById(R.id.btnCancelar);
        btnEnviar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);


        Intent i = getIntent();
        idCustomer = i.getIntExtra("idCustomer", 0);
        user=i.getStringExtra("user");
        Toast.makeText(EditCustomerActivity.this, user, Toast.LENGTH_SHORT).show();
        loadCustomer();


    }

    private void loadCustomer() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Cliente", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                loadCustomerInForm();
            }
        });

        tarea.execute(new String[] { ClientesActivity.url + "/" + idCustomer });

        /*try {
            jsonResult = tarea.execute(new String[] { MainActivity.url + "/" + idCustomer }).get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (ExecutionException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }*/
        //Toast.makeText(EditCustomerActivity.this, jsonResult, Toast.LENGTH_SHORT).show();


    }

    private void loadCustomerInForm() {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);

            JSONObject jsonChildNode = jsonResponse.getJSONObject("customer");


            JSONObject jsonChildNodeBillingAddress = jsonChildNode.getJSONObject("billing_address");
            Address billingAddress = new Address(jsonChildNodeBillingAddress.getString("first_name"), jsonChildNodeBillingAddress.getString("last_name"),jsonChildNodeBillingAddress.getString("company"),jsonChildNodeBillingAddress.getString("phone"),jsonChildNodeBillingAddress.getString("address_1"),jsonChildNodeBillingAddress.getString("city"),jsonChildNodeBillingAddress.getString("country"),jsonChildNodeBillingAddress.getString("state"),jsonChildNodeBillingAddress.getString("postcode"));
            JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
            Address1 shippingAddress = new Address1(jsonChildNodeShippingAddress.getString("first_name"), jsonChildNodeShippingAddress.getString("last_name"));

            customer =
                    new Customer(
                            jsonChildNode.getInt("id"),
                            jsonChildNode.getString("email"),
                            jsonChildNode.getString("first_name"),
                            jsonChildNode.getString("last_name"),
                            jsonChildNode.getString("username"),
                            billingAddress,
                            shippingAddress
                    );
            //System.out.println("Nombres: " + jsonChildNode.getString("first_name"));
            //System.out.println("Apellidos: " + jsonChildNode.getString("last_name"));

        }  catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        txtNombres.setText(customer.getFirst_name());
        txtApellidos.setText(customer.getLast_name());
        txtEmail.setText(customer.getEmail());;
        txtCompañia.setText(customer.getBilling_address().getCompany());
        txtTelefono.setText(customer.getBilling_address().getPhone());
        txtPais.setText(customer.getBilling_address().getCountry());
        txtDireccion.setText(customer.getBilling_address().getAddress_1());
        txtCiudad.setText(customer.getBilling_address().getCity());
        txtEstado.setText(customer.getBilling_address().getState());
        txtCP.setText(customer.getBilling_address().getPostcode());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnviar:
                if(user.equals("admin")){
                    saveCustomer();
                    Intent admin = new Intent(this, AdminActivity.class);
                    startActivity(admin);
                }
                else if(user.equals("clie")){
                    saveCustomer();
                    finish();
                }
                //saveCustomer();
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }


    private void saveCustomer() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.PUT_TASK, "Guardando Cliente...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Toast.makeText(EditCustomerActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        customer.setFirst_name(txtNombres.getText().toString());
        customer.setLast_name(txtApellidos.getText().toString());
        customer.setBilling_address(new Address(txtNombres.getText().toString(),txtApellidos.getText().toString(),txtCompañia.getText().toString(),txtTelefono.getText().toString(),txtDireccion.getText().toString(),txtCiudad.getText().toString(),txtCiudad.getText().toString(),txtEstado.getText().toString(),txtCP.getText().toString()));
        //customer.setBilling_address(new Address1(txtNombres.getText().toString(),txtApellidos.getText().toString()));
        tarea.setObject(customer);

        tarea.execute(new String[] { ClientesActivity.url + "/" + idCustomer });

        //String json_customer = Json.toJSon(customer);
        //Toast.makeText(EditCustomerActivity.this, json_customer, Toast.LENGTH_LONG).show();
        //System.out.println(json_customer);


    }

}

