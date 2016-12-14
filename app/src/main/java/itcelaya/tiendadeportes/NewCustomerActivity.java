package itcelaya.tiendadeportes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import itcelaya.tiendadeportes.model.Address;
import itcelaya.tiendadeportes.model.Address1;
import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.WooCommerceTask;

public class NewCustomerActivity extends Activity implements View.OnClickListener {

    String jsonResult;
    int idCustomer;
    EditText txtNombres, txtApellidos, txtEmail, txtUsuario,txtCompañia,txtTelefono,txtPais,txtCalle,txtCiudad,txtEstado,txtCP;
    Button btnEnviar, btnCancelar;
    Customer customer = new Customer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        txtNombres   = (EditText) findViewById(R.id .txtNombresN);
        txtApellidos = (EditText) findViewById(R.id.txtApellidosN);
        txtEmail     = (EditText) findViewById(R.id.txtEmailN);
        txtUsuario   = (EditText) findViewById(R.id.txtUsuarioN);
        txtCompañia   = (EditText) findViewById(R.id.txtCompañiaN);
        txtTelefono   = (EditText) findViewById(R.id.txtTelefonoN);
        txtPais   = (EditText) findViewById(R.id.txtPaisN);
        txtCalle   = (EditText) findViewById(R.id.txtCalleN);
        txtCiudad   = (EditText) findViewById(R.id.txtCiudadN);
        txtEstado   = (EditText) findViewById(R.id.txtEstadoN);
        txtCP   = (EditText) findViewById(R.id.txtCPN);

        btnEnviar    = (Button) findViewById(R.id.btnEnviarN);
        btnCancelar  = (Button) findViewById(R.id.btnCancelarN);
        btnEnviar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnviarN:
                saveCustomer();
                cliente();
                break;
            case R.id.btnCancelarN:
                finish();
                break;
        }
    }

    public void cliente(){
        String user = "clie";
        String c="";
        Intent intencion = new Intent(this, ComprasActivity.class);
        intencion.putExtra("id_producto",c);
        intencion.putExtra("user",user);
        startActivity(intencion);
    }
    private void saveCustomer() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.POST_TASK, "Guardando Cliente...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Toast.makeText(NewCustomerActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        customer.setFirst_name(txtNombres.getText().toString());
        customer.setLast_name(txtApellidos.getText().toString());
        customer.setEmail(txtEmail.getText().toString());
        customer.setUsername(txtUsuario.getText().toString());
        customer.setBilling_address(new Address(txtNombres.getText().toString(), txtApellidos.getText().toString(),txtCompañia.getText().toString(),txtTelefono.getText().toString(),txtCalle.getText().toString(),txtCiudad.getText().toString(),txtPais.getText().toString(),txtEstado.getText().toString(),txtCP.getText().toString()));
        customer.setShipping_address(new Address1(txtNombres.getText().toString(), txtApellidos.getText().toString()));

        //JSONObject jsonChildNodeBillingAddress = jsonChildNode.getJSONObject("billing_address");
        //customer.setBilling_address(new Address(jsonChildNodeBillingAddress.getString("first_name"), jsonChildNodeBillingAddress.getString("last_name"),jsonChildNodeBillingAddress.getString("company"),jsonChildNodeBillingAddress.getString("phone"),jsonChildNodeBillingAddress.getString("address_1"),jsonChildNodeBillingAddress.getString("city"),jsonChildNodeBillingAddress.getString("country"),jsonChildNodeBillingAddress.getString("state"),jsonChildNodeBillingAddress.getString("postcode"));
        //JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
        //Address1 shippingAddress = new Address1(jsonChildNodeShippingAddress.getString("first_name"), jsonChildNodeShippingAddress.getString("last_name"));

        tarea.setObject(customer);

        tarea.execute(new String[] { ClientesActivity.url });

        //String json_customer = Json.toJSon(customer);
        //Toast.makeText(NewCustomerActivity.this, json_customer, Toast.LENGTH_LONG).show();
        //System.out.println(json_customer);

    }

}

