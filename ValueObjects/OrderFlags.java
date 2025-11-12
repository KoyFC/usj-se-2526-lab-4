package ValueObjects;

import Entities.Order;

public class OrderFlags {

    private ShippingType shipping;
    private WrapType wrap;
    private InsuranceType insurance;

    public OrderFlags() {
        this.shipping = ShippingType.STANDARD;
        this.wrap = WrapType.NONE;
        this.insurance = InsuranceType.NONE;
    }

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

    public OrderFlags setShipping(ShippingType shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderFlags setWrap(WrapType wrap) {
        this.wrap = wrap;
        return this;
    }

    public OrderFlags setInsurance(InsuranceType insurance) {
        this.insurance = insurance;
        return this;
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