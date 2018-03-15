/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model;


import BoBaPop.Model.tables.Bills;
import BoBaPop.Model.tables.Drinks;
import BoBaPop.Model.tables.Drinktypes;
import BoBaPop.Model.tables.Items;
import BoBaPop.Model.tables.Tables;
import BoBaPop.Model.tables.Users;
import BoBaPop.Model.tables.records.BillsRecord;
import BoBaPop.Model.tables.records.DrinksRecord;
import BoBaPop.Model.tables.records.DrinktypesRecord;
import BoBaPop.Model.tables.records.ItemsRecord;
import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.UsersRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>db_milktea</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<BillsRecord, Integer> IDENTITY_BILLS = Identities0.IDENTITY_BILLS;
    public static final Identity<DrinksRecord, Integer> IDENTITY_DRINKS = Identities0.IDENTITY_DRINKS;
    public static final Identity<DrinktypesRecord, Integer> IDENTITY_DRINKTYPES = Identities0.IDENTITY_DRINKTYPES;
    public static final Identity<TablesRecord, Integer> IDENTITY_TABLES = Identities0.IDENTITY_TABLES;
    public static final Identity<UsersRecord, Integer> IDENTITY_USERS = Identities0.IDENTITY_USERS;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<BillsRecord> KEY_BILLS_PRIMARY = UniqueKeys0.KEY_BILLS_PRIMARY;
    public static final UniqueKey<DrinksRecord> KEY_DRINKS_PRIMARY = UniqueKeys0.KEY_DRINKS_PRIMARY;
    public static final UniqueKey<DrinktypesRecord> KEY_DRINKTYPES_PRIMARY = UniqueKeys0.KEY_DRINKTYPES_PRIMARY;
    public static final UniqueKey<ItemsRecord> KEY_ITEMS_PRIMARY = UniqueKeys0.KEY_ITEMS_PRIMARY;
    public static final UniqueKey<TablesRecord> KEY_TABLES_PRIMARY = UniqueKeys0.KEY_TABLES_PRIMARY;
    public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = UniqueKeys0.KEY_USERS_PRIMARY;
    public static final UniqueKey<UsersRecord> KEY_USERS_USERNAME = UniqueKeys0.KEY_USERS_USERNAME;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<BillsRecord, TablesRecord> BILLS_IBFK_2 = ForeignKeys0.BILLS_IBFK_2;
    public static final ForeignKey<DrinksRecord, DrinktypesRecord> DRINKS_IBFK_1 = ForeignKeys0.DRINKS_IBFK_1;
    public static final ForeignKey<ItemsRecord, BillsRecord> ITEMS_IBFK_1 = ForeignKeys0.ITEMS_IBFK_1;
    public static final ForeignKey<ItemsRecord, DrinksRecord> ITEMS_IBFK_2 = ForeignKeys0.ITEMS_IBFK_2;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<BillsRecord, Integer> IDENTITY_BILLS = createIdentity(Bills.BILLS, Bills.BILLS.BILLID);
        public static Identity<DrinksRecord, Integer> IDENTITY_DRINKS = createIdentity(Drinks.DRINKS, Drinks.DRINKS.DRINKID);
        public static Identity<DrinktypesRecord, Integer> IDENTITY_DRINKTYPES = createIdentity(Drinktypes.DRINKTYPES, Drinktypes.DRINKTYPES.DRINKTYPEID);
        public static Identity<TablesRecord, Integer> IDENTITY_TABLES = createIdentity(Tables.TABLES, Tables.TABLES.TABLEID);
        public static Identity<UsersRecord, Integer> IDENTITY_USERS = createIdentity(Users.USERS, Users.USERS.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<BillsRecord> KEY_BILLS_PRIMARY = createUniqueKey(Bills.BILLS, "KEY_bills_PRIMARY", Bills.BILLS.BILLID);
        public static final UniqueKey<DrinksRecord> KEY_DRINKS_PRIMARY = createUniqueKey(Drinks.DRINKS, "KEY_drinks_PRIMARY", Drinks.DRINKS.DRINKID);
        public static final UniqueKey<DrinktypesRecord> KEY_DRINKTYPES_PRIMARY = createUniqueKey(Drinktypes.DRINKTYPES, "KEY_drinktypes_PRIMARY", Drinktypes.DRINKTYPES.DRINKTYPEID);
        public static final UniqueKey<ItemsRecord> KEY_ITEMS_PRIMARY = createUniqueKey(Items.ITEMS, "KEY_items_PRIMARY", Items.ITEMS.BILLID, Items.ITEMS.DRINKID);
        public static final UniqueKey<TablesRecord> KEY_TABLES_PRIMARY = createUniqueKey(Tables.TABLES, "KEY_tables_PRIMARY", Tables.TABLES.TABLEID);
        public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = createUniqueKey(Users.USERS, "KEY_users_PRIMARY", Users.USERS.ID);
        public static final UniqueKey<UsersRecord> KEY_USERS_USERNAME = createUniqueKey(Users.USERS, "KEY_users_UserName", Users.USERS.USERNAME);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<BillsRecord, TablesRecord> BILLS_IBFK_2 = createForeignKey(BoBaPop.Model.Keys.KEY_TABLES_PRIMARY, Bills.BILLS, "bills_ibfk_2", Bills.BILLS.TABLEID);
        public static final ForeignKey<DrinksRecord, DrinktypesRecord> DRINKS_IBFK_1 = createForeignKey(BoBaPop.Model.Keys.KEY_DRINKTYPES_PRIMARY, Drinks.DRINKS, "drinks_ibfk_1", Drinks.DRINKS.DRINKTYPEID);
        public static final ForeignKey<ItemsRecord, BillsRecord> ITEMS_IBFK_1 = createForeignKey(BoBaPop.Model.Keys.KEY_BILLS_PRIMARY, Items.ITEMS, "items_ibfk_1", Items.ITEMS.BILLID);
        public static final ForeignKey<ItemsRecord, DrinksRecord> ITEMS_IBFK_2 = createForeignKey(BoBaPop.Model.Keys.KEY_DRINKS_PRIMARY, Items.ITEMS, "items_ibfk_2", Items.ITEMS.DRINKID);
    }
}
