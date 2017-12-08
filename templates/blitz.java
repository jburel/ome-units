/*
 * Copyright (C) 2014 University of Dundee & Open Microscopy Environment.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package omero.model;

import static ome.model.units.Conversion.Mul;
import static ome.model.units.Conversion.Add;
import static ome.model.units.Conversion.Int;
import static ome.model.units.Conversion.Pow;
import static ome.model.units.Conversion.Rat;
import static ome.model.units.Conversion.Sym;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Map;
import java.util.EnumMap;
import java.util.HashMap;

import ome.model.ModelBased;
import ome.model.units.BigResult;
import ome.model.units.Conversion;
import ome.units.unit.Unit;
import ome.util.Filterable;
import ome.util.ModelMapper;
import ome.util.ReverseModelMapper;
import ome.xml.model.enums.EnumerationException;

import omero.model.enums.Units${name};

/**
 * Blitz wrapper around the {@link ome.model.units.${name}} class.
 * Like {@link Details} and {@link Permissions}, this object
 * is embedded into other objects and does not have a full life
 * cycle of its own.
 *
 * @author Josh Moore, josh at glencoesoftware.com
 */
public class ${name}I extends ${name} implements ModelBased {

    private static final long serialVersionUID = 1L;

{% for cfrom in sorted(equations) %}\
    private static Map<Units${name}, Conversion> createMap${cfrom}() {
        EnumMap<Units${name}, Conversion> c =
            new EnumMap<Units${name}, Conversion>(Units${name}.class);
{% for cto, equation in sorted(equations.get(cfrom, {}).items()) %}\
{% if cfrom != cto %}\
        c.put(Units${name}.${cto}, ${equation});
{% end %}\
{% end %}\
        return Collections.unmodifiableMap(c);
    }

{% end %}\
    private static final Map<Units${name}, Map<Units${name}, Conversion>> conversions;
    static {

        Map<Units${name}, Map<Units${name}, Conversion>> c
            = new EnumMap<Units${name}, Map<Units${name}, Conversion>>(Units${name}.class);

{% for cfrom in sorted(equations) %}\
        c.put(Units${name}.${cfrom}, createMap${cfrom}());
{% end %}\
        conversions = Collections.unmodifiableMap(c);
    }

    private static final Map<Units${name}, String> SYMBOLS;
    static {
        Map<Units${name}, String> s = new HashMap<Units${name}, String>();
{% for x in sorted(items) %}\
        s.put(Units${name}.${x.CODE}, "${x.SYMBOL}");
{% end %}\
        SYMBOLS = s;
    }

    public static String lookupSymbol(Units${name} unit) {
        return SYMBOLS.get(unit);
    }

    public static final Ice.ObjectFactory makeFactory(final omero.client client) {

        return new Ice.ObjectFactory() {

            public Ice.Object create(String arg0) {
                return new ${name}I();
            }

            public void destroy() {
                // no-op
            }

        };
    };

    //
    // CONVERSIONS
    //

    public static ome.xml.model.enums.Units${name} makeXMLUnit(String unit) {
        try {
            return ome.xml.model.enums.Units${name}
                    .fromString((String) unit);
        } catch (EnumerationException e) {
            throw new RuntimeException("Bad ${name} unit: " + unit, e);
        }
    }

    public static ome.units.quantity.${name} makeXMLQuantity(double d, String unit) {
        ome.units.unit.Unit<ome.units.quantity.${name}> units =
                ome.xml.model.enums.handlers.Units${name}EnumHandler
                        .getBaseUnit(makeXMLUnit(unit));
        return new ome.units.quantity.${name}(d, units);
    }

   /**
    * FIXME: this should likely take a default so that locations which don't
    * want an exception can have
    *
    * log.warn("Using new PositiveFloat(1.0)!", e); return new
    * PositiveFloat(1.0);
    *
    * or similar.
    */
   public static ome.units.quantity.${name} convert(${name} t) {
       if (t == null) {
           return null;
       }

       Double v = t.getValue();
       // Use the code/symbol-mapping in the ome.model.enums files
       // to convert to the specification value.
       String u = ome.model.enums.Units${name}.valueOf(
               t.getUnit().toString()).getSymbol();
       ome.xml.model.enums.Units${name} units = makeXMLUnit(u);
       ome.units.unit.Unit<ome.units.quantity.${name}> units2 =
               ome.xml.model.enums.handlers.Units${name}EnumHandler
                       .getBaseUnit(units);

       return new ome.units.quantity.${name}(v, units2);
   }


    //
    // REGULAR ICE CLASS
    //

    public final static Ice.ObjectFactory Factory = makeFactory(null);

    public ${name}I() {
        super();
    }

    public ${name}I(double d, Units${name} unit) {
        super();
        this.setUnit(unit);
        this.setValue(d);
    }

    public ${name}I(double d,
            Unit<ome.units.quantity.${name}> unit) {
        this(d, ome.model.enums.Units${name}.bySymbol(unit.getSymbol()));
    }

   /**
    * Copy constructor that converts the given {@link omero.model.${name}}
    * based on the given ome-xml enum
    */
   public ${name}I(${name} value, Unit<ome.units.quantity.${name}> ul) throws BigResult {
       this(value,
            ome.model.enums.Units${name}.bySymbol(ul.getSymbol()).toString());
   }

   /**
    * Copy constructor that converts the given {@link omero.model.${name}}
    * based on the given ome.model enum
    */
   public ${name}I(double d, ome.model.enums.Units${name} ul) {
        this(d, Units${name}.valueOf(ul.toString()));
    }

   /**
    * Copy constructor that converts the given {@link omero.model.${name}}
    * based on the given enum string.
    *
    * @param target String representation of the CODE enum
    */
    public ${name}I(${name} value, String target) throws BigResult {
       String source = value.getUnit().toString();
       if (target.equals(source)) {
           setValue(value.getValue());
           setUnit(value.getUnit());
        } else {
            Units${name} targetUnit = Units${name}.valueOf(target);
            Conversion conversion = conversions.get(value.getUnit()).get(targetUnit);
            if (conversion == null) {
                throw new RuntimeException(String.format(
                    "%f %s cannot be converted to %s",
                        value.getValue(), value.getUnit(), target));
            }
            double orig = value.getValue();
            BigDecimal big = conversion.convert(orig);
            double converted = big.doubleValue();
            if (Double.isInfinite(converted)) {
                throw new BigResult(big,
                        "Failed to convert " + source + ":" + target);
            }

            setValue(converted);
            setUnit(targetUnit);
       }
    }

   /**
    * Copy constructor that converts between units if possible.
    *
    * @param target unit that is desired. non-null.
    */
    public ${name}I(${name} value, Units${name} target) throws BigResult {
        this(value, target.toString());
    }

    /**
     * Convert a Bio-Formats {@link Length} to an OMERO Length.
     */
    public ${name}I(ome.units.quantity.${name} value) {
        ome.model.enums.Units${name} internal =
            ome.model.enums.Units${name}.bySymbol(value.unit().getSymbol());
        Units${name} ul = Units${name}.valueOf(internal.toString());
        setValue(value.value().doubleValue());
        setUnit(ul);
    }

    public double getValue(Ice.Current current) {
        return this.value;
    }

    public void setValue(double value , Ice.Current current) {
        this.value = value;
    }

    public Units${name} getUnit(Ice.Current current) {
        return this.unit;
    }

    public void setUnit(Units${name} unit, Ice.Current current) {
        this.unit = unit;
    }

    public String getSymbol(Ice.Current current) {
        return SYMBOLS.get(this.unit);
    }

    public ${name} copy(Ice.Current ignore) {
        ${name}I copy = new ${name}I();
        copy.setValue(getValue());
        copy.setUnit(getUnit());
        return copy;
    }

    @Override
    public void copyObject(Filterable model, ModelMapper mapper) {
        if (model instanceof ome.model.units.${name}) {
            ome.model.units.${name} t = (ome.model.units.${name}) model;
            this.value = t.getValue();
            this.unit = Units${name}.valueOf(t.getUnit().toString());
        } else {
            throw new IllegalArgumentException(
              "${name} cannot copy from " +
              (model==null ? "null" : model.getClass().getName()));
        }
    }

    @Override
    public Filterable fillObject(ReverseModelMapper mapper) {
        ome.model.enums.Units${name} ut = ome.model.enums.Units${name}.valueOf(getUnit().toString());
        ome.model.units.${name} t = new ome.model.units.${name}(getValue(), ut);
        return t;
    }

    // ~ Java overrides
    // =========================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "${name}(" + value + " " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ${name} other = (${name}) obj;
        if (unit != other.unit)
            return false;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

}
