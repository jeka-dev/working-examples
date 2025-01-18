package app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CoffeeShopModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = -1L;

    private String name = "";

    private String address = "";

    private String phone = "";

    private Double priceOfCoffee = 0.0;

    private boolean powerAccessible = true;

    private int internetReliability = 3; // 1-5

    public CoffeeShopModel(String name,
                           String address,
                           String phone,
                           double priceOfCoffee,
                           boolean powerAccessible,
                           int internetReliability) {
        this.internetReliability = internetReliability;
        this.powerAccessible = powerAccessible;
        this.priceOfCoffee = priceOfCoffee;
        this.phone = phone;
        this.address = address;
        this.name = name;
    }
    // Getters and Setters


    public CoffeeShopModel() {
    }

    public Long getId() {
        return id;
    }

    public CoffeeShopModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CoffeeShopModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public CoffeeShopModel setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public CoffeeShopModel setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Double getPriceOfCoffee() {
        return priceOfCoffee;
    }

    public CoffeeShopModel setPriceOfCoffee(Double priceOfCoffee) {
        this.priceOfCoffee = priceOfCoffee;
        return this;
    }

    public boolean isPowerAccessible() {
        return powerAccessible;
    }

    public CoffeeShopModel setPowerAccessible(boolean powerAccessible) {
        this.powerAccessible = powerAccessible;
        return this;
    }

    public int getInternetReliability() {
        return internetReliability;
    }

    public CoffeeShopModel setInternetReliability(int internetReliability) {
        this.internetReliability = internetReliability;
        return this;
    }
}