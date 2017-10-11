/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model.routines;


import BoBaPop.Model.Coffeemilkteamanager;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Chargedtotalonitem extends AbstractRoutine<Double> {

    private static final long serialVersionUID = 148564634;

    /**
     * The parameter <code>coffeemilkteamanager.ChargedTotalOnItem.RETURN_VALUE</code>.
     */
    public static final Parameter<Double> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.FLOAT, false, false);

    /**
     * The parameter <code>coffeemilkteamanager.ChargedTotalOnItem.Bill_ID</code>.
     */
    public static final Parameter<Integer> BILL_ID = createParameter("Bill_ID", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>coffeemilkteamanager.ChargedTotalOnItem.Drink_ID</code>.
     */
    public static final Parameter<Integer> DRINK_ID = createParameter("Drink_ID", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * Create a new routine call instance
     */
    public Chargedtotalonitem() {
        super("ChargedTotalOnItem", Coffeemilkteamanager.COFFEEMILKTEAMANAGER, org.jooq.impl.SQLDataType.FLOAT);

        setReturnParameter(RETURN_VALUE);
        addInParameter(BILL_ID);
        addInParameter(DRINK_ID);
    }

    /**
     * Set the <code>Bill_ID</code> parameter IN value to the routine
     */
    public void setBillId(Integer value) {
        setValue(BILL_ID, value);
    }

    /**
     * Set the <code>Bill_ID</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setBillId(Field<Integer> field) {
        setField(BILL_ID, field);
    }

    /**
     * Set the <code>Drink_ID</code> parameter IN value to the routine
     */
    public void setDrinkId(Integer value) {
        setValue(DRINK_ID, value);
    }

    /**
     * Set the <code>Drink_ID</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setDrinkId(Field<Integer> field) {
        setField(DRINK_ID, field);
    }
}
