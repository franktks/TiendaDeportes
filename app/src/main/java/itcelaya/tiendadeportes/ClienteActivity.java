package itcelaya.tiendadeportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ClienteActivity extends AppCompatActivity {
//lista productos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itmCliOpc1:
                Intent carrito = new Intent(getApplicationContext(), ComprasActivity.class);
                startActivity(carrito);
                //this.finish();
                break;
            case R.id.itmCliOpc2:
                Intent historial = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(historial);
                //this.finish();
                break;
            case R.id.itmCliOpc3:
                //Intent compras = new Intent(getApplicationContext(), ComprasActivity.class);
                //startActivity(compras);
                //this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
