/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model.routines;


import BoBaPop.Model.Coffeemilkteamanager;

import javax.annotation.Generated;

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
public class SpGetdatasortedby extends AbstractRoutine<java.lang.Void> {

    private static final long serialVersionUID = 296032183;

    /**
     * The parameter <code>coffeemilkteamanager.sp_GetDataSortedBy.col_name</code>.
     */
    public static final Parameter<String> COL_NAME = createParameter("col_name", org.jooq.impl.SQLDataType.VARCHAR(20), false, false);

    /**
     * Create a new routine call instance
     */
    public SpGetdatasortedby() {
        super("sp_GetDataSortedBy", Coffeemilkteamanager.COFFEEMILKTEAMANAGER);

        addInParameter(COL_NAME);
    }

    /**
     * Set the <code>col_name</code> parameter IN value to the routine
     */
    public void setColName(String value) {
        setValue(COL_NAME, value);
    }
}
