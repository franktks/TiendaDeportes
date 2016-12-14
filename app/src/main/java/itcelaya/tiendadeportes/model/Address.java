package itcelaya.tiendadeportes.model;

/**
 * Created by niluxer on 5/25/16.
 */
public class Address {
    String first_name;
    String last_name;
    private String company;
    private String phone;
    private String address_1;
    private String city;
    private String country;
    private String state;
    private String postcode;


    public Address(String first_name, String last_name, String company, String phone, String address_1,String city, String country, String state, String postcode) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.company=company;
        this.phone=phone;
        this.address_1=address_1;
        this.city=city;
        this.country=country;
        this.state=state;
        this.postcode=postcode;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}