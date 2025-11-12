package ValueObjects;

public class OrderFlags {

    private ShippingType shipping;
    private WrapType wrap;
    private InsuranceType insurance;

    public OrderFlags(ShippingType shipping, WrapType wrap, InsuranceType insurance) {
        this.shipping = shipping;
        this.wrap = wrap;
        this.insurance = insurance;
    }

    public ShippingType getShipping() {
        return shipping;
    }

    public WrapType getWrap() {
        return wrap;
    }

    public InsuranceType getInsurance() {
        return insurance;
    }

    public enum ShippingType {
        STANDARD,
        EXPRESS
    }

    public enum WrapType {
        NONE,
        GIFT
    }

    public enum InsuranceType {
        NONE,
        BASIC
    }
}