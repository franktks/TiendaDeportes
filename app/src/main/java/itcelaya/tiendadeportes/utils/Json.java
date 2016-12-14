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
            jsonBillingAddress.put("company", customer.getBilling_address().getCompany());
            jsonBillingAddress.put("address_1", customer.getBilling_address().getAddress_1());
            jsonBillingAddress.put("address_2", "");
            jsonBillingAddress.put("city", customer.getBilling_address().getCity());
            jsonBillingAddress.put("state", customer.getBilling_address().getState());
            jsonBillingAddress.put("postcode", customer.getBilling_address().getPostcode());
            jsonBillingAddress.put("country", customer.getBilling_address().getCountry());
            jsonBillingAddress.put("email", customer.getEmail());
            jsonBillingAddress.put("phone", customer.getBilling_address().getPhone());

            jsonObj.put("billing_address", jsonBillingAddress);


            JSONObject jsonShippingAddress = new JSONObject(); // Another object to store the address
            jsonShippingAddress.put("first_name", customer.getShipping_address().getFirst_name());
            jsonShippingAddress.put("last_name", customer.getShipping_address().getLast_name());
            jsonShippingAddress.put("company", customer.getBilling_address().getCompany());
            jsonShippingAddress.put("address_1", customer.getBilling_address().getAddress_1());
            jsonShippingAddress.put("address_2", "");
            jsonShippingAddress.put("city", customer.getBilling_address().getCity());
            jsonShippingAddress.put("state", customer.getBilling_address().getState());
            jsonShippingAddress.put("postcode", customer.getBilling_address().getPostcode());
            jsonShippingAddress.put("country", customer.getBilling_address().getCountry());

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
