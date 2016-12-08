package itcelaya.tiendadeportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invitado,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemInOpc1:
                Intent compra = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(compra);
                //this.finish();
                break;
            case R.id.itemInOpc2:
                Intent login = new Intent(getApplicationContext(), ComprasActivity.class);
                startActivity(login);
                //this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
