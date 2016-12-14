package itcelaya.tiendadeportes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import itcelaya.tiendadeportes.model.Address;
import itcelaya.tiendadeportes.model.Address1;
import itcelaya.tiendadeportes.model.Coupon;
import itcelaya.tiendadeportes.task.AsyncResponse;
import itcelaya.tiendadeportes.task.CouponsTask;
import itcelaya.tiendadeportes.task.WooCommerceTask;
import itcelaya.tiendadeportes.task.WooCommerceTaskC;
import itcelaya.tiendadeportes.task.WooCommerceTaskcupones;
import itcelaya.tiendadeportes.task.WoocommerceTaskCo;

public class AltaCuponActivity extends AppCompatActivity implements View.OnClickListener{

    String jsonResult;
    EditText txtcodigo, txtdescripcion,monto,limite,montominimo,montomaximo;
    Button btnEnviar;
    Coupon cupon=new Coupon();
    String urla = "https://192.168.0.23/DeportesITC/wc-api/v3/coupons";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_cupon);
        ActionBar actionBar= getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Registrar Cupon</font>"));

        txtcodigo=(EditText) findViewById(R.id.etnumorder);
        txtdescripcion=(EditText) findViewById(R.id.etdescripcion);
        monto=(EditText) findViewById(R.id.etmonto);
        limite=(EditText) findViewById(R.id.etlimite);
        montominimo=(EditText) findViewById(R.id.etmontominimo);
        montomaximo=(EditText) findViewById(R.id.etmontomaximo);
        btnEnviar = (Button) findViewById(R.id.agregarcupon);
        btnEnviar.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agregarcupon:
                saveCoupon2();
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }


    private void saveCoupon2() {
        WoocommerceTaskCo tarea = new WoocommerceTaskCo(this, WoocommerceTaskCo.POST_TASK, "Guardando Coupon...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
               Toast.makeText(AltaCuponActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        cupon.setCodigo(txtcodigo.getText().toString());
        cupon.setDescripcion(txtdescripcion.getText().toString());
        cupon.setMonto(monto.getText().toString());
        cupon.setLimitearticulos(Integer.parseInt(limite.getText().toString()));
        cupon.setMontominimo(montominimo.getText().toString());
        cupon.setMontomaximo(montomaximo.getText().toString());
        tarea.setObject(cupon);

        tarea.execute(new String[] { urla });

    }



}
