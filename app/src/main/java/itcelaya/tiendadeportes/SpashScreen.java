package itcelaya.tiendadeportes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ProgressBar;

public class SpashScreen extends AppCompatActivity {
    ProgressBar pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        ActionBar actionBar= getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#fffcfc'></font>"));
        pro=(ProgressBar) findViewById(R.id.pgbinicio);
        new Hilo().execute();
    }
    class Hilo extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int valor;
            for (int i=1; i<=5;i++){
                try {
                    Thread.sleep(1000);
                    publishProgress(i*20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pro.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intencion = new Intent(SpashScreen.this, MainActivity.class);
            startActivity(intencion);
        }
    }
}
