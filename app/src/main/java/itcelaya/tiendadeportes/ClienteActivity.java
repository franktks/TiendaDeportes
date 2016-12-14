package itcelaya.tiendadeportes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ClienteActivity extends AppCompatActivity {
    String id_customer;
    BDTienda objcreate;
    SQLiteDatabase objexecute;
    //lista productos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        ActionBar actionBar= getSupportActionBar();
    //    actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'>Home Cliente</font>"));
        Bundle extras = getIntent().getExtras();
        id_customer= extras.getString("id_customer");
        objcreate= new BDTienda(this,"DESPENSA",null,1);
        objexecute = objcreate.getWritableDatabase();
       comprobar();
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
    public void comprobar(){
        Toast.makeText(this, id_customer, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itmCliOpc1:
                String user="clie";
                Intent carrito = new Intent(getApplicationContext(), MainActivity.class);
                carrito.putExtra("user",user);
                startActivity(carrito);
                //this.finish();
                break;
            case R.id.itmCliOpc2:
                Intent historial = new Intent(getApplicationContext(), HistorialActivity.class);
                historial.putExtra("id_customer",id_customer);
                startActivity(historial);
                //this.finish();
                break;
            case R.id.itmCliOpc3:
                int id=Integer.parseInt(id_customer);
                editCustomer(id);
                break;
            case R.id.itmCliOpc4:
                eliminarNegocio();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    private void editCustomer(int idCustomer) {
        String user="clie";
        Intent i = new Intent(this, EditCustomerActivity.class);
        i.putExtra("idCustomer", idCustomer);
        i.putExtra("user",user);
        startActivity(i);

    }
}
