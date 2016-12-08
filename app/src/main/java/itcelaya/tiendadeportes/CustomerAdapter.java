package itcelaya.tiendadeportes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import itcelaya.tiendadeportes.model.Customer;
import itcelaya.tiendadeportes.utils.Utils;


public class CustomerAdapter extends BaseAdapter {
    private Context context;
    public List<Customer> customers;
    ImageView img1;

    public CustomerAdapter(Context context, List<Customer> productos) {
        super();
        this.context = context;
        this.customers = productos;
    }

    @Override
    public int getCount() {
        return this.customers.size();
    }

    @Override
    public Object getItem(int position) {
        return this.customers.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_customers, null);
        }

        TextView tvNombre   = (TextView) rowView.findViewById(R.id.tvNombre);
        TextView tvEmail  = (TextView) rowView.findViewById(R.id.tvEmail);
        img1 = (ImageView) rowView.findViewById(R.id.avatarCustomer);

        final Customer item = this.customers.get(position);
        tvNombre.setText(item.getFirst_name() + " " + item.getLast_name() );
        tvEmail.setText(item.getEmail());
        rowView.setTag(item.getId());
        //String sUrl = item.getImageUrl();

        String sUrl = "https://www.gravatar.com/avatar/" + Utils.md5(item.getEmail().toLowerCase());

        System.out.println("URL GRAVATAR " + sUrl);

        try {
            final Bitmap bitmap = new BackgroundTask().execute(sUrl).get();
            img1.setImageBitmap(bitmap);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ImageView ivFoto = new ImageView(context);
                    TextView tvNombre = new TextView(context);
                    tvNombre.setText(item.getUsername());
                    ivFoto.setImageBitmap(bitmap);

                    LinearLayout layout1 = new LinearLayout(context);
                    layout1.setOrientation(LinearLayout.VERTICAL);
                    layout1.addView(ivFoto);
                    layout1.addView(tvNombre);
                    //builder.setView(ivFoto);
                    builder.setView(layout1);
                    AlertDialog dialogFoto = builder.create();
                    dialogFoto.show();

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rowView;

    }

    private class BackgroundTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... url) {
            //---download an image---
            Bitmap bitmap = Utils.DownloadImage(url[0]);
            return bitmap;
        }
    }

}
