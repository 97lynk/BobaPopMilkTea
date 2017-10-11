/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model;


import BoBaPop.Model.tables.Bills;
import BoBaPop.Model.tables.Customers;
import BoBaPop.Model.tables.Customertypes;
import BoBaPop.Model.tables.Drinks;
import BoBaPop.Model.tables.Drinktypes;
import BoBaPop.Model.tables.Items;
import BoBaPop.Model.tables.VwBilldetails;
import BoBaPop.Model.tables.VwItems;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in coffeemilkteamanager
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * InnoDB free: 6144 kB; (`CustomerID`) REFER `coffeemilkteamanager/customers`(`Cus
     */
    public static final Bills BILLS = BoBaPop.Model.tables.Bills.BILLS;

    /**
     * InnoDB free: 6144 kB; (`CustomerTypeID`) REFER `coffeemilkteamanager/customertyp
     */
    public static final Customers CUSTOMERS = BoBaPop.Model.tables.Customers.CUSTOMERS;

    /**
     * InnoDB free: 6144 kB
     */
    public static final Customertypes CUSTOMERTYPES = BoBaPop.Model.tables.Customertypes.CUSTOMERTYPES;

    /**
     * InnoDB free: 6144 kB; (`DrinkTypeID`) REFER `coffeemilkteamanager/drinktypes`(`D
     */
    public static final Drinks DRINKS = BoBaPop.Model.tables.Drinks.DRINKS;

    /**
     * InnoDB free: 6144 kB
     */
    public static final Drinktypes DRINKTYPES = BoBaPop.Model.tables.Drinktypes.DRINKTYPES;

    /**
     * InnoDB free: 6144 kB; (`BillID`) REFER `coffeemilkteamanager/bills`(`BillID`); (
     */
    public static final Items ITEMS = BoBaPop.Model.tables.Items.ITEMS;

    /**
     * InnoDB free: 6144 kB
     */
    public static final BoBaPop.Model.tables.Tables TABLES = BoBaPop.Model.tables.Tables.TABLES;

    /**
     * VIEW
     */
    public static final VwBilldetails VW_BILLDETAILS = BoBaPop.Model.tables.VwBilldetails.VW_BILLDETAILS;

    /**
     * VIEW
     */
    public static final VwItems VW_ITEMS = BoBaPop.Model.tables.VwItems.VW_ITEMS;
}
