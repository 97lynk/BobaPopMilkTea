/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model;


import BoBaPop.Model.tables.Bills;
import BoBaPop.Model.tables.Drinks;
import BoBaPop.Model.tables.Drinktypes;
import BoBaPop.Model.tables.Items;
import BoBaPop.Model.tables.Users;
import BoBaPop.Model.tables.VwBillDetails;
import BoBaPop.Model.tables.VwBillStatistic;
import BoBaPop.Model.tables.VwDrinks;
import BoBaPop.Model.tables.VwItems;
import BoBaPop.Model.tables.VwSumDrinkByBill;
import BoBaPop.Model.tables.VwTodayBills;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in db_milktea
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
     * The table <code>db_milktea.bills</code>.
     */
    public static final Bills BILLS = BoBaPop.Model.tables.Bills.BILLS;

    /**
     * The table <code>db_milktea.drinks</code>.
     */
    public static final Drinks DRINKS = BoBaPop.Model.tables.Drinks.DRINKS;

    /**
     * The table <code>db_milktea.drinktypes</code>.
     */
    public static final Drinktypes DRINKTYPES = BoBaPop.Model.tables.Drinktypes.DRINKTYPES;

    /**
     * The table <code>db_milktea.items</code>.
     */
    public static final Items ITEMS = BoBaPop.Model.tables.Items.ITEMS;

    /**
     * The table <code>db_milktea.tables</code>.
     */
    public static final BoBaPop.Model.tables.Tables TABLES = BoBaPop.Model.tables.Tables.TABLES;

    /**
     * The table <code>db_milktea.users</code>.
     */
    public static final Users USERS = BoBaPop.Model.tables.Users.USERS;

    /**
     * VIEW
     */
    public static final VwBillDetails VW_BILL_DETAILS = BoBaPop.Model.tables.VwBillDetails.VW_BILL_DETAILS;

    /**
     * VIEW
     */
    public static final VwBillStatistic VW_BILL_STATISTIC = BoBaPop.Model.tables.VwBillStatistic.VW_BILL_STATISTIC;

    /**
     * VIEW
     */
    public static final VwDrinks VW_DRINKS = BoBaPop.Model.tables.VwDrinks.VW_DRINKS;

    /**
     * VIEW
     */
    public static final VwItems VW_ITEMS = BoBaPop.Model.tables.VwItems.VW_ITEMS;

    /**
     * VIEW
     */
    public static final VwSumDrinkByBill VW_SUM_DRINK_BY_BILL = BoBaPop.Model.tables.VwSumDrinkByBill.VW_SUM_DRINK_BY_BILL;

    /**
     * VIEW
     */
    public static final VwTodayBills VW_TODAY_BILLS = BoBaPop.Model.tables.VwTodayBills.VW_TODAY_BILLS;
}
