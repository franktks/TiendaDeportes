package itcelaya.tiendadeportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
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
                Intent cupones = new Intent(getApplicationContext(), AltaCuponActivity.class);
                startActivity(cupones);
                break;
            case R.id.itemadmOpc6:
                Intent reportes = new Intent(getApplicationContext(), ReportesActivity.class);
                startActivity(reportes);
                break;
            case R.id.itemadmOpc7:
               // Intent ialta = new Intent(getApplicationContext(), AltaActivity.class);
                //startActivity(ialta);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
