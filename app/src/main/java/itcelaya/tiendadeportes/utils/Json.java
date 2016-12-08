package itcelaya.tiendadeportes.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import itcelaya.tiendadeportes.model.Customer;

/**
 * Created by niluxer on 5/25/16.
 */
public class Json {

    public static String toJSon(Customer customer) {
        try {
            // Convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("email", customer.getEmail());
            jsonObj.put("first_name", customer.getFirst_name());
            jsonObj.put("last_name", customer.getLast_name());
            jsonObj.put("username", customer.getUsername());

            JSONObject jsonBillingAddress = new JSONObject(); // Another object to store the address
            jsonBillingAddress.put("first_name", customer.getBilling_address().getFirst_name());
            jsonBillingAddress.put("last_name", customer.getBilling_address().getLast_name());
            jsonBillingAddress.put("company", "");
            jsonBillingAddress.put("address_1", "969 Market");
            jsonBillingAddress.put("address_2", "");
            jsonBillingAddress.put("city", "San Francisco");
            jsonBillingAddress.put("state", "CA");
            jsonBillingAddress.put("postcode", "94103");
            jsonBillingAddress.put("country", "US");
            jsonBillingAddress.put("email", customer.getEmail());
            jsonBillingAddress.put("phone", "(555) 555-5555");

            jsonObj.put("billing_address", jsonBillingAddress);


            JSONObject jsonShippingAddress = new JSONObject(); // Another object to store the address
            jsonShippingAddress.put("first_name", customer.getShipping_address().getFirst_name());
            jsonShippingAddress.put("last_name", customer.getShipping_address().getLast_name());
            jsonShippingAddress.put("company", "");
            jsonShippingAddress.put("address_1", "969 Market");
            jsonShippingAddress.put("address_2", "");
            jsonShippingAddress.put("city", "San Francisco");
            jsonShippingAddress.put("state", "CA");
            jsonShippingAddress.put("postcode", "94103");
            jsonShippingAddress.put("country", "US");

            jsonObj.put("shipping_address", jsonShippingAddress);


            JSONObject jsonCustomer = new JSONObject();
            jsonCustomer.put("customer", jsonObj);

            return jsonCustomer.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
