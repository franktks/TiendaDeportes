package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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

import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.task.LoginTask;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static String consumer_key ="ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";
    public static String url = "https://192.168.0.23/DeportesITC/wc-api/v3/customers";
    String auth_url = "https://192.168.0.23/DeportesITC/auth_users.php";
    String loginResult;

    Dialog dLogin;


    Button btnAceptar, btnCancelar;
    EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar= getSupportActionBar();
     //   actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Login</font>"));
        NukeSSLCerts.nuke();


        txtUsername = (EditText) findViewById(R.id.txtlogUsername);
        txtPassword = (EditText) findViewById(R.id.txtlogPassword);
        btnAceptar = (Button)  findViewById(R.id.btnlogaceptar);
        btnCancelar = (Button) findViewById(R.id.btnlogCancelar);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogaceptar:
                validaAcceso();
                break;
            case R.id.btnlogCancelar:
                break;
        }
    }
    private void validaAcceso () {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        LoginTask tarea = new LoginTask(this);
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

        Toast.makeText(this, loginResult, Toast.LENGTH_SHORT).show();


        try {
            JSONObject jso = new JSONObject(loginResult);
            JSONArray jsonMainNode = jso.optJSONArray("auth");

            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id_Customer= jsonChildNode.optString("id");
                Boolean valido = jsonChildNode.optBoolean("valido");
                String rol=jsonChildNode.getString("rol");
                //Toast.makeText(this, id_Customer, Toast.LENGTH_SHORT).show();
                //
                if (valido == true && rol.equals("customer")) {
//                    dLogin.dismiss();
                    Intent intent = new Intent(this, ClienteActivity.class);
                    intent.putExtra("id_customer",id_Customer);
                    startActivity(intent);
                   // loadCustomers();
                }
                else if (valido == true && rol.equals("administrator")){
                    Intent administrador = new Intent(this, AdminActivity.class);
                    startActivity(administrador);
                }
                else{
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
}
