/*
 * This file is generated by jOOQ.
*/
package BoBaPop.Model.tables;


import BoBaPop.Model.DbMilktea;
import BoBaPop.Model.Indexes;
import BoBaPop.Model.Keys;
import BoBaPop.Model.tables.records.DrinksRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class Drinks extends TableImpl<DrinksRecord> {

    private static final long serialVersionUID = -373697373;

    /**
     * The reference instance of <code>db_milktea.drinks</code>
     */
    public static final Drinks DRINKS = new Drinks();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DrinksRecord> getRecordType() {
        return DrinksRecord.class;
    }

    /**
     * The column <code>db_milktea.drinks.DrinkID</code>.
     */
    public final TableField<DrinksRecord, Integer> DRINKID = createField("DrinkID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>db_milktea.drinks.DrinkName</code>.
     */
    public final TableField<DrinksRecord, String> DRINKNAME = createField("DrinkName", org.jooq.impl.SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>db_milktea.drinks.DrinkTypeID</code>.
     */
    public final TableField<DrinksRecord, Integer> DRINKTYPEID = createField("DrinkTypeID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>db_milktea.drinks.UnitPrice</code>.
     */
    public final TableField<DrinksRecord, Double> UNITPRICE = createField("UnitPrice", org.jooq.impl.SQLDataType.FLOAT, this, "");

    /**
     * The column <code>db_milktea.drinks.Image</code>.
     */
    public final TableField<DrinksRecord, byte[]> IMAGE = createField("Image", org.jooq.impl.SQLDataType.BLOB, this, "");

    /**
     * Create a <code>db_milktea.drinks</code> table reference
     */
    public Drinks() {
        this(DSL.name("drinks"), null);
    }

    /**
     * Create an aliased <code>db_milktea.drinks</code> table reference
     */
    public Drinks(String alias) {
        this(DSL.name(alias), DRINKS);
    }

    /**
     * Create an aliased <code>db_milktea.drinks</code> table reference
     */
    public Drinks(Name alias) {
        this(alias, DRINKS);
    }

    private Drinks(Name alias, Table<DrinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private Drinks(Name alias, Table<DrinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return DbMilktea.DB_MILKTEA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.DRINKS_DRINKTYPEID, Indexes.DRINKS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<DrinksRecord, Integer> getIdentity() {
        return Keys.IDENTITY_DRINKS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DrinksRecord> getPrimaryKey() {
        return Keys.KEY_DRINKS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DrinksRecord>> getKeys() {
        return Arrays.<UniqueKey<DrinksRecord>>asList(Keys.KEY_DRINKS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<DrinksRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<DrinksRecord, ?>>asList(Keys.DRINKS_IBFK_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drinks as(String alias) {
        return new Drinks(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drinks as(Name alias) {
        return new Drinks(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Drinks rename(String name) {
        return new Drinks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Drinks rename(Name name) {
        return new Drinks(name, null);
    }
}
