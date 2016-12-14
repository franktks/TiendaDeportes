package itcelaya.tiendadeportes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import itcelaya.tiendadeportes.model.Products;
import itcelaya.tiendadeportes.task.LoadProductsTask;
import itcelaya.tiendadeportes.utils.NukeSSLCerts;

public class ComprasActivity extends AppCompatActivity {
    String id_productos;
    String ip = "192.168.0.23";
    Button btncheckout, btnCuponV;
    EditText edtCupones;

    ListView listProducts;

    List<Products> items = new ArrayList<Products>();
    BDTienda objcreate;
    SQLiteDatabase objexecute;

    public static String consumer_key = "ck_9504739e63fdbdefa492559699298c92b10237ed";
    public static String consumer_secret = "cs_b374d814c0b933e991812a107fe184de7fd94db3";

    String url = "https://192.168.0.23/DeportesITC/wc-api/v3/products";
    String url_coupons = "https://192.168.0.23/DeportesITC/wc-api/v3/coupons";

    String jsonResult,jsonResultCoupons;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
        ActionBar actionBar= getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Productos seleccionados</font>"));
        listProducts = (ListView) findViewById(R.id.listCompras);
        btnCuponV=(Button) findViewById(R.id.btnCuponV);
        edtCupones=(EditText) findViewById(R.id.EdtCupones);

        objcreate= new BDTienda(this,"DESPENSA",null,1);
        objexecute = objcreate.getWritableDatabase();


        Bundle extras = getIntent().getExtras();
        btncheckout=(Button) findViewById(R.id.btncheckout);
        id_productos= extras.getString("id_producto");
        user= extras.getString("user");
        Toast.makeText(getBaseContext(),"Producto :"+ id_productos+ user, Toast.LENGTH_SHORT).show();

        if(user.equals("invi")){
            edtCupones.setVisibility(View.INVISIBLE);
            btnCuponV.setVisibility(View.INVISIBLE);
        }
        else if(user.equals("clie")){
            edtCupones.setVisibility(View.VISIBLE);
            btnCuponV.setVisibility(View.VISIBLE);
        }else if(user.equals("admin")){
            edtCupones.setVisibility(View.VISIBLE);
            btnCuponV.setVisibility(View.VISIBLE);
        }


        // listProducts.setOnItemClickListener(listenerOrdenes);
        NukeSSLCerts.nuke();
        if(id_productos.equals("")){

        }else{
            String query = "INSERT INTO productos(id_producto)" +
                    " VALUES('" + id_productos + "')";
            objexecute.execSQL("PRAGMA foreign_keys=ON;");
            objexecute.execSQL(query);
            objexecute.execSQL("PRAGMA foreign_keys=OFF;");
            Toast.makeText(this, "Compra añadida", Toast.LENGTH_LONG).show();}


        Cursor c = objexecute.rawQuery("select * from productos",null);
        if(c.moveToFirst()){
            do{
                if(c.getString(1).equals("")){
                    continue;
                }
                else{
                    loadProducts(c.getString(1));}
                //Toast.makeText(this, c.getString(1), Toast.LENGTH_LONG).show();
            }while(c.moveToNext());
        }
        btncheckout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(user.equals("invi")){
                    NewCustomer();
                }
                else if(user.equals("clie")){

                }

            }
        });
        btnCuponV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadCupones();

            }
        });
    }
    private void NewCustomer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No estas registrado");
        // builder.setMessage("Para poder realizar la compra de estos productos necesitas estar registrado en la plataforma.");
        builder.setMessage("Para poder realizar la compra de estos productos necesitas estar registrado en la plataforma.\n¿Desea Registrarte a la plataforma?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent newclientes = new Intent(getApplicationContext(), NewCustomerActivity.class);
                startActivity(newclientes);
                //eliminarNegocio();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //finish();
            }
        });

        Dialog d = builder.create();
        d.show();
    }
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
    public void probar(){
        Toast.makeText(this, "probar", Toast.LENGTH_LONG).show();
    }


    private void loadProducts(String id_pro) {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url+"/"+id_pro }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListProductos();

    }

    private void loadCupones() {
        LoadProductsTask tarea = new LoadProductsTask(this, consumer_key, consumer_secret);
        try {
            jsonResultCoupons = tarea.execute(new String[] { url_coupons }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ValidarCoupons();

    }
    public void ValidarCoupons(){
        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResultCoupons);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("coupons");
            String validar="";
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String code = jsonChildNode.optString("code");
                String porciento = jsonChildNode.optString("amount");
                Integer uso_limite = jsonChildNode.optInt("limit_usage_to_x_items");
                Double usado = jsonChildNode.optDouble("usage_count");
                String expiracion = jsonChildNode.optString("expiry_date");
                if(edtCupones.getText().toString().equals(code)){
                    validar="El codigo: "+ code+"es valido"+"\nContiente un "+porciento+"% de descuento"+"\n Tiene un limite de uso de "+uso_limite+"\n Actualmente se ha usado "+usado+"veces"+"\nLa fecha de expiracion es: "+expiracion;
                }
            }
            if(validar!=""){
                Toast.makeText(getBaseContext(), validar, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getBaseContext(), "El codigo que ha ingresado es invalido", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void ListProductos() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONObject jsonMainNode = jsonResponse.optJSONObject("product");
            String name = jsonMainNode.optString("title");

           // for (int i = 0; i < jsonMainNode.length(); i++) {
             //   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String type = jsonMainNode.optString("type");
                Integer id_product = jsonMainNode.optInt("id");
                Double price = jsonMainNode.optDouble("regular_price");
                String ImageURL = jsonMainNode.optString("featured_src");
            items.add(new Products(id_product, name, type, price, ImageURL));





         //   }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }
        listProducts.setAdapter(new ProductsAdapter(this, items));
    }
    }
